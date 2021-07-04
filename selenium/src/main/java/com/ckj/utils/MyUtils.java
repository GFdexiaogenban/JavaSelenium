package com.ckj.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class MyUtils {
    public boolean doesWebElementExistByXpath(WebDriver driver, String xpathString) {
        try {
            driver.findElement(By.xpath(xpathString));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
