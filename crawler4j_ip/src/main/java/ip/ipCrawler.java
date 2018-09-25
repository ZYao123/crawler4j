package ip;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class ipCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(ipCrawler.class);
    PreparedStatement ps = null;
    databaseManager manager = null;
    private static int count = 0;
    /**
     * 正则表达式匹配指定的后缀文件
     */
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif" + "|mp3|mp4|zip|gz))$");

    /**
     * 线程启动时执行的函数
     * 每个线程启动时均执行一次
     */
    @Override
    public void onStart() {
        this.manager = databaseManager.getManager();
        try {
            this.ps = this.manager.getPs("INSERT INTO ipTable (ip, port, address, hide, type, time) VALUES (?, ?, ?, ?, ?, ?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 这个方法主要是决定哪些url我们需要抓取，返回true表示是我们需要的，返回false表示不是我们需要的Url
     * 第一个参数referringPage封装了当前爬取的页面信息 第二个参数url封装了当前爬取的页面url信息
     * 在这个例子中，我们指定爬虫忽略具有css，js，git，...扩展名的url，只接受以“http://desk.zol.com.cn”开头的url。
     * 在这种情况下，我们不需要referringPage参数来做出决定。
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();// 得到小写的url
        return !FILTERS.matcher(href).matches(); // 正则匹配，过滤掉我们不需要的后缀文件
//                && href.startsWith("https://m.weibo.cn");// 只接受以“http://desk.zol.com.cn”开头的url
    }

    /**
     * 当一个页面被提取并准备好被你的程序处理时，这个函数被调用。
     */
    @Override
    public void visit(Page page) {
        // 判断page是否为真正的网页
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();// 页面html内容
            Document doc = Jsoup.parse(html);// 采用jsoup解析html，这个大家不会可以简单搜一下

            // 使用选择器的时候需要了解网页中的html规则，自己去网页中F12一下，
            Elements tbody = doc.select("tbody");
            Elements trs = tbody.select("tr");
            for (Element element : trs) {
                System.out.println("------------------------------");
                Elements tds = element.select("td");
                String ip = tds.eq(1).text();
                if (ip.equals(""))
                    return;
                String port = tds.eq(2).text();
                String address = tds.eq(3).text();
                String hide = tds.eq(4).text().equals("高匿") ? "1" : "0";
                String type = tds.eq(5).text();
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = formatter.format(currentTime).toString();
                String[] str = {ip, port, address, hide, type, time};
                try {
                    manager.insert(this.ps, str);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                count++;//爬取计数
                System.out.println(count + ":" + ip);
            }
        }
    }
}