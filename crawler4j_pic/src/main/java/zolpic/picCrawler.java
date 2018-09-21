package zolpic;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    static String path = "D:/pic/test/";//文件保存目录，结尾一定要加 /

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
                && href.startsWith("http://desk.zol.com.cn");// 只接受以“http://desk.zol.com.cn”开头的url
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
                Elements str = element.select("#bigImg");
                String src = str.attr("src");
                src = src.replaceFirst("s960x600", "s1920x1080");//根据zol的url规则，选择下载壁纸的分辨率
                if (!src.equals("")) {
                    logger.info(src);
                    downloadPicture(src);
                }
            }
        }
    }

    //下载图片
    private static void downloadPicture(String url) {
        int imageNumber = 0;

        try {
            DataInputStream dataInputStream = new DataInputStream(new URL(url).openStream());
            //截取url作为文件名
            int end = url.lastIndexOf(".");
            int start = url.substring(0, end).lastIndexOf("/");
            String imageName = path + url.substring(start + 1);//图片储存目录+文件名

            File file = new File(imageName);
            if (file.exists()) {
                logger.warn("文件已存在@" + imageName);
                return;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}