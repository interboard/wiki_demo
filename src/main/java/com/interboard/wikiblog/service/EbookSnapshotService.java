package com.interboard.wikiblog.service;

import com.interboard.wikiblog.mapper.EbookSnapshotMapperCust;
import com.interboard.wikiblog.resp.StatisticResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot() {
        ebookSnapshotMapperCust.genSnapshot();
        ebookSnapshotMapperCust.updateSnapshot1();
        ebookSnapshotMapperCust.updateSnapshot2();
    }

    /**
     * 獲取各項數據
     */
    public List<StatisticResp> getStatistic() {
        return ebookSnapshotMapperCust.getStatistic();
    }

    /**
     * 30天數值統計
     */
    public List<StatisticResp> get30Statistic() {
        return ebookSnapshotMapperCust.get30Statistic();
    }

}
