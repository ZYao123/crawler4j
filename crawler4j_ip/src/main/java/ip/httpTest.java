package ip;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class httpTest {
    private static final Logger logger = LoggerFactory.getLogger(httpTest.class);
    private static String ip = "http://qilinguizu.xyz:8080/getIp";

    public static void setIp(String ip) {
        httpTest.ip = ip;
    }

    public int test(HttpHost proxy) {
        RequestConfig requestConfig;
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(2000)//一、连接超时：connectionTimeout-->指的是连接一个url的连接等待时间
                .setSocketTimeout(2000)// 二、读取数据超时：SocketTimeout-->指的是连接上一个url，获取response的返回等待时间
                .setConnectionRequestTimeout(2000)
                .setProxy(proxy)
                .build();
//        RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
//        HttpClient client = new DefaultHttpClient();
        CloseableHttpClient client = HttpClients.createDefault();  //  创建httpClient实例
        HttpGet get = new HttpGet(ip);
        get.setConfig(requestConfig);
        get.setHeader("User-Agent", "Mozilla/5.0  (Windows  NT  6.1;  Win64;  x64;  rv:50.0)  Gecko/20100101  Firefox/50.0");
        long t1 = System.currentTimeMillis();
        HttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;//请求异常
        }
        long t2 = System.currentTimeMillis();
        if ((t2 - t1) > 2000) {
            return -2;//请求超时
        }
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity resEntity = response.getEntity();
            String returnIp;
            try {
                returnIp = EntityUtils.toString(resEntity, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
                return -3;//网页解析异常
            }
            if (proxy.getHostName().equals(returnIp)) {
                return 0;//请求成功s
            } else {
                return -4;//返回Ip错误
            }
        } else {
            return -5;//请求失败
        }
    }
}
