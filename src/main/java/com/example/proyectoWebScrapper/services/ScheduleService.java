package com.example.proyectoWebScrapper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class ScheduleService {
    @Autowired
    private SpiderService spiderService;
    @Scheduled(cron = "0 30 1 * * ?")
    public void scheduleIndexWebPage(){
        spiderService.indexWebPages();
    }
}
