package com.ckj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HouseQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int houseId;
    private String houseName;
    private String question;
    private String answer;
    private String url;
}
