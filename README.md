# Kafka2HDFS
使用flink同步Kafka的数据到HDFS目录

## 配置

```text
# 使用#进行注释，#后面的内容都不会进行解析
appname=flink_app_order_refund_info
database=gmall
table_name=user_info  # 数据库表名
topic=test  # Kafka的topic名称
groupid=kafka2hdfs4
bootstrapservers=172.21.40.103:9092
file_path=hdfs://localhost:9000/warehouse/log/gmall/user_info
checkpoint=hdfs://localhost:9000/warehouse/checkpoint/order_refund_info
datatype=json  # json或者text，text类型为按某分隔符分隔的字符串行数据
datapatter=,  # json类不需要此配置，text类则配置为对应的分隔符
datetimefield=update_time  # 日期时间字段名，用于保存的hdfs分区
datetimeformat=yyyy-MM-dd  # 日期时间的格式化字符串，用于格式化日期时间字段
datetimefieldindex=0   # 日期时间字段的索引值，从0开始计数
checkpoint_interval=8000  # checkpoints序列化间隔时长，非必要，默认为5000
parallelism=1  # 并行度设置，不建议配置此项目（测试配置），配置为1才会生效，设置为其他值则会按照服务器的最大线程数设置并行度，设置为非数字类型可能会抛出异常
```
- `appname`是提交的application名称，方便进行管理。
- `database`以及`table_name`是库表相关名称，之前配置中是为了拼接文件存放目录，目前没有使用到。
- `topic`为Kafka的topic
- `groupid`为Kafka消费组id
- `bootstrapservers`为Kafka集群地址，多个IP端口使用英文逗号隔开
- `file_path`为文件路径，存放最终输出的文件路径，可以是远程或者本地目录
- `checkpoint`为checkpoint存放目录
- `datatype`为数据类型，json or text
- `datapatter`为数据分割符，text的情况下生效，如果是|应该为\\|，否则可能会导致程序异常
- `datetimefield`为日期时间字段名，json数据的情况下才会加载该配置，获取日期时间数据
- `datetimeformat`为日期时间格式化字符串，以数据的时间为准
- `datetimefieldindex`为text数据的日期时间的索引，从0开始
- `checkpoint_interval`为checkpoint配置，checkpoint保存的时间间隔。
- `parallelism`为flink程序的全局并行度，不建议采用此配置，不配置此项的情况下会采用系统的线程数作为并行度。

## 运行
```shell
java com.saberbin.kafka2filesystem.kafka2FileSystem.java --config defaultConfig_json.conf

```



