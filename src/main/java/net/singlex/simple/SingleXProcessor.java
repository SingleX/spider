package net.singlex.simple;

import lombok.extern.slf4j.Slf4j;
import net.singlex.base.BasicHttpClient;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@ConditionalOnProperty(name = "site.singlex.enable", havingValue = "true")
@Slf4j
@Component
public class SingleXProcessor extends BasicHttpClient implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Value("${site.singlex.url}")
    private String url;

    @Value("${site.singlex.username}")
    private String username;

    @Value("${site.singlex.password}")
    private String password;

    @PostConstruct
    void init() throws InterruptedException, ExecutionException, TimeoutException {
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(Pair.of("log", username));
        params.add(Pair.of("pwd", password));
        System.out.println("SingleXProcessor init...");
        httpPostForLogin(url, params);
        httpGetWithCookie("http://www.singlex.net/wp-admin/");

        Spider.create(new SingleXProcessor()).addUrl("http://www.singlex.net/wp-admin/").thread(1).run();
    }

    @Override
    public void process(Page page) {
        System.out.println("get page ...");
        System.out.println(page.getHtml()
                               .xpath("//div[@id='postbox-container-1']/div/div/div/div/ul/li/a/text()")
                               .toString());
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

    private String getCookie() {
        StringBuffer buffer = new StringBuffer();
        cookies.forEach(cookie -> buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";"));
        return buffer.toString();
    }
}
