package com.ckj;

import com.ckj.job.Job;
import com.ckj.service.HouseListService;
import com.ckj.service.impl.*;
import com.ckj.utils.PicDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class SeleniumApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(SeleniumApplication.class, args);

        // HouseListServiceImpl houseListService = new HouseListServiceImpl();
        // houseListService.addAllPageUrl();

    }

}
