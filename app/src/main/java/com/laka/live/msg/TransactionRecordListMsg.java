package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.TransactionRecord;
import com.laka.live.util.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by luwies on 16/4/6.
 */
public class TransactionRecordListMsg extends ListMag<TransactionRecord> {

    @Expose
    @SerializedName(Common.DATA)
    private List<TransactionRecord> list;

    @Override
    public List<TransactionRecord> getList() {
        return list;
    }

    public void setList(List<TransactionRecord> list) {
        this.list = list;
    }

    @Override
    public void parase() {
        if (list != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar;
            for (TransactionRecord record : list) {
                String time = record.getTime();
                try {
                    Date date = format.parse(time);
                    calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    record.setYear(calendar.get(Calendar.YEAR));
                    record.setMonth(calendar.get(Calendar.MONTH) + 1);
                } catch (ParseException e) {

                }
            }
        }
    }
}
