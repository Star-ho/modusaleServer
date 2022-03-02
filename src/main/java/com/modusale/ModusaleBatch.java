package com.modusale;

import com.modusale.requests.yogiyo.YogiyoRequest;
import com.modusale.requests.baemin.BaeminRequest;
import com.modusale.requests.coupang.CoupangRequest;
import com.modusale.utils.AppDataObj;
import com.modusale.utils.GitHubData;
import com.modusale.utils.TelegramAPI;
import com.modusale.requests.wemefo.WemefoRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

@Component
public class ModusaleBatch {

    private final AppDataObj appDataObj;
    private final BaeminRequest baeminRequest;
    private final YogiyoRequest yogiyoRequest;
    private final CoupangRequest coupangRequest;
    private final WemefoRequest wemefoRequest;
    private final TelegramAPI telegramAPI;
    private final GitHubData gitHubData;


    public ModusaleBatch(AppDataObj appDataObj, BaeminRequest baeminRequest, YogiyoRequest yogiyoRequest, CoupangRequest coupangRequest,
                         WemefoRequest wemefoRequest, TelegramAPI telegramAPI, GitHubData gitHubData ){
        this.appDataObj=appDataObj;
        this.baeminRequest=baeminRequest;
        this.yogiyoRequest=yogiyoRequest;
        this.wemefoRequest=wemefoRequest;
        this.coupangRequest=coupangRequest;
        this.telegramAPI=telegramAPI;
        this.gitHubData=gitHubData;
    }

    public void consume(){
        telegramAPI.send("refresh!!");
        gitHubData.setGithubData();
        wemefBatch();
        baeminYogiyoCoupangBatch();
    }


    @Scheduled(initialDelay = 1000*60*5, fixedDelay = 1000*60*5)
    public void fiveMinBatch(){
        baeminYogiyoCoupangBatch();
    }

    @Scheduled(initialDelay = 1000*60*60*6, fixedDelay = 1000*60*60*6)
    public void sixHourBatch(){
        gitHubData.setGithubData();
    }


    @Scheduled(cron = "* 2,32 * * * *")// 매 정각, 30분마다
    public void thirtyminBatch() throws InterruptedException {
        wemefBatch();
        sleep(1000 * 60);
    }

    @Scheduled(initialDelay = 1000*60*60*4, fixedDelay = 1000*60*60*4)
    public void twoHourBatch(){
        telegramAPI.send("running batch server");
    }

    @Scheduled(cron = "0 0 0 * * 0")// 초 분 시 일 월 요일
    public void wemefOAlertCron(){
        for(int i=0;i<8;i++)telegramAPI.send("wemefOchange!!");
    }

    public void wemefBatch() {
        try {
            appDataObj.setWemefDataList(wemefoRequest.getWemefOData());
        }catch (Exception e){
            telegramAPI.send("wemef error!!!\n"+e.getMessage());
        }

    }

    public void baeminYogiyoCoupangBatch(){
        try{
            appDataObj.setYogiyoDataList(yogiyoRequest.getAppData());
        }catch (Exception e){
            telegramAPI.send("yogiyo error!!!\n"+e);
        }
        try {
            appDataObj.setBaeminDataList(baeminRequest.getAppData());
        }catch (Exception e){
            telegramAPI.send("baemin error!!!\n"+e);
        }
        appDataObj.setCoupangDataList(coupangRequest.getAppData());
    }
}
