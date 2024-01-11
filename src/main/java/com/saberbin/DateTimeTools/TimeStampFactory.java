package com.saberbin.DateTimeTools;

/**
 * @projectName: Kafka2FileSystem
 * @package: DateTimeTools
 * @className: TimeStampFactory
 * @author: NelsonWu
 * @description: 日期时间戳解析类
 * @date: 2024/1/10 16:37
 * @version: 1.0
 */
public class TimeStampFactory extends DateTimeToolFactory{
    /**
     * 日期时间戳数据解析工厂类的抽象方法，返回日期时间解析对象
     * @return
     */
    @Override
    public parseDatetimeAbs create() {
        return new TimeStampParser();
    }
}
