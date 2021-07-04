package com.ckj.service.impl;

import com.ckj.dao.HouseTrendInfoDao;
import com.ckj.pojo.HouseTrendInfo;
import com.ckj.service.HouseTrendInfoService;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class HouseTrendInfoServiceImpl {
    @Autowired
    private HouseListServiceImpl houseListService;

    @Autowired
    private HouseTrendInfoDao houseTrendInfoDao;

    public String getHouseTrendInfoUrl() {
        WebDriver driver = new Driver().getDriver();

        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));
        driver.get(this.houseListService.getFirstUrl());
        WebElement element = driver.findElement(By.xpath("html/body/div[@class='pages']/div[@class='pages pages-house pages-detail']/div[@id='navigate']/a[3]"));
        String houseTrendInfoUrl = element.getAttribute("href");
        driver.close();
        return houseTrendInfoUrl;
    }

    @Transactional
    public void saveHouseTrendInfo() throws InterruptedException {
        WebDriver driver = new Driver().getDriver();

        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));
        driver.get(getHouseTrendInfoUrl());
        Thread.sleep(1000);
        MyUtils myUtils = new MyUtils();
        PicDownload picDownload = new PicDownload();
        while (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-dtlb']/div[@class='container box']/div[@class='house-left']/div[@class='list-dt']/div[@class='more']/a")) {
            driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dtlb']/div[@class='container box']/div[@class='house-left']/div[@class='list-dt']/div[@class='more']/a")).click();
            Thread.sleep(500);
        }
        List<WebElement> trendInfoPageList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dtlb']/div[@class='container box']/div[@class='house-left']/div[@class='list-dt']/ul[@id='load-timer']/li/div[@class='t']/a"));
        List<String> pageUrlList = new ArrayList<String>();
        for (int i = 0; i < trendInfoPageList.size(); i++) {
            pageUrlList.add(trendInfoPageList.get(i).getAttribute("href"));
        }

        int houseId = houseListService.getFirstId();
        String houseName = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dtlb']/div[@class='title box']/div[@class='t']/h1")).getText();

        for (int i = 0; i < pageUrlList.size(); i++) {
            driver.get(pageUrlList.get(i));
            String trendTitle = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dt']/div[@class='container box']/div[@class='house-left']/h1")).getText();
            String trendTime = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dt']/div[@class='container box']/div[@class='house-left']/div[@class='other']/div[@class='date']")).getText();
            String contents = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dt']/div[@class='container box']/div[@class='house-left']/div[@class='article']")).getText();
            String directory = "";
            String trendUrl = driver.getCurrentUrl();
            HouseTrendInfo houseTrendInfo = new HouseTrendInfo();
            houseTrendInfo.setHouseId(houseId);
            houseTrendInfo.setHouseName(houseName);
            houseTrendInfo.setTrendTitle(trendTitle);
            houseTrendInfo.setTrendTime(trendTime);
            houseTrendInfo.setContents(contents);
            houseTrendInfo.setDirectory(directory);
            houseTrendInfo.setTrendUrl(trendUrl);
            houseTrendInfoDao.save(houseTrendInfo);
        }
        driver.close();
    }
}
