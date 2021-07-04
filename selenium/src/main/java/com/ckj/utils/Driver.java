package com.ckj.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Driver {
    public WebDriver getDriver(){

        ChromeOptions chromeOptions = new ChromeOptions();

        // chromeOptions.addArguments("headless");//无界面参数
        // chromeOptions.addArguments("no-sandbox");//禁用沙盒 就是被这个参数搞了一天

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().window().maximize();
        return driver;
    }
}
