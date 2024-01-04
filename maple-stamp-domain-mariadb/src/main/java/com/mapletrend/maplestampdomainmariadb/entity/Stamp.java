package com.mapletrend.maplestampdomainmariadb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stamp")
@NoArgsConstructor
@Getter
public class Stamp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BigInt")
    private Long id;

    @Column(unique = true, length = 36)
    private String uuid;

    @Column(unique = true, length = 200)
    private String nexonApiKey;

    @Column(unique = true, length = 200)
    private String ocid;

    @Column(unique = true, length = 45)
    private String invenNickname;

    @Column(unique = true, length = 45)
    private String characterName;

    @Column(unique = true, length = 45)
    private String worldName;

    private long characterLevel;

    @Column(length = 20)
    private String battlePower;

    private long unionLevel;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] stampImage;

    @Builder
    public Stamp(String uuid, String nexonApiKey, String ocid, String invenNickname, String characterName,
                 String worldName, long characterLevel, String battlePower, long unionLevel, byte[] stampImage) {
        this.uuid = uuid;
        this.nexonApiKey = nexonApiKey;
        this.ocid = ocid;
        this.invenNickname = invenNickname;
        this.characterName = characterName;
        this.worldName = worldName;
        this.characterLevel = characterLevel;
        this.battlePower = battlePower;
        this.unionLevel = unionLevel;
        this.stampImage = stampImage;
    }
}
