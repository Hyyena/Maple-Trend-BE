package com.mapletrend.maplestampdomainmariadb.repository;

import com.mapletrend.maplestampdomainmariadb.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    
}
