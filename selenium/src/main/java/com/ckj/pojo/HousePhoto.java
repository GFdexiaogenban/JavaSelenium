package com.ckj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class HousePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private int id;// 编号
    private int houseId;
    private String houseName;
    private String picType;
    private String picName;
    private String picDirectory;
    private String url;
}
