package com.dianping.spider.util.support;

import com.dianping.piccentercloud.storage.api.HttpUploadAPI;
import com.dianping.piccentercloud.storage.api.Token;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 14-1-20
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
public class PicUtilsTest {

    @Test
    public void test() throws IOException {
        HttpUploadAPI uploadApi = new HttpUploadAPI("test", "test");
        uploadApi.setRequestURL("http://192.168.214.231:8080/upload/cloud/api/test");
        uploadApi.setConnectTimeout(10000); //http连接超时时间
        uploadApi.setReadTimeout(10000);      //http读取的超时时间

        Token token = new Token();
        token.setBiz("test");
        token.setAccount("test");
        token.setExpiredTime(System.currentTimeMillis()/1000+30*60);
//        token.setOwnerName("仲良骏");
//        token.setCallBackUrl("");
//        token.setClientType(1);

        Map<String,String> header = new HashMap<String, String>();
        header.put("ClientIPByProxy", "127.0.0.1");

        Map<String,String> map = (Map<String,String>)uploadApi.execute(token, new File("/Users/liming_liu/Downloads/bynam.jpg"), "baby",header);

        //成功的返回字段：code+url+width+height
        System.out.println(map.get("code"));
        System.out.println(map.get("msg"));
        System.out.println(map.get("url"));
        System.out.println(map.get("width"));
        System.out.println(map.get("height"));

    }

    @Test
    public void formTest() {

    }
}
