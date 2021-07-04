package com.ckj.service.impl;

import com.ckj.dao.HousePhotoDao;
import com.ckj.pojo.HouseDetail;
import com.ckj.pojo.HousePhoto;
import com.ckj.service.HousePhotoService;
import com.ckj.utils.Driver;
import com.ckj.utils.MyUtils;
import com.ckj.utils.PicDownload;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HousePhotoServiceImpl implements HousePhotoService {
    @Autowired
    private HouseListServiceImpl houseListService;

    @Autowired
    private HousePhotoDao housePhotoDao;

    public String getHousePhotoUrl() {
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));

        driver.get(this.houseListService.getFirstUrl());
        WebElement element = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail']/div[@id='navigate']/a[5]"));
        String photoPageUrl = element.getAttribute("href");
        driver.close();
        return photoPageUrl;
    }

    @Transactional
    public void saveHousePhoto() throws InterruptedException, IOException {
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));

        driver.get(getHousePhotoUrl());
        Thread.sleep(1000);

        MyUtils myUtils = new MyUtils();
        PicDownload picDownload = new PicDownload();

        // 固定部分
        int houseId = this.houseListService.getFirstId();
        String houseName = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='title box']/div[@class='t']/h1")).getText();

        int picNumber = 1;
        // 不确定部分
        List<WebElement> picTypeElementList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-right']/div[@class='hx-nav']/ul/li/a"));
        List<WebElement> elements = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-right']/div[@class='hx-nav']/ul/li/a/p"));
        List<String> urlList = new ArrayList<String>();
        List<String> picTypeStringList = new ArrayList<String>();
        for (int i = 0; i < picTypeElementList.size(); i++) {
            urlList.add(picTypeElementList.get(i).getAttribute("href"));
            picTypeStringList.add(elements.get(i).getText());
        }

        System.out.println(1);
        for (int i = 0; i < urlList.size(); i++) {
            String turnUrl = urlList.get(i);
            driver.get(turnUrl);
            // e.click();

            String picType = picTypeStringList.get(i);
            Thread.sleep(1000);

            // 特殊情况 户型图
            while (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='hxlb']/ul/li/a[@class='img']")) {
                //如果存在
                List<WebElement> picList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='hxlb']/ul/li/a[@class='img']/img"));
                for (WebElement pic : picList) {
                    String picUrl = pic.getAttribute("src");
                    String picName = picNumber + ".jpg";
                    String picDirectory = picDownload.download(picUrl, houseName, picType, picName);

                    //每下载完一张图片，保存一条数据库记录
                    HousePhoto housePhoto = new HousePhoto();

                    housePhoto.setHouseId(houseId);
                    housePhoto.setHouseName(houseName);
                    housePhoto.setPicType(picType);
                    housePhoto.setPicName(picName);
                    housePhoto.setPicDirectory(picDirectory);
                    housePhoto.setUrl(picUrl);
                    housePhotoDao.save(housePhoto);
                    picNumber++;
                }
                //判断下一页是否存在，如果存在就进行点击
                if (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='xclb']/div[@class='page-turn']/div[@class='item-turn']/a[@class='ui-paging-next']")) {
                    driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='xclb']/div[@class='page-turn']/div[@class='item-turn']/a[@class='ui-paging-next']")).click();
                    Thread.sleep(1000);
                } else {
                    //如果不存在下一页，跳出循环
                    break;
                }
                System.out.println(1);
            }


            //判断图片是否存在，如果存在就进入循环，下载图片
            while (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='xclb']/ul/li/a[@class='img']")) {
                //如果存在
                List<WebElement> picList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='xclb']/ul/li/a[@class='img']/img"));
                for (WebElement pic : picList) {
                    String picUrl = pic.getAttribute("src");
                    String picName = picNumber + ".jpg";
                    String picDirectory = picDownload.download(picUrl, houseName, picType, picName);

                    //每下载完一张图片，保存一条数据库记录
                    HousePhoto housePhoto = new HousePhoto();

                    housePhoto.setHouseId(houseId);
                    housePhoto.setHouseName(houseName);
                    housePhoto.setPicType(picType);
                    housePhoto.setPicName(picName);
                    housePhoto.setPicDirectory(picDirectory);
                    housePhoto.setUrl(picUrl);
                    housePhotoDao.save(housePhoto);
                    picNumber++;
                }
                //判断下一页是否存在，如果存在就进行点击
                if (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='xclb']/div[@class='page-turn']/div[@class='item-turn']/a[@class='ui-paging-next']")) {
                    driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-hx']/div[@class='container box']/div[@class='house-left']/div[@class='xclb']/div[@class='page-turn']/div[@class='item-turn']/a[@class='ui-paging-next']")).click();
                    Thread.sleep(1000);
                } else {
                    //如果不存在下一页，跳出循环
                    break;
                }
                System.out.println(1);
            }

        }
        driver.close();
    }
}
