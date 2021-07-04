package com.ckj.service.impl;

import com.ckj.dao.HouseDetailDao;
import com.ckj.pojo.HouseDetail;
import com.ckj.service.HouseDetailService;
import com.ckj.utils.Driver;
import com.ckj.utils.MyUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HouseDetailServiceImpl implements HouseDetailService {
    @Autowired
    private HouseListServiceImpl houseListService;

    @Autowired
    HouseDetailDao houseDetailDao;

    // /usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs
    public String getDetailPageUrl() {
        // ChromeOptions chromeOptions = new ChromeOptions();
        //
        // chromeOptions.addArguments("headless");//无界面参数
        // chromeOptions.addArguments("no-sandbox");//禁用沙盒 就是被这个参数搞了一天
        //
        // WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));
        driver.manage().window().maximize();
        driver.get(this.houseListService.getFirstUrl());
        WebElement element = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail']/div[@id='navigate']/a[2]"));
        String detailPageUrl = element.getAttribute("href");
        driver.close();
        return detailPageUrl;
    }


    @Transactional
    public void saveHouseDetailMsg() throws InterruptedException {
        // ChromeOptions chromeOptions = new ChromeOptions();
        //
        // chromeOptions.addArguments("headless");//无界面参数
        // chromeOptions.addArguments("no-sandbox");//禁用沙盒 就是被这个参数搞了一天
        //
        // WebDriver driver = new ChromeDriver(chromeOptions);
        // driver.manage().window().maximize();

        WebDriver driver = new Driver().getDriver();
        // System.setProperty("phantomjs.binary.path", "E:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        // System.setProperty("phantomjs.binary.path", "/usr/lib/phantomjs/phantomjs-2.1.1-linux-x86_64/bin/phantomjs");
        // PhantomJSDriver driver = new PhantomJSDriver();
        // driver.manage().window().setSize(new Dimension(1920,1080));
        // driver = new RemoteWebDriver(getDetailPageUrl(), chromeCap);

        driver.get(getDetailPageUrl());
        // driver.get("https://cq.loupan.com/info/7096758.html");
        Thread.sleep(1000);
        HouseDetail houseDetail = new HouseDetail();
        MyUtils myUtils = new MyUtils();

        houseDetail.setHouseId(houseListService.getFirstId());
        //房屋名称
        houseDetail.setHouseName(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='title box']/div[@class='t']/h1")).getText());
        //房屋别名
        if (myUtils.doesWebElementExistByXpath(driver, "/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail']/div[@class='title box']/div[@class='t']/p")) {
            houseDetail.setHouseAlias(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-detail']/div[@class='title box']/div[@class='t']/p")).getText());
        } else {
            houseDetail.setHouseAlias("无");
        }
        //房屋价格
        String price = driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[1]")).getText();
        houseDetail.setPrice(price);
        //物业类别
        houseDetail.setPropertyType(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[3]/span[@class='liData']")).getText());
        //物业公司
        houseDetail.setPropertyCompany(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[5]/span[@class='liData']")).getText());
        //装修状况
        houseDetail.setDecorationStatus(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[7]/span[@class='liData']")).getText());
        //开盘时间-地点
        houseDetail.setOpeningTimePlace(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[@class='full'][1]")).getText());
        //售楼地址
        houseDetail.setSalesAddress(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[10]")).getText());
        //项目位置
        houseDetail.setLocation(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[11]")).getText());
        //项目特色
        houseDetail.setProjectFeatures(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[2]/span[@class='liData']")).getText());
        //入住时间
        houseDetail.setCheckInTime(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[@class='wuzheng wt']/span[@class='liData']")).getText());
        //物业费用
        houseDetail.setPropertyFee(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[6]/span[@class='liData']")).getText());
        //产权年限
        houseDetail.setTermOfPropertyRight(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[8]/span[@class='liData']")).getText());

        List<WebElement> baseInfoElementList = driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo']"));
        if (baseInfoElementList.size() == 3) {
            ///html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][1]/ul/li[@class='full'][3]如果等于三，则没有销售许可证
            //开发商
            houseDetail.setDeveloper(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[1]")).getText());
            //建筑面积
            houseDetail.setBuildingArea(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[3]")).getText());
            //车位
            houseDetail.setCarport(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[5]")).getText());
            //当前户数
            houseDetail.setNowHouseholdNum(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[7]")).getText());
            //绿化率
            houseDetail.setGreening(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[9]")).getText());
            //建筑类型
            houseDetail.setBuildingType(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[2]")).getText());
            //占地面积
            houseDetail.setArea(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[4]")).getText());
            //总户数
            houseDetail.setTotalHouseholdNum(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[6]")).getText());
            //容积率
            houseDetail.setFra(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/ul/li[8]")).getText());
            //其他信息
            houseDetail.setOtherInformation(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul")).getText());
        }
        if (baseInfoElementList.size() == 4) {
            //预售许可证
            houseDetail.setPreSalePermit(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/table[@class='licence-table']/tbody/tr/td[1]")).getText());
            //发证时间
            houseDetail.setIssuingTime(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/table[@class='licence-table']/tbody/tr/td[2]")).getText());
            //楼栋号
            houseDetail.setBuildingNumber(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][2]/table[@class='licence-table']/tbody/tr/td[3]")).getText());

            //开发商
            houseDetail.setDeveloper(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[1]")).getText());
            //建筑面积
            houseDetail.setBuildingArea(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[3]")).getText());
            //车位
            houseDetail.setCarport(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[5]")).getText());
            //当前户数
            houseDetail.setNowHouseholdNum(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[7]")).getText());
            //绿化率
            houseDetail.setGreening(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[9]")).getText());
            //建筑类型
            houseDetail.setBuildingType(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[2]")).getText());
            //占地面积
            houseDetail.setArea(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[4]")).getText());
            //总户数
            houseDetail.setTotalHouseholdNum(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[6]")).getText());
            //容积率
            houseDetail.setFra(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul/li[8]")).getText());
            //其他信息
            houseDetail.setOtherInformation(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='baseInfo'][3]/ul")).getText());
        } else {
            houseDetail.setPreSalePermit("暂无");
            houseDetail.setIssuingTime("暂无");
            houseDetail.setBuildingNumber("暂无");
        }
        //项目简介
        houseDetail.setProjectBrief(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content'][1]/div[@class='desc']/p[2]")).getText());
        //周边配套
        houseDetail.setSurroundingFacilities(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content'][2]/div[@class='desc']/p")).getText());
        //小区配套
        houseDetail.setCommunitySupporting(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content'][3]/div[@class='desc']/p")).getText());
        //交通情况
        houseDetail.setTrafficSituation(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content'][4]/div[@class='desc']/p")).getText());
        if (driver.findElements(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content']")).size() == 6) {
            houseDetail.setFloorCondition(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content'][5]/div[@class='desc']/p")).getText());
            //交付标准
            houseDetail.setDeliveryStandard(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content'][6]/div[@class='desc']/p")).getText());
        } else {
            houseDetail.setFloorCondition("");
            houseDetail.setDeliveryStandard(driver.findElement(By.xpath("/html/body/div[@class='pages']/div[@class='pages pages-house pages-info']/div[@class='container box']/div[@class='house-left']/div[@class='content'][5]/div[@class='desc']/p")).getText());
        }

        //当前页面url
        houseDetail.setUrl(driver.getCurrentUrl());
        houseDetailDao.save(houseDetail);
        System.out.println(1);
        driver.close();

    }


}
