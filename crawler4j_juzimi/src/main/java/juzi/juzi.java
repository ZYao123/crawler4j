package juzi;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

public class juzi {

    private static final Logger logger = LoggerFactory.getLogger(juzi.class);

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "D:/data";// crawler4j文件存储位置

        int numberOfCrawlers = 5;// 线程数量
        //浏览器头文件
        CrawlConfig config = new CrawlConfig();
        HashSet<BasicHeader> collections = new HashSet<BasicHeader>();
        collections.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3192.0 Safari/537.36"));
        collections.add(new BasicHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));
        collections.add(new BasicHeader("Accept-Encoding", "gzip, deflate, br"));
        collections.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
        collections.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
        collections.add(new BasicHeader("Connection", "keep-alive"));
        collections.add(new BasicHeader("Cookie",""));
        config.setDefaultHeaders(collections);

        config.setMaxPagesToFetch(-1);//最大爬取页面数量
        config.setPolitenessDelay(200);//爬取延迟，默认200
        config.setCrawlStorageFolder(crawlStorageFolder);// 配置信息设置
        config.setMaxDepthOfCrawling(-1);// 最大爬取深度
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);// 创建爬虫执行器

        controller.addSeed("https://www.juzimi.com/");// 传入种子 要爬取的网址,可添加多个

        controller.start(juziCrawler.class, numberOfCrawlers);// 开始运行
    }
}