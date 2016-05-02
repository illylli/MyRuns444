package com.example.yuzhong.myruns4;

/**
 * Created by Shicheng on 2016/4/23.
 */
public class DeleteFromDatabase extends Thread {

    private long pos;
    HistoryDataSource historydata;

    public DeleteFromDatabase(){ }

    public DeleteFromDatabase(long pos, HistoryDataSource historydata){
        this.pos = pos;
        this.historydata = historydata;
    }

    @Override
    public void run() {
        super.run();
        historydata.open();
        historydata.deleteHistory(pos);
        historydata.close();
    }
}
