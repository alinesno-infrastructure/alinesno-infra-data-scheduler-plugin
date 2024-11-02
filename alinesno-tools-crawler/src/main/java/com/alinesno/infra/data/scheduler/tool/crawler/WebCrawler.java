package com.alinesno.infra.data.scheduler.tool.crawler;

import com.alinesno.infra.data.scheduler.plugin.core.constant.PluginConstants;
import com.alinesno.infra.data.scheduler.tool.bean.PageInfoBean;
import com.alinesno.infra.data.scheduler.tool.parse.PageParse;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.noear.solon.Solon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * WebCrawler类用于实现网页爬虫的功能
 * 该类通过Slf4j记录日志，并通过Lombok的@Data注解自动生成getter和setter方法
 */
@Slf4j
@Data
public class WebCrawler {

    private final Set<String> visitedLinks = new HashSet<>();
    private final int maxDepth;
    private String targetDomain;
    private String siteType ;

    private List<PageInfoBean> pageInfoList = new ArrayList<>();

    public WebCrawler(String startUrl, int maxDepth) {
        this.maxDepth = maxDepth;
        try {
            URL url = new URL(startUrl);
            this.targetDomain = url.getHost();
        } catch (IOException e) {
            log.error("无效的起始URL: " + startUrl);
        }
    }

    @SneakyThrows
    public void crawl(String url, int currentDepth) {

        // 处理带有描点的链接
        if(url.lastIndexOf("#") != -1){
            url = url.substring(0, url.lastIndexOf("#"));
        }

        // 如果达到最大深度或已经访问过该链接，则返回
        if (currentDepth > maxDepth || !visitedLinks.add(url)) {
            return;
        }

        // 获取URL对象并检查域名
        URL linkUrl = new URL(url);
        if (!linkUrl.getHost().equals(targetDomain)) {
            return;  // 跳过非目标域名的链接
        }

        Document doc = Jsoup
                .connect(url)
                .timeout(120*1000)
                .get();

        // 处理当前页面的内容
        processDocument(doc , currentDepth , url);

        // 查找所有的链接
        Elements linksOnPage = doc.select("a[href]");
        for (Element link : linksOnPage) {
            String nextLink = link.absUrl("href");
            crawl(nextLink, currentDepth + 1);
        }

    }


    private void processDocument(Document doc, int currentDepth, String url) {

        PageParse pageParse = Solon.context().getBean(siteType +  PluginConstants.PAGE_PARSE_SUFFIX);
        PageInfoBean pageInfoBean = pageParse.parse(doc , currentDepth , url) ;

        if(pageInfoBean != null){
            pageInfoList.add(pageInfoBean) ;
        }
    }


}