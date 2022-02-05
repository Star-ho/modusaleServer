package com.modusale;

import com.modusale.web.service.ImageService;
import com.modusale.web.service.TodayService;
import com.modusale.yogiyo.YogiyoRequest;
import com.modusale.baemin.BaeminRequest;
import com.modusale.coupang.CoupangRequest;
import com.modusale.utils.AppDataObj;
import com.modusale.web.service.MainService;
import com.modusale.utils.GitHubData;
import com.modusale.utils.TelegramAPI;
import com.modusale.wemefo.WemefoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

@Component
public class ModusaleBatch {

    @Autowired
    private BaeminRequest baeminRequest;
    @Autowired
    private YogiyoRequest yogiyoRequest;
    @Autowired
    private CoupangRequest coupangRequest;
    @Autowired
    private WemefoRequest wemefoRequest;
    private TelegramAPI telegramAPI;
    private GitHubData gitHubData;
    private ImageService imageService;
    @Autowired
    private MainService mainService;
    private TodayService todayService;

    @Autowired
    private AppDataObj appDataObj;

    @Autowired
    private void setTodayService(TodayService todayService){this.todayService=todayService;}

    @Autowired
    private void setImageService(ImageService imageService){
        this.imageService=imageService;
    }

    @Autowired
    public void setGitHubData(GitHubData gitHubData) {
        this.gitHubData = gitHubData;
    }
    @Autowired
    public void setTelegramAPI(TelegramAPI telegramAPI) {
        this.telegramAPI = telegramAPI;
    }


    public void consume(){
        telegramAPI.send("refresh!!");
        gitHubData.setGithubData();
        wemefBatch();
        baeminYogiyoCoupangBatch();
    }


    @Scheduled(fixedDelay = 1000*60*5)
    public void fiveMinBatch(){
        baeminYogiyoCoupangBatch();
    }

    @Scheduled(fixedDelay = 1000*60*60*6)
    public void sixHourBatch(){
        gitHubData.setGithubData();
    }


    @Scheduled(cron = "* 2,32 * * * *")// 매 정각, 30분마다
    public void thirtyminBatch() throws InterruptedException {
        wemefBatch();
        sleep(1000 * 60);
    }

    @Scheduled(fixedDelay = 1000*60*60*4)
    @ConditionalOnProperty(prefix = "job.autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
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
        try{
            appDataObj.setCoupangDataList(coupangRequest.getAppData());
        }catch (Exception e){
            telegramAPI.send("coupang error!!!\n"+e);
            System.out.println(e);
        }
    }
}
