package com.mapletrend.appmaplestampapi.service;

import com.mapletrend.maplestampdomainmariadb.entity.Stamp;
import com.mapletrend.maplestampdomainmariadb.repository.StampRepository;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class StampService {

    private final StampRepository stampRepository;

    @Transactional
    public byte[] createStampImage(JSONObject jsonObject) {

        String invenNickname = (String) jsonObject.get("invenNickname");
        String formattedCharacterLevel = (String) jsonObject.get("formattedCharacterLevel");
        String formattedBattlePower = (String) jsonObject.get("formattedBattlePower");
        String formattedUnionLevel = (String) jsonObject.get("formattedUnionLevel");
        String uuid = (String) jsonObject.get("uuid");
        String date = (String) jsonObject.get("date");

        String characterName = (String) jsonObject.get("characterName");
        String worldName = (String) jsonObject.get("worldName");
        String ocid = (String) jsonObject.get("ocid");
        String nexonApiKey = (String) jsonObject.get("nexonApiKey");
        long characterLevel = (long) jsonObject.get("characterLevel");
        long unionLevel = (long) jsonObject.get("unionLevel");
        String battlePower = (String) jsonObject.get("battlePower");

        Optional<Stamp> isExistingStamp = stampRepository.findByNexonApiKeyAndInvenNicknameAndCharacterName(
                nexonApiKey, invenNickname, characterName);

        if (isExistingStamp.isPresent()) {
            uuid = isExistingStamp.get().getUuid();
        }

        int width = 600;
        int height = 200;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, 600, 200);

        int fontSize = 30;
        graphics2D.setFont(new Font("Nanum", Font.BOLD, fontSize));
        graphics2D.setColor(Color.black);
        graphics2D.drawString(invenNickname, 30, 60);

        graphics2D.setFont(new Font("Nanum", Font.PLAIN, 20));
        graphics2D.drawString("레벨: " + formattedCharacterLevel, 30, 100);
        graphics2D.drawString("전투력: " + formattedBattlePower, 30, 130);
        graphics2D.drawString("유니온: " + formattedUnionLevel, 30, 160);

        Font detailsFont = new Font("Nanum", Font.PLAIN, 12);
        graphics2D.setFont(detailsFont);
        // Calculate position for the text
        FontMetrics metrics = graphics2D.getFontMetrics(detailsFont);
        String issueDate = "발급일: " + date;
        String issueNumber = "발급번호: " + uuid;
        int textWidthDate = metrics.stringWidth(issueDate);
        int textWidthNumber = metrics.stringWidth(issueNumber);
        int textHeight = metrics.getHeight();
        int xDate = width - textWidthDate - 10; // 10 pixels from right edge
        int xNumber = width - textWidthNumber - 10; // 10 pixels from right edge
        int yDate = height - textHeight - 10; // 10 pixels from bottom edge
        int yNumber = height - 10; // 10 pixels from bottom edge

        // Draw the issue date and number
        graphics2D.drawString(issueDate, xDate, yDate);
        graphics2D.drawString(issueNumber, xNumber, yNumber);

        // Adding a watermark
        int watermarkFontSize = 20;
        String watermarkText = invenNickname;

        // Set watermark properties
        graphics2D.setFont(new Font("Nanum", Font.PLAIN, watermarkFontSize));
        graphics2D.setColor(new Color(0, 0, 0, 128));
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));

        // Calculate the diagonal step for watermark placement
        int stepX = graphics2D.getFontMetrics().stringWidth(watermarkText) + 10; // 10 is the spacing between watermarks
        int stepY = watermarkFontSize + 10; // Adjust the vertical spacing

        // Draw the watermark in a diagonal pattern
        for (int x = 0; x < width; x += stepX) {
            for (int y = 0; y < height; y += stepY) {
                graphics2D.drawString(watermarkText, x, y);
            }
        }

        graphics2D.dispose();

//        try {
//            File file = new File("stamp.jpg");
//            ImageIO.write(bufferedImage, "jpg", file);
//        } catch (IOException e) {
//            log.error("이미지 파일 생성 중 오류가 발생했습니다.", e);
//        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] bytes = baos.toByteArray();

            Stamp stamp = Stamp.builder()
                    .uuid(uuid)
                    .nexonApiKey(nexonApiKey)
                    .ocid(ocid)
                    .invenNickname(invenNickname)
                    .characterName(characterName)
                    .worldName(worldName)
                    .characterLevel(characterLevel)
                    .battlePower(battlePower)
                    .unionLevel(unionLevel)
                    .stampImage(bytes)
                    .build();

            if (isExistingStamp.isPresent()) {
                Stamp existingStamp = isExistingStamp.get();
                existingStamp.updateStamp(ocid, worldName, characterLevel, battlePower, unionLevel, bytes);
                log.info("이미 존재하는 스탬프입니다.");
                return bytes;
            }

            stampRepository.save(stamp);

            return bytes;
        } catch (IOException e) {
            log.error("이미지 파일 생성 중 오류가 발생했습니다.", e);
        }
        return new byte[0];
    }

    @Transactional(readOnly = true)
    public byte[] getStampImage(String invenNickname) {
        Optional<Stamp> stamp = stampRepository.findByInvenNickname(invenNickname);
        if (stamp.isPresent()) {
            return stamp.get().getStampImage();
        }
        return new byte[0];
    }
}

