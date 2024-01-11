package com.saberbin.DateTimeTools;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @projectName: Kafka2FileSystem
 * @package: DateTimeTools
 * @className: TimeStampParser
 * @author: NelsonWu
 * @description: 日期时间戳数据解析接口
 * @date: 2024/1/10 15:20
 * @version: 1.0
 */
public class TimeStampParser implements parseDatetimeAbs<Date>{
    /**
     * 解析日期时间字符串，返回标准格式的数据
     *
     * @param datetime 日期时间字符串，格式类似：1970-09-01T14:29:39.030Z
     * @param format   日期时间字符串格式化模式
     * @return 标准化后的数据
     */
    @Override
    public Date parseDateTime(String datetime, String format) {
        ZonedDateTime DateTime;
        DateTime = ZonedDateTime.parse((CharSequence) datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        Date date = Date.from(DateTime.toInstant());  // ZonedDateTime转换为Date对象，统一输出字段
        return date;
    }
}
