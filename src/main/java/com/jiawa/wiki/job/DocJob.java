package com.jiawa.wiki.job;

import com.jiawa.wiki.service.DocService;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 每30秒更新數據
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {

        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("每30秒更新數據start");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("每30秒更新數據end，耗时：{}毫秒", System.currentTimeMillis() - start);
    }

}
