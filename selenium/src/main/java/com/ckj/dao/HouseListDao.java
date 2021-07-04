package com.ckj.dao;

import com.ckj.pojo.HouseList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseListDao extends JpaRepository<HouseList,Long> {
    public void deleteById(int id);
}
