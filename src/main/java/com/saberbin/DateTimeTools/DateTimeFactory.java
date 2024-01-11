package com.saberbin.DateTimeTools;

/**
 * @projectName: kafka2hdfs
 * @package: DateTimeTools
 * @className: DateTimeFactory
 * @author: NelsonWu
 * @description: 日期时间解析工厂类
 * @date: 2024/1/10 16:36
 * @version: 1.0
 */
public class DateTimeFactory extends DateTimeToolFactory{

    /**
     * @return
     */
    @Override
    public parseDatetimeAbs create() {
        return new DatetimePaser();
    }
}
