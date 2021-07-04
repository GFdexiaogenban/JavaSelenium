package com.ckj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseTrendInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;// 编号
    private int houseId;
    private String houseName;
    private String trendTitle;
    private String trendTime;
    private String contents;
    private String directory;
    private String trendUrl;

}
