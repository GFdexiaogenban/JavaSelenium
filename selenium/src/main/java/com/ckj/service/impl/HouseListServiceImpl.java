package com.ckj.service.impl;

import com.ckj.dao.HouseListDao;
import com.ckj.pojo.HouseList;
import com.ckj.service.HouseListService;
import com.ckj.utils.Driver;
import com.ckj.utils.MyUtils;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class HouseListServiceImpl implements HouseListService {

    @Autowired
    private HouseListDao houseListDao;

    String pUrl = "https://cq.loupan.com/xinfang/";
    // String pUrl = "https://cq.loupan.com/xinfang/p35/";


    @Override
    public int getFirstId() {
        return this.houseListDao.findAll().get(0).getId();
    }

    @Override
    public String getFirstUrl() {
        return this.houseListDao.findAll().get(0).getUrl();
    }


    @Transactional
    @Override
    public void deleteFirst() {
        List<HouseList> list = this.houseListDao.findAll();
        this.houseListDao.deleteById(list.get(0).getId());
    }

    @Override
    public int getUrlSum() {
        return houseListDao.findAll().size();
    }

    @Override
    public void addAllPageUrl() throws InterruptedException {
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));

        driver.get(this.pUrl);
        String nextPageXpath = "/html/body/div[@class='pages']/div[@class='pages pages-list']/div[@class='main box']/div[@class='left']/div[@class='page-turn']/div[@class='item-turn']/a[@class='pagenxt']";

        MyUtils myUtils = new MyUtils();
        while (myUtils.doesWebElementExistByXpath(driver, nextPageXpath) == true) {
            WebElement nextPageElement = driver.findElement(By.xpath(nextPageXpath));
            Thread.sleep(1000);
            getNowPageList(driver);
            System.out.println(nextPageElement.getAttribute("href"));
            driver.get(nextPageElement.getAttribute("href"));

        }
        Thread.sleep(1000);
        getNowPageList(driver);
        System.out.println(1111);
        driver.close();
    }

    public void getNowPageList(WebDriver driver) {
        List<WebElement> elements = driver.findElements(By.xpath("//div[@class='pages']/div[@class='pages pages-list']/div[@class='main box']/div[@class='left']/ul[@class='list-house']/li/a"));
        for (WebElement element : elements) {
            System.out.println("add url ==>" + element.getAttribute("href"));

            String urlPage = element.getAttribute("href");
            HouseList houseList = new HouseList();
            houseList.setUrl(urlPage);
            this.houseListDao.save(houseList);

        }
    }
}
