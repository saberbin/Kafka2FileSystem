package com.saberbin.utils;

import java.io.*;
import java.util.HashMap;

/**
 * @projectName: Kafka2FileSystem
 * @package: utils
 * @className: Config
 * @author: NelsonWu
 * @description: 配置文件解析类
 * @date: 2024/1/10 16:36
 * @version: 1.0
 */
public class Config {
    String configPath;  //? 配置文件路径，最好是传入绝对路径
    public Config(String configPath){
        this.configPath = configPath;
    }

    /**
     * 读取配置文件并解析
     * 按照等号分割参数与值
     * 对于#开头的行以及#后面的内容都不处理，当成注释内容
     * @return 构建好的hashmap对象，key为配置参数名，value为配置参数值
     * @throws IOException
     * @throws FileNotFoundException
     */
    public HashMap<String, String> readData() throws IOException,FileNotFoundException {
        File file = new File(this.configPath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String s;
        HashMap<String, String> conf = new HashMap<>();
        while ((s = bufferedReader.readLine()) != null) {
            String str_value;
            if (s.startsWith("#")) {
                // 跳过注释的行
                continue;
            } else if (s.contains("#")) {
                // 去掉#后面的注释部分以及空格
                str_value = s.split("#")[0].replace(" ", "");
            } else {
                // 去掉空格
                str_value = s.replace(" ", "");
            }
            String[] splitdata = str_value.split("=");
            conf.put(splitdata[0], splitdata[1]);
        }
        return conf;
    }

    /**
     * 解析配置文件并构建配置文件对象到类属性
     * @return hashmap object
     * @throws IOException
     * @throws FileNotFoundException
     */
    public HashMap<String, String> makeData() throws IOException,FileNotFoundException {
        return readData();
    }

    public static void main(String[] args) throws IOException,FileNotFoundException {

        Config config = new Config("D:\\project\\kafka2hdfs\\config\\defaultConf_json.conf");
        HashMap<String, String> stringStringHashMap = config.readData();
        System.out.println(stringStringHashMap);
    }

}
