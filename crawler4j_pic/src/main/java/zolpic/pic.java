package zolpic;

import java.io.File;
import java.util.HashSet;

import org.apache.http.message.BasicHeader;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class pic {

    private static final Logger logger = LoggerFactory.getLogger(pic.class);

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "D:/data";// crawler4j文件存储位置

        int numberOfCrawlers = 10;// 线程数量
        //浏览器头文件
        CrawlConfig config = new CrawlConfig();
        HashSet<BasicHeader> collections = new HashSet<BasicHeader>();
        collections.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36"));
        collections.add(new BasicHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"));
        collections.add(new BasicHeader("Accept-Encoding", "gzip,deflate,br"));
        collections.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
        collections.add(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
        collections.add(new BasicHeader("Connection", "keep-alive"));
        collections.add(new BasicHeader("Cookie", "__cfduid=d9837e63adcbbdcb7f4d55c844dd305d61563170940; _ga=GA1.2.1483881517.1563170941; _gid=GA1.2.788615789.1563170941"));
        config.setDefaultHeaders(collections);

        config.setMaxPagesToFetch(-1);//最大爬取页面数量
        config.setPolitenessDelay(200);//爬取延迟，默认200
        config.setCrawlStorageFolder(crawlStorageFolder);// 配置信息设置
        config.setMaxDepthOfCrawling(4);// 最大爬取深度
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);// 创建爬虫执行器

        controller.addSeed("https://bing.ioliu.cn");// 传入种子 要爬取的网址,可添加多个

        /*
          若目录不存在，创建目录
         */
        File file = new File(picCrawler.path);
        if (!file.mkdirs()) {
            logger.info(file.mkdir() ? "目录创建成功" : "目录创建失败");
        } else {
            controller.start(picCrawler.class, numberOfCrawlers);// 开始运行
        }
    }
}