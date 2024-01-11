package com.saberbin.DateTimeTools;

public interface parseDatetimeAbs<T> {

    /**
     * 解析日期时间字符串，返回标准格式的数据
     * @param datetime 日期时间字符串
     * @param format 日期时间字符串格式化模式
     * @return 标准化后的数据
     */
    T parseDateTime(String datetime, String format);
}
