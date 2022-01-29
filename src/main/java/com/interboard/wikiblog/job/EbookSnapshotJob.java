package com.interboard.wikiblog.job;

import com.interboard.wikiblog.service.EbookSnapshotService;
import com.interboard.wikiblog.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @Resource
    private SnowFlake snowFlake;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void doSnapshot() {

        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("今日檔案快照");
        Long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("今日檔案快照结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }

}
