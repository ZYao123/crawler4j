package ip;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class ip {

    private static final Logger logger = LoggerFactory.getLogger(ip.class);

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "D:/data";// crawler4j文件存储位置

        int numberOfCrawlers = 1;// 线程数量
        //浏览器头文件
        CrawlConfig config = new CrawlConfig();
        HashSet<BasicHeader> collections = new HashSet<BasicHeader>();
//        collections.add(new BasicHeader("User-Agent",
//                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36"));
//        collections.add(new BasicHeader("Accept",
//                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
//        collections.add(new BasicHeader("Accept-Encoding", "gzip, deflate, br"));
//        collections.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
//        collections.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
//        collections.add(new BasicHeader("Connection", "keep-alive"));
//        collections.add(new BasicHeader("Cookie","_free_proxy_session=BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJTJhYWZiNmNkZTQ2ODc1YWI4YTU3YjZmOGQzMWNlNzVhBjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMWFjdzhEeSt4dWEyaXRHT1IxRENMRXZIYzAxaDZseGNMS0pPVmswalBVY2s9BjsARg%3D%3D--2f054c5b74189c2cf6402b07c4685f2748778e3e; Hm_lvt_0cf76c77469e965d2957f0553e6ecf59=1537606818; Hm_lpvt_0cf76c77469e965d2957f0553e6ecf59=1537623077"));
        collections.add(new BasicHeader("Host: www.xicidaili.com");
        collections.add(new BasicHeader("Connection: keep-alive");
        collections.add(new BasicHeader("Cache-Control: max-age=0");
        collections.add(new BasicHeader("Upgrade-Insecure-Requests: 1");
        collections.add(new BasicHeader("DNT: 1");
        collections.add(new BasicHeader("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
        collections.add(new BasicHeader("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        collections.add(new BasicHeader("Referer: http://www.xicidaili.com/");
        collections.add(new BasicHeader("Accept-Encoding: gzip, deflate");
        collections.add(new BasicHeader("Accept-Language: en-XA,en-US;q=0.9,en;q=0.8,zh-CN;q=0.7,zh;q=0.6");
        collections.add(new BasicHeader("Cookie: _free_proxy_session=BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJTJhYWZiNmNkZTQ2ODc1YWI4YTU3YjZmOGQzMWNlNzVhBjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMWFjdzhEeSt4dWEyaXRHT1IxRENMRXZIYzAxaDZseGNMS0pPVmswalBVY2s9BjsARg%3D%3D--2f054c5b74189c2cf6402b07c4685f2748778e3e; Hm_lvt_0cf76c77469e965d2957f0553e6ecf59=1537606818; Hm_lpvt_0cf76c77469e965d2957f0553e6ecf59=1537623854");
        collections.add(new BasicHeader("If-None-Match: W/"4b590f7db2575334ed802d21d2b03f7c/"");
        collections.add(new BasicHeader("");
        collections.add(new BasicHeader(
        collections.add(new BasicHeader(


        config.setDefaultHeaders(collections);









        config.setMaxPagesToFetch(-1);//最大爬取页面数量
        config.setPolitenessDelay(200);//爬取延迟，默认200
        config.setCrawlStorageFolder(crawlStorageFolder);// 配置信息设置
        config.setMaxDepthOfCrawling(-1);// 最大爬取深度
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);// 创建爬虫执行器

        controller.addSeed("http://www.xicidaili.com/wn/");// 传入种子 要爬取的网址,可添加多个

        controller.start(ipCrawler.class, numberOfCrawlers);// 开始运行
    }
}