package com.saberbin.DateTimeTools;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @projectName: Kafka2FileSystem
 * @package: DateTimeTools
 * @className: DatetimeTool
 * @author: NelsonWu
 * @description: 数据中的日期时间解析类，
 *      可以根据需求自定义，需要继承DateTimeToolFactory父类实现自己的parseDatetimeAbs.parseDateTime的接口，返回一个解析后的Date对象
 * @date: 2024/1/10 16:40
 * @version: 1.0
 */
public class DatetimeTool {
    private String dateString;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private Date date;

    private ZonedDateTime DateTime;

    public DatetimeTool(String datetimeString, String format){
        this.dateString = datetimeString;
        parseDatetimeAbs Parser;
        if ((format.toUpperCase().contains("T")) & (format.toUpperCase().contains("Z"))) {
            TimeStampFactory dateTimeFactory = (TimeStampFactory) DateTimeToolFactory.create(TimeStampFactory.class);
            Parser = dateTimeFactory.create();
            this.date = (Date) Parser.parseDateTime(this.dateString, format);
        } else {
            DateTimeFactory dateTimeFactory = (DateTimeFactory) DateTimeToolFactory.create(DateTimeFactory.class);
            Parser = dateTimeFactory.create();
            this.date = (Date) Parser.parseDateTime(this.dateString, format);
        }
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(this.date);
    }

    public String getDate(String pattern) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(this.date);
    }
    public String getDate() throws Exception {
        String pattern = "yyyy-MM-dd";
        return this.getDate(pattern);
    };

    public String getDateTime(String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(this.date);
    }

    public String getDateTime(){
        return this.getDateTime("yyyy-MM-dd HH:mm:ss");
    }

    public int getYear(){
        return this.calendar.get(Calendar.YEAR);
    }

    public int getMonth(){
        return this.calendar.get(Calendar.MONTH)+1;
    }
    public int getDay(){
        return this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour(){
        return this.calendar.get(Calendar.HOUR);
    }

    public int getMinute(){
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond(){
        return this.calendar.get(Calendar.SECOND);
    }

    public static void main(String[] args) throws Exception {
        DatetimeTool datetimeTool = new DatetimeTool("2023-11-21 07:05:05.11", "yyyy-MM-dd HH:mm:SS.sss");
        System.out.println(datetimeTool.getDate("yyyyMMddHHmmSSsss"));
        DatetimeTool datetimeTool2 = new DatetimeTool("2023-11-21T07:05:05Z", "yyyy-MM-ddTHH:mm:SSZ");
        System.out.println(datetimeTool2.getDate("yyyyMMdd"));
    }
}
