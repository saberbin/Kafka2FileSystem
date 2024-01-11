package com.saberbin.DateTimeTools;

/**
 * @projectName: Kafka2FileSystem
 * @package: DateTimeTools
 * @className: DateTimeToolFactory
 * @author: NelsonWu
 * @description: 时间日期数据解析抽象类
 * @date: 2024/1/10 15:26
 * @version: 1.0
 */
public abstract class DateTimeToolFactory {

    public abstract parseDatetimeAbs create();

    static DateTimeToolFactory create(Class classObj){
        DateTimeToolFactory factory = null;
        try{
            factory = (DateTimeToolFactory) classObj.newInstance();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return factory;
    }
}
