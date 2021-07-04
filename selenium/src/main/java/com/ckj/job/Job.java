package com.ckj.job;

import com.ckj.service.HouseListService;
import com.ckj.service.impl.*;
import com.ckj.utils.PicDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.security.pkcs11.P11Util;


import java.io.IOException;

@Component
public class Job {
    @Autowired
    private HouseListServiceImpl houseListService;
    @Autowired
    private HouseDetailServiceImpl houseDetailService;
    @Autowired
    private HousePhotoServiceImpl housePhotoService;
    @Autowired
    private HouseTrendInfoServiceImpl houseTrendInfoService;
    @Autowired
    private HouseSurroundingServiceInfoImpl houseSurroundingService;
    @Autowired
    private HouseQuestionServiceImpl houseQuestionService;

    @Scheduled(fixedDelay = 5000)
    public void test() throws InterruptedException, IOException {
        // this.houseListService.addAllPageUrl();
        for (int i = 0; i < this.houseListService.getUrlSum(); i++) {
            save();
        }

        System.out.println(1);
        PicDownload picDownload = new PicDownload();
    }

    // @Scheduled(fixedDelay = 5000)
    @Transactional
    public void save() throws InterruptedException, IOException {
        this.houseSurroundingService.saveHouseSurrounding();
        this.housePhotoService.saveHousePhoto();
        this.houseTrendInfoService.saveHouseTrendInfo();
        this.houseQuestionService.saveHouseQuestion();
        this.houseDetailService.saveHouseDetailMsg();
        this.houseListService.deleteFirst();
    }

}
