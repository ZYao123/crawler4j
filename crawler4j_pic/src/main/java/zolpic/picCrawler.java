package zolpic;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class picCrawler extends WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(picCrawler.class);
    static String path = "D:/pic/bing/";//文件保存目录，结尾一定要加 /

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
        super.onStart();
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
        return !FILTERS.matcher(href).matches() // 正则匹配，过滤掉我们不需要的后缀文件
                && href.startsWith("https://bing.ioliu.cn");// 只接受以“http://desk.zol.com.cn”开头的url
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
            Elements elements = doc.select("img");
            if (elements.size() == 0) {
                return;
            }
            for (Element element : elements) {
                Elements str = element.select(".progressive__img");
                String src = str.attr("src");
                System.out.println(src);
//                src = src.replaceFirst("s960x600", "s1920x1080");//根据zol的url规则，选择下载壁纸的分辨率
                if (!src.equals("")) {
                    logger.info(src);
                    downloadPicture(src);
                }
            }
        }
    }

    //下载图片
    public static void downloadPicture(String urlStr) {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestProperty("Host", "paper.cnstock.com");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            conn.setRequestProperty("Accept-Encoding", "utf8, deflate");//注意编码，gzip可能会乱码
            conn.setRequestProperty("Content-Encoding", "utf8");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Cookie", "__cfduid=d9837e63adcbbdcb7f4d55c844dd305d61563170940; _ga=GA1.2.1483881517.1563170941; _gid=GA1.2.788615789.1563170941; _gat_gtag_UA_61934506_5=1");
            conn.setRequestProperty("Cache-Control", "max-age=0");
//            conn.setRequestProperty("Content-Type", "application/pdf");

            InputStream inStream = conn.getInputStream();
            String u = conn.getURL().toString();
            String fileName = u.substring(u.lastIndexOf("/") + 1);
            FileOutputStream fs = new FileOutputStream(path + fileName);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}