package com.interboard.wikiblog.mapper;

import com.interboard.wikiblog.resp.StatisticResp;

import java.util.List;

public interface EbookSnapshotMapperCust {

    public void genSnapshot();
    public void updateSnapshot1();
    public void updateSnapshot2();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}
