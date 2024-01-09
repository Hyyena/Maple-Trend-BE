package com.mapletrend.maplestampdomainmariadb.repository;

import com.mapletrend.maplestampdomainmariadb.entity.Stamp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    Optional<Stamp> findByUuid(String uuid);

    Optional<Stamp> findByInvenNickname(String invenNickname);

    Optional<Stamp> findByNexonApiKeyAndInvenNicknameAndCharacterName(String apiKey, String invenNickname,
                                                                      String characterName);
}
