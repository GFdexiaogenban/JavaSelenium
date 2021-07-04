package com.ckj.dao;

import com.ckj.pojo.HouseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseDetailDao  extends JpaRepository<HouseDetail,Long> {
    public HouseDetail findById(Integer id);
}
