package com.saberbin.DateTimeTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @projectName: Kafka2FileSystem
 * @package: DateTimeTools
 * @className: DatetimePaser
 * @author: NelsonWu
 * @description: 日期时间解析类
 * @date: 2024/1/10 15:16
 * @version: 1.0
 */
public class DatetimePaser implements parseDatetimeAbs<Date> {
    /**
     * 解析日期时间字符串，返回标准格式的数据
     *
     * @param datetime 日期时间字符串，格式类似：1970-01-01 13:39:29
     * @param format   日期时间字符串格式化模式
     * @return 标准化后的数据
     */
    @Override
    public Date parseDateTime(String datetime, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(datetime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}
