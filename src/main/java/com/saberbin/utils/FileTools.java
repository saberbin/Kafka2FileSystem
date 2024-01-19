package com.saberbin.utils;

import java.io.File;
import java.util.Objects;

import static com.saberbin.utils.RandomStrGenerator.generatorRandomStr;

/**
 * @projectName: Kafka2FileSystem
 * @package: com.saberbin.utils
 * @className: FileTools
 * @author: NelsonWu
 * @description: 文件工具类，构建输出文件路径以及checkpoint目录
 * @date: 2024/1/11 15:35
 * @version: 1.0
 */
public class FileTools {
    String filePath;
    String checkpointPath;

    String WorkspacePath = System.getProperty("user.dir");  // 当前工作目录
    public FileTools(String checkpoint_path, String filePath){
        makeUpCheckpointPath(checkpoint_path);
        makeUpFilePath(filePath);
    }

    /**
     * 生成带tmp前缀的随机字符串
     * @return 带tmp前缀的随机字符串
     */
    private static String getTmpPath(){
        String tmpName = generatorRandomStr(8);
        return "tmp_" + tmpName;
    }

    /**
     * 合并路径，兼容系统的路径分隔符
     * @param path1 路径1
     * @param path2 路径2
     * @return 拼接后的路径
     */
    private static String combinePath(String path1, String path2){
        return path1 + File.separator + path2;
    }

    /**
     * 构造checkpoint路径
     * 如果传入的checkpointPath参数值为checkpoint或者null，则在当前工作目录新建checkpoint目录存放
     * 如果传入的checkpointPath为指定目录，则使用该值
     * @param checkpointPath
     */
    private void makeUpCheckpointPath(String checkpointPath){
        if (checkpointPath.equals("checkpoint")){
            String tmpPath = combinePath(this.WorkspacePath, checkpointPath);
            this.checkpointPath = combinePath(tmpPath, getTmpPath());
        } else if (checkpointPath == null) {
            String tmpPath = combinePath(this.WorkspacePath, "checkpoint");
            this.checkpointPath = combinePath(tmpPath, getTmpPath());
        } else {
            this.checkpointPath = checkpointPath;
        }
    }

    /**
     * 构建输出文件路径
     * 如果传入output字符串或者null，则以当前工作目录的output目录作为输出文件目录
     * 如果传入具体的路径，则以该路径作为输出路径
     * @param filePath 字符串类型，文件输出目录
     */
    private void makeUpFilePath(String filePath){
        if (filePath.equals("output")){
            String tmpPath = combinePath(this.WorkspacePath, filePath);
            this.filePath = combinePath(tmpPath, getTmpPath());
        } else if (filePath == null) {
            String tmpPath = combinePath(this.WorkspacePath, "output");
            this.filePath = combinePath(tmpPath, getTmpPath());
        } else {
            this.filePath = filePath;
        }
    }

    /**
     * 获取checkpoint目录
     * 如果创建FileTools对象的时候传入的hashmap对象没有对应的checkpoint的value，
     * 那么会默认为工作目录下的checkpoint目录下生成带tmp前缀的随机目录返回
     * 否则返回用户配置中的checkpoint目录
     * @return checkpoint目录
     */
    public String getCheckpointPath() {
        return this.checkpointPath;
    }

    /**
     * 获取文件存放目录
     * 如果创建FileTools对象的时候传入的hashmap对象没有对应的file_path的value，
     * 那么会在当前output目录下生成一个带有tmp前缀的随机字符串目录
     * 否则返回用户配置中的file_path目录
     * @return 文件存放目录
     */
    public String getFilePath(){
        return this.filePath;
    }

    /**
     * 创建目录
     * @param path 需要创建的目录路径
     */
    public void mkdirPath(String path){
        File dir = new File(path);
        if (!dir.exists()){
            boolean isCreated = dir.mkdirs();
        }
    }

}
