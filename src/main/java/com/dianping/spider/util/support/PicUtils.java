package com.dianping.spider.util.support;

import com.dianping.combiz.spring.util.LionConfigUtils;
import com.dianping.piccentercloud.storage.api.HttpUploadAPI;
import com.dianping.piccentercloud.storage.api.Token;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 14-1-20
 * Time: 下午7:51
 * To change this template use File | Settings | File Templates.
 */
public class PicUtils {

    private final static String CLASSPATH = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    public static String uploadPic(String fileName) {
        HttpUploadAPI uploadApi = new HttpUploadAPI("test", "test");
        uploadApi.setRequestURL(LionConfigUtils.getProperty("piccenter-storage.server.url.www") + "/upload/cloud/api/test");
        uploadApi.setConnectTimeout(10000); //http连接超时时间
        uploadApi.setReadTimeout(10000);      //http读取的超时时间

        Token token = new Token();
        token.setBiz("test");
        token.setAccount("test");
        token.setExpiredTime(System.currentTimeMillis()/1000+30*60);

        Map<String,String> header = new HashMap<String, String>();
        header.put("ClientIPByProxy", "127.0.0.1");

        Map<String,String> map;
        try {
            map = uploadApi.execute(token, new File("/Users/liming_liu/Downloads/bynam.jpg"), "baby",header);
        } catch (IOException e) {
            return null;
        }

        if ("200".equals(map.get("code"))) {
            return map.get("url");
        }
        return null;
    }

    public static boolean download(String urlString, String filePath) {

        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(filePath);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean delete(String filePath) {
        try {
            File picFile = new File(filePath);
            picFile.delete();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }


    public static String getTempFilePath(String urlString){
        String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
        String tempPath = CLASSPATH+"tempPic/";
        File tempFolder = new File(tempPath);
        if(!tempFolder.exists()){
            tempFolder.mkdir();
        }
        String filePath = tempPath+fileName;
        return filePath;
    }


}
