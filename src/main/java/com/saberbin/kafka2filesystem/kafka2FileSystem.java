package com.saberbin.kafka2filesystem;


import com.saberbin.DateTimeTools.DatetimeTool;
import com.saberbin.utils.Config;
import com.saberbin.utils.FileTools;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import static com.saberbin.DateTimeTools.DatetimeTool.getCurrentDateTime;
import static com.saberbin.utils.RandomStrGenerator.generatorRandomStr;

/**
 * @projectName: kafka2FileSystem
 * @package: com.saberbin.kafka2FileSystem
 * @className: kafka2FileSystem
 * @author: NelsonWu
 * @description: flink application通过Kafka source读取数据并写入file system
 * @date: 2024/1/11 15:35
 * @version: 1.0
 */
public class kafka2FileSystem implements Serializable {
    public HashMap<String, String> config;

    public kafka2FileSystem(){}

    public void setConfigValue(String key, String value){
        this.config.put(key, value);
    }

    public void setConfig(String configPath) throws Exception {
        this.config = getConfig(configPath);
    }
    /**
     * 加载配置信息
     * @param configPath 配置文件路径
     * @return 配置文件信息，hashmap对象
     * @throws Exception
     */
    public static HashMap<String, String> getConfig(String configPath) throws Exception{
        Config config = new Config(configPath);
        return config.makeData();
    }

    /**
     * 获取配置参数值，如果该参数不存在则返回null
     * @param key 配置参数，即key
     * @return 配置参数值，或者null
     */
    public String getConfigValue(String key){
        return this.config.getOrDefault(key, null);
    }

    /**
     * 获取配置参数值，如果该参数不存在则返回默认值
     * @param key 参数，key
     * @param defaultValue 配置参数默认值
     * @return 参数值
     */
    public String getConfigValueOrDefault(String key, String defaultValue){
        return this.config.getOrDefault(key, defaultValue);
    }

    public HashMap<String, String> getConfigObj(){
        return this.config;
    }

    /**
     * 自定义bucketId构建方法，根据不同数据类型返回对应的bucketid字符串
     * @param value stream中的数据
     * @return
     */
    public String getCustomBucketId(String value) throws Exception {
        String dtime;
        String dataType = getConfigValue("datatype");
        String dtField = getConfigValue("datetimefield");
        int dtIndex = Integer.parseInt(getConfigValue("datetimefieldindex"));
        String dataPatter = getConfigValue("datapatter");
        if(dataType.equalsIgnoreCase("JSON")){
            JSONObject jsonObject = JSON.parseObject(value);
            dtime = jsonObject.getString(dtField);
        } else if (dataType.equalsIgnoreCase("TEXT")) {
            String[] valueList = value.split(dataPatter);
            dtime = valueList[dtIndex];
        }else{
            return null;
        }
        // 解析日期时间数据
        String dtFormat = getConfigValue("datetimeformat");
        DatetimeTool dtTool = new DatetimeTool(dtime, dtFormat);
        return dtTool.getDate("yyyyMMdd");
    }

    /**
     * 如果配置文件中存在该配置则使用配置的参数值，否则设置为1
     * @return 并行度
     */
    public int getParallelism(){
        //? 设置程序并行度
        return Integer.parseInt(
                getConfigValueOrDefault("parallelism", "1")
        );
    }

    public static void main(String[] args) throws Exception{
        //? 加载配置文件路径并解析
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String configPath = parameterTool.get("config");

        kafka2FileSystem kafka2FileSys = new kafka2FileSystem();
        kafka2FileSys.setConfig(configPath);

        //? 列出配置信息
        String bootstrapServer = kafka2FileSys.getConfigValue("bootstrapservers");
        String groupId = kafka2FileSys.getConfigValue("groupid");
        String topic = kafka2FileSys.getConfigValue("topic");
        //? 如果不配置appname，则创建带有flinkApp前缀包含当前日期时间的时间戳（秒级）字符作为appname
        String defaultAppName = "flinkApp_" + getCurrentDateTime();
        // String defaultAppName = "flinkApp_" + getCurrentDateTime() + "_" + generatorRandomStr(5);
        String appName = kafka2FileSys.getConfigValueOrDefault("appname", defaultAppName);
        //? 数据存放目录
        String file_path = kafka2FileSys.getConfigValue("file_path");
        String checkPoint = kafka2FileSys.getConfigValue("checkpoint");

        //? 数据库表信息
        String tb_name = kafka2FileSys.getConfigValue("table_name");

        //? 目录存放路径，包括数据以及checkpoint
        FileTools fileTool = new FileTools(checkPoint, file_path);
        kafka2FileSys.setConfigValue("outputFilePath", fileTool.getFilePath());

        //? checkpoint配置信息
        String checkpointPath = fileTool.getCheckpointPath();
        int checkpoint_interval = Integer.parseInt(
                kafka2FileSys.getConfigValueOrDefault("checkpoint_interval", "5000")
        );

        //? 设置全局变量
        ParameterTool GlobalParam = ParameterTool.fromMap(kafka2FileSys.getConfigObj());
        //? flink application环境配置
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.enableCheckpointing(checkpoint_interval, CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointStorage(checkpointPath);
        env.getConfig().setGlobalJobParameters(GlobalParam);

        //! 并行度配置，不建议在此处配置，默认配置为1，不传参的情况下按照系统线程数设置（flink默认做法）
        if (kafka2FileSys.getParallelism() == 1){
            env.setParallelism(1);  //! 不建议在此处设置并行度
            // 默认设置为1，因为当前Kafka的topic为1.可以根据Kafka的topic分区数量来调整flink程序的并行度
        }

        //? 配置Kafka参数构建source
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers(bootstrapServer)
                .setGroupId(groupId)
                .setTopics(topic)
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();
        DataStreamSource<String> kafkaDS = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource");

        //? 输出到文件系统
        Path outputFilePath = new Path(GlobalParam.get("outputFilePath"));
        FileSink<String> fileSink = FileSink.<String>forRowFormat(
                        outputFilePath,
                        new SimpleStringEncoder<>("UTF-8")
                ).withOutputFileConfig(OutputFileConfig.builder()
                        .withPartPrefix(tb_name)  // ? 文件前缀，按照表名作为前缀
                        .withPartSuffix(".log")  //? 文件后缀
                        .build())
                //? 指定分桶策略，按照数据时间分桶 -> 影响目录
                .withBucketAssigner(new BucketAssigner<String, String>() {
                    @Override
                    public String getBucketId(String s, Context context) {
                        String bucketId;  //? 定义bucketId变量
                        String customBucketId = null;
                        try {
                            customBucketId = kafka2FileSys.getCustomBucketId(s);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        //? 构造bucketId并返回
                        bucketId = "dtime=" + customBucketId;
                        return bucketId;
                    }

                    @Override
                    public SimpleVersionedSerializer<String> getSerializer() {
                        return SimpleVersionedStringSerializer.INSTANCE;
                    }
                })
                //? 文件滚动策略，满足策略后该文件关闭不再写入，新建文件写入。策略只要满足其中一个：时间或者文件大小
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(Duration.ofSeconds(10 * 60))  //?  10分钟刷新一个文件到文件系统中
                                .withMaxPartSize(new MemorySize((1024 * 1024 * 256)))  //? 每个文件存放256MB
                                .build()
                ).build()
                ;
        kafkaDS.sinkTo(fileSink);

        env.execute(appName);
    }
}
