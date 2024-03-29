package com.interboard.wikiblog.job;// package com.jiawa.wiki.job;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import java.text.SimpleDateFormat;
// import java.util.Date;
//
// @Component
// public class TestJob {
//
//    private static final Logger LOG = LoggerFactory.getLogger(TestJob.class);
//

//    @Scheduled(fixedRate = 1000)
//    public void simple() throws InterruptedException {
//        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
//        String dateString = formatter.format(new Date());
//        Thread.sleep(2000);
//        LOG.info("每隔五秒鐘執行一次： {}", dateString);
//    }
//

//    @Scheduled(cron = "*/1 * * * * ?")
//    public void cron() throws InterruptedException {
//        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss SSS");
//        String dateString = formatter.format(new Date());
//        Thread.sleep(1500);
//        LOG.info("每隔一秒鐘執行一次： {}", dateString);
//    }
//
// }
