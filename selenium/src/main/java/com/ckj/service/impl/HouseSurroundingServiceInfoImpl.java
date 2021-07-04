package com.ckj.service.impl;

import com.ckj.dao.HouseSurroundingInfoDao;
import com.ckj.pojo.HouseSurroundingInfo;
import com.ckj.service.HouseSurroundingInfoService;
import com.ckj.utils.Driver;
import com.ckj.utils.MyUtils;
import com.ckj.utils.PicDownload;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HouseSurroundingServiceInfoImpl implements HouseSurroundingInfoService {
    @Autowired
    private HouseListServiceImpl houseListService;

    @Autowired
    private HouseSurroundingInfoDao houseSurroundingInfoDao;

    public String getHouseSurroundingUrl() {
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));
        driver.get(this.houseListService.getFirstUrl());
        WebElement element = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail']/div[@id='navigate']/a[6]"));
        String houseSurroundingUrl = element.getAttribute("href");
        driver.close();
        return houseSurroundingUrl;
    }

    @Transactional
    public void saveHouseSurrounding() throws InterruptedException {
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));
        driver.get(getHouseSurroundingUrl());
        Thread.sleep(1000);
        MyUtils myUtils = new MyUtils();

        List<WebElement> elements = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail pages-map']/div[@class='peitao box']/div[@class='mapbox']/div[@class='edit']/ul[@class='tab']/li/p"));
        List<String> typeList = new ArrayList<String>();
        for (int i = 0; i < elements.size(); i++) {
            typeList.add(elements.get(i).getText());
        }
        System.out.println(1);

        //固定的部分
        int houseId = houseListService.getFirstId();
        String houseName = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail pages-map']/div[@class='title box']/div[@class='t']/h1")).getText();

        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).click();
            Thread.sleep(1500);
            //将边上的建筑的名字存入List
            List<WebElement> surroundingList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail pages-map']/div[@class='peitao box']/div[@class='mapbox']/div[@class='edit']/div[@class='list']/ul[@id='map-show']/li/p"));
            List<String> surroundingTextList = new ArrayList<String>();
            for (int j = 0; j < surroundingList.size(); j++) {
                surroundingTextList.add(surroundingList.get(j).getText());
            }

            //将距离存入List中
            List<String> surroundingDistanceStringList = new ArrayList<String>();
            List<WebElement> surroundingDistanceList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail pages-map']/div[@class='peitao box']/div[@class='mapbox']/div[@class='edit']/div[@class='list']/ul[@id='map-show']/li/span"));
            for (int j = 0; j < surroundingDistanceList.size(); j++) {
                surroundingDistanceStringList.add(surroundingDistanceList.get(j).getText());
            }
            //固定的部分
            String type = typeList.get(i);
            String url = driver.getCurrentUrl();
            //不定的部分
            WebDriver baiduDriver = new Driver().getDriver();
            baiduDriver.manage().window().maximize();
            // PhantomJSDriver baiduDriver = new PhantomJSDriver();
            // driver.manage().window().setSize(new Dimension(1920,1080));
            baiduDriver.get("http://api.map.baidu.com/lbsapi/getpoint/index.html");
            // 重庆百度地图官网：
            for (int j = 0; j < surroundingTextList.size(); j++) {
                WebElement inputElement = baiduDriver.findElement(By.xpath("/html/body/div[@class='content']/div[@class='LogoCon clear']/div[@class='search map_search']/input[@id='localvalue']"));
                inputElement.clear();
                inputElement.sendKeys(surroundingTextList.get(j));
                inputElement.sendKeys(Keys.ENTER);
                Thread.sleep(1000);
                float coordinateX = 0;
                float coordinateY = 0;
                if (myUtils.doesWebElementExistByXpath(baiduDriver, "/html/body/div[@class='content']/div[@id='wrapper']/div[@id='MapInfo']/div[@id='txtPanel']/ul[@class='local_s']/li[@id='no0']/div[@id='no_0']")) {
                    WebElement element = baiduDriver.findElements(By.xpath("/html/body/div[@class='content']/div[@id='wrapper']/div[@id='MapInfo']/div[@id='txtPanel']/ul[@class='local_s']/li[@id='no0']/div[@id='no_0']")).get(0);
                    element.click();
                    Thread.sleep(100);
                    String xANDy = baiduDriver.findElement(By.xpath("/html/body/div[@class='content']/div[@class='LogoCon clear']/div[3]/div[@class='pointContainer']/input[@id='pointInput']")).getAttribute("data-clipboard-text");
                    String temp[];
                    temp = xANDy.split(",");
                    String x = temp[0];
                    String y = temp[1];
                    coordinateX = Float.parseFloat(x);
                    coordinateY = Float.parseFloat(y);
                } else {
                    baiduDriver.findElement(By.xpath("/html/body/div[@class='content']/div[@class='dt_nav']/ul[@class='nav']/li[1]/div[@class='l']/span/a[@id='curCityText']")).click();
                    Thread.sleep(100);
                    baiduDriver.findElement(By.xpath("/html/body/div[@id='map_popup']/div[@class='popup_main']/div[@class='sel_city']/table[@id='selCity']/tbody/tr[2]/td[2]/nobr[4]/a")).click();
                    Thread.sleep(200);
                }

                String coordinateName = surroundingTextList.get(j);
                String distance = surroundingDistanceStringList.get(j);
                HouseSurroundingInfo houseSurroundingInfo = new HouseSurroundingInfo();

                houseSurroundingInfo.setHouseId(houseId);
                houseSurroundingInfo.setHouseName(houseName);
                houseSurroundingInfo.setType(type);
                houseSurroundingInfo.setCoordinateName(coordinateName);
                houseSurroundingInfo.setX(coordinateX);
                houseSurroundingInfo.setY(coordinateY);
                houseSurroundingInfo.setDistance(distance);
                houseSurroundingInfo.setUrl(url);
                houseSurroundingInfoDao.save(houseSurroundingInfo);
            }
            baiduDriver.close();
        }


        driver.close();
    }
}
