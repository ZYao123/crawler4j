package pic_zol;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class picCrawler extends WebCrawler {
    /**
     * ������ʽƥ��ָ���ĺ�׺�ļ�
     */
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif" + "|mp3|mp4|zip|gz))$");

    /**
     * ���������Ҫ�Ǿ�����Щurl������Ҫץȡ������true��ʾ��������Ҫ�ģ�����false��ʾ����������Ҫ��Url
     * ��һ������referringPage��װ�˵�ǰ��ȡ��ҳ����Ϣ �ڶ�������url��װ�˵�ǰ��ȡ��ҳ��url��Ϣ
     * ����������У�����ָ��������Ծ���css��js��git��...��չ����url��ֻ�����ԡ�http://www.ics.uci.edu/����ͷ��url��
     * ����������£����ǲ���ҪreferringPage����������������
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();// �õ�Сд��url
        return !FILTERS.matcher(href).matches() // ����ƥ�䣬���˵����ǲ���Ҫ�ĺ�׺�ļ�
                && href.startsWith("http://desk.zol.com.cn");// ֻ�����ԡ�http://www.ics.uci.edu/����ͷ��url
    }

    /**
     * ��һ��ҳ�汻��ȡ��׼���ñ���ĳ�����ʱ��������������á�
     */
    @Override
    public void visit(Page page) {
        // �ж�page�Ƿ�Ϊ��������ҳ
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();// ҳ��html����
            Document doc = Jsoup.parse(html);// ����jsoup����html�������Ҳ�����Լ���һ��

            // ʹ��ѡ������ʱ����Ҫ�˽���ҳ�е�html�����Լ�ȥ��ҳ��F12һ�£�
            Elements elements = doc.select("img");
            if (elements.size() == 0) {
                return;
            }
            for (Element element : elements) {
                Elements str = element.select("#bigImg");
                String src = str.attr("src");
                src = src.replaceFirst("s960x600", "s1920x1080");//����zol��url����ѡ�����ر�ֽ�ķֱ���
                if (src != null && !src.equals("")) {
                    System.out.println(src);
                    downloadPicture(src);
                }
            }
        }
    }

    //����ͼƬ
    private static void downloadPicture(String urlList) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            //��ȡurl��Ϊ�ļ���
            int end = urlList.lastIndexOf(".");
            int start = urlList.substring(0, end).lastIndexOf("/");
            String imageName = "D:/pic/test/" + urlList.substring(start + 1);//ͼƬ����Ŀ¼

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
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