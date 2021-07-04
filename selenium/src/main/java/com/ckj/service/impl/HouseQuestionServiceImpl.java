package com.ckj.service.impl;

import com.ckj.dao.HouseQuestionDao;
import com.ckj.pojo.HouseQuestion;
import com.ckj.service.HouseQuestionService;
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
public class HouseQuestionServiceImpl implements HouseQuestionService {
    @Autowired
    private HouseListServiceImpl houseListService;

    @Autowired
    private HouseQuestionDao houseQuestionDao;

    public String getHouseQuestionUrl() {
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));

        driver.get(this.houseListService.getFirstUrl());
        WebElement element = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail']/div[@id='navigate']/a[8]"));
        String houseQuestionUrl = element.getAttribute("href");
        driver.close();
        return houseQuestionUrl;
    }

    @Override

    public void saveHouseQuestion() throws InterruptedException {
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));

        driver.get(getHouseQuestionUrl());
        Thread.sleep(1000);
        MyUtils myUtils = new MyUtils();
        // PicDownload picDownload = new PicDownload();
        // while (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-dtlb']/div[@class='container box']/div[@class='house-left']/div[@class='list-dt']/div[@class='more']/a")) {
        //     driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dtlb']/div[@class='container box']/div[@class='house-left']/div[@class='list-dt']/div[@class='more']/a")).click();
        //     Thread.sleep(500);
        // }
        int houseId = houseListService.getFirstId();
        String houseName = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dp']/div[@class='title box']/div[@class='t']/h1")).getText();
        String url = driver.getCurrentUrl();

        if (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-dp']/div[@class='container box']/div[@class='house-left']/div[@class='askBlock']/div[@class='list']/dd/div[@class='ask']/p[@class='questionText']")) {
            List<WebElement> questionList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dp']/div[@class='container box']/div[@class='house-left']/div[@class='askBlock']/div[@class='list']/dd/div[@class='ask']/p[@class='questionText']"));
            List<WebElement> answerList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-dp']/div[@class='container box']/div[@class='house-left']/div[@class='askBlock']/div[@class='list']/dd/div[@class='answer']/p[@class='answerText']"));
            for (int i = 0; i < questionList.size(); i++) {
                String questionString = questionList.get(i).getText();
                String answerString = answerList.get(i).getText();
                HouseQuestion houseQuestion = new HouseQuestion();

                houseQuestion.setHouseId(houseId);
                houseQuestion.setHouseName(houseName);
                houseQuestion.setQuestion(questionString);
                houseQuestion.setAnswer(answerString);
                houseQuestion.setUrl(url);
                houseQuestionDao.save(houseQuestion);
            }
        }

        // PicDownload picDownload = new PicDownload();
        System.out.println("debug");
        driver.close();
    }
}
