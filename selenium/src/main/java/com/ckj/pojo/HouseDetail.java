package com.ckj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private int id;// 编号
  private int houseId;
  private String houseName;//房屋名称
  private String houseAlias;//房屋别名
  private String price;//价格
  private String propertyType;//物业类型
  private String propertyCompany;//物业公司
  private String decorationStatus;//装修情况
  private String openingTimePlace;//开盘时间-地点
  private String salesAddress;//售楼地址
  private String location;//项目位置
  private String projectFeatures;//项目特色
  private String checkInTime;//入住时间
  private String propertyFee;//物业费
  private String termOfPropertyRight;//产权年限

  private String preSalePermit;//预售许可证
  private String issuingTime;//发证时间
  private String buildingNumber;//楼栋号

  //private String architecturalPlanning;//建筑规划
  private String developer; //开发商
  private String buildingArea; //建筑面积
  private String carport; //车位
  private String nowHouseholdNum; //户数
  private String greening; //绿化率
  private String buildingType; //建筑类型
  private String area; // 占地面积
  private String totalHouseholdNum; //总户数
  private String fra; //容积率

  private String projectBrief;//项目简介
  private String surroundingFacilities;//周边配套
  private String communitySupporting;//小区配套
  private String trafficSituation;//交通情况
  private String floorCondition;//楼层状况
  private String deliveryStandard;//交付标准
  private String otherInformation;//其他信息
  private String url;
}
