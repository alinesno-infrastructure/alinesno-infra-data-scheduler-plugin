package com.alinesno.infra.data.scheduler.tool;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alinesno.infra.data.scheduler.tool.bean.PageInfoBean;
import com.alinesno.infra.data.scheduler.tool.crawler.WebCrawler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.bean.LifecycleBean;

import java.io.File;
import java.nio.charset.Charset;

@Slf4j
@Component
public class Config implements LifecycleBean {

    @Inject("${tools.crawler.parse}")
    private String parseType;

    @Inject("${tools.crawler.site}")
    private String site;

    @Inject("${tools.crawler.fileName:crawler-site}")
    private String fileName ;

    @Inject("${tools.crawler.savePath:${java.io.tmpdir}}")
    private String savePath;

    @SneakyThrows
    @Override
    public void start() {

        log.debug("parseType = {} , savePath = {} , site = {}", parseType , savePath , site);

        String startUrl = site ;
        int defaultMaxDepth = 10000;

        WebCrawler crawler = new WebCrawler(startUrl, defaultMaxDepth);
        crawler.setSiteType(parseType);
        crawler.crawl(startUrl, 0);

        // 创建文件
        File file = new File(savePath , fileName + ".json");

        FileUtils.write(file, JSONArray.toJSONString(crawler.getPageInfoList()) , Charset.defaultCharset() , false);
        log.info("文件写入成功：{}", file.getAbsolutePath());
    }

}