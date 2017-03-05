package net.singlex.simple;

import net.singlex.base.BasicHttpClient;
import org.asynchttpclient.cookie.Cookie;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by singlex on 17-3-5.
 */
@Component
public class SingleXProcessor extends BasicHttpClient implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    private String url = "";
    private String username = "";
    private String password = "";


    @PostConstruct
    void init() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("explorer singlex.net init ...");
        url = config.getString("site.singlex.url");
        username = config.getString("site.singlex.username");
        password = config.getString("site.singlex.password");

        doLogin(url, username, password);
        getWithCookie("http://www.singlex.net/wp-admin/");

    }

    private String getCookie() {
        String cookieStr = null;
        for (Cookie cookie : cookies) {
            cookieStr += (cookie.getName() + "=" + cookie.getValue() + ";");
        }
        return cookieStr;
    }

    @Override
    public void process(Page page) {
        System.out.println("get page ...");
        System.out.println(page.getHtml().xpath("//div[@id='postbox-container-1']/div/div/div/div/ul/li/a/text()").toString());
//        page.addTargetRequests(page.getHtml().links().regex("(http://www.singlex\\.net/\\d+.html)").all());
//        page.putField("author", page.getUrl().regex("https://singlex\\.net/.*").toString());
//        page.putField("name", page.getHtml().xpath("//p[@class='comment-meta']/a/text()").toString());
//        if (page.getResultItems().get("name") == null) {
            //skip this page
//            page.setSkip(true);
//        }
//        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
    }

    @Override
    public Site getSite() {
        site.addHeader("Cookie", getCookie());
        return site;
    }
}
