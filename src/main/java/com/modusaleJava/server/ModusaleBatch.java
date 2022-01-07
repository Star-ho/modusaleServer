package com.modusaleJava.server;

import com.modusaleJava.server.baemin.BaeminRequest;
import com.modusaleJava.server.coupang.CoupangRequest;
import com.modusaleJava.server.service.ImageService;
import com.modusaleJava.server.service.MainService;
import com.modusaleJava.server.service.TodayService;
import com.modusaleJava.server.utils.GitHubData;
import com.modusaleJava.server.utils.TelegramAPI;
import com.modusaleJava.server.wemefo.WemefoRequest;
import com.modusaleJava.server.yogiyo.YogiyoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.*;

import static java.lang.Thread.sleep;

@Component
public class ModusaleBatch {

    private List<ModusaleAppData> baeminDataList =new ArrayList<>();
    private List<ModusaleAppData> yogiyoDataList =new ArrayList<>();
    private List<ModusaleAppData> wemefDataList =new ArrayList<>();
    private List<ModusaleAppData> coupangDataList =new ArrayList<>();
    private BaeminRequest baeminRequest;
    private YogiyoRequest yogiyoRequest;
    private CoupangRequest coupangRequest;
    private WemefoRequest wemefoRequest;
    private TelegramAPI telegramAPI;
    private GitHubData gitHubData;
    private ImageService imageService;
    private MainService mainService;
    private TodayService todayService;

    @Autowired
    private void setTodayService(TodayService todayService){this.todayService=todayService;}

    @Autowired
    private void setImageService(ImageService imageService){
        this.imageService=imageService;
    }

    @Autowired
    private void setMainService(MainService mainService){
        this.mainService=mainService;
    }

    @Autowired
    public void setGitHubData(GitHubData gitHubData) {
        this.gitHubData = gitHubData;
    }
    @Autowired
    public void setTelegramAPI(TelegramAPI telegramAPI) {
        this.telegramAPI = telegramAPI;
    }
    @Autowired
    public void setBaeminRequest(BaeminRequest baeminRequest) {
        this.baeminRequest = baeminRequest;
    }
    @Autowired
    public void setYogiyoRequest(YogiyoRequest yogiyoRequest) {
        this.yogiyoRequest = yogiyoRequest;
    }
    @Autowired
    public void setCoupangRequest(CoupangRequest coupangRequest) {
        this.coupangRequest = coupangRequest;
    }
    @Autowired
    public void setWemefoRequest(WemefoRequest wemefoRequest) {
        this.wemefoRequest = wemefoRequest;
    }

    public void consume(){
        telegramAPI.send("refresh!!");
        wemefBatch();
        baeminYogiyoCoupangBatch(true);
    }


    @Scheduled(fixedDelay = 1000*60*5)
    public void fiveMinBatch(){
        baeminYogiyoCoupangBatch(false);
    }


    @Scheduled(cron = "* 2,32 * * * *")// 매 정각, 30분마다
    public void thirtyminBatch() throws InterruptedException {

            wemefBatch();
            sleep(1000 * 60 * 1);

}

    @Scheduled(fixedDelay = 1000*60*60*2)
    public void twoHourBatch(){
        telegramAPI.send("running batch server");
    }

    @Scheduled(cron = "0 0 0 * * 0")// 초 분 시 일 월 요일
    public void wemefOAlertCron(){
        for(int i=0;i<8;i++)telegramAPI.send("wemefOchange!!");
    }

    public void wemefBatch() {
        try {
            List<ModusaleAppData> wemefDataList = wemefoRequest.getWemefOData();
        }catch (Exception e){
            telegramAPI.send("wemef error!!!\n"+e.getMessage());
        }
//        System.out.println(new Date()+"wemef!");
        if (wemefDataList.toString().equals(this.wemefDataList.toString()) == false) {
            this.wemefDataList = wemefDataList;
            mergeAndSend();
        }
    }

    public void baeminYogiyoCoupangBatch(boolean flag){
        List<ModusaleAppData> coupangDataList= new ArrayList<>();
        List<ModusaleAppData> yogiyoDataList =new ArrayList<>();
        List<ModusaleAppData> baeminDataList =new ArrayList<>();

        try{
            yogiyoDataList = yogiyoRequest.getAppData();
        }catch (Exception e){
            telegramAPI.send("yogiyo error!!!\n"+e.getMessage());
        }
        try {
            baeminDataList = baeminRequest.getAppData();
        }catch (Exception e){
            telegramAPI.send("baemin error!!!\n"+e.getMessage());
        }
        try{
            coupangDataList = coupangRequest.getAppData();
        }catch (Exception e){
            telegramAPI.send("coupang error!!!\n"+e.getMessage());
        }

        if(baeminDataList.toString().equals(this.baeminDataList.toString())==false){
            this.baeminDataList =baeminDataList;
            flag=true;
        }
        if(yogiyoDataList.toString().equals(this.yogiyoDataList.toString())==false){
            this.yogiyoDataList =yogiyoDataList;
            flag=true;
        }
        if(coupangDataList.toString().equals(this.coupangDataList.toString())==false){
            this.coupangDataList = coupangDataList;
            flag=true;
//            System.out.println(coupangDataList);
        }
        if(flag){
            mergeAndSend();
        }
    }

    private void mergeAndSend(){
        List<ModusaleAppData> modusaleDataList =new ArrayList<>();
        modusaleDataList.addAll(this.baeminDataList);
        modusaleDataList.addAll(this.yogiyoDataList);
        modusaleDataList.addAll(this.coupangDataList);
        modusaleDataList.addAll(this.wemefDataList);
        Map<String,String > unifiedName =gitHubData.getUnifiedNameMap();
        Map<String,List<String>> cateMappingName=gitHubData.getCategoryMap();
        Map<String,List<String>> retList=new HashMap<>();
        for(int i = 0; i< modusaleDataList.size(); i++){
            ModusaleAppData data= modusaleDataList.get(i);
            if(unifiedName.get(data.getBrandName())!=null){
                data.setBrandName(unifiedName.get(data.getBrandName()));
            }
            if(cateMappingName.get(data.getBrandName())!=null) {
                List<String> cate = cateMappingName.get(data.getBrandName());
                retList.put("" + i, Arrays.asList(data.getBrandName(), data.getAppName(), cate.get(1), cate.get(2), data.getPrice(), data.getBrandScheme()));
            }else {
                retList.put("" + i, Arrays.asList(data.getBrandName(), data.getAppName(), "undefiend.png", "없음", data.getPrice(), data.getBrandScheme()));
            }
        }
        makeImageListAndTodaySale(retList);
        mainService.consume(retList);
    }

    private void makeImageListAndTodaySale(Map<String, List<String>> retList){
        LinkedHashMap<String,String> imgMap= coupangRequest.getCoupangBannerList();
        imgMap.putAll(wemefoRequest.getWemefBannerList());
        imageService.consume(imgMap);
        todayService.consume(retList);
    }
}
