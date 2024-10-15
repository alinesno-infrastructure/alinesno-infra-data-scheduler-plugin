package com.alinesno.infra.data.scheduler.tool.parse;

import com.alinesno.infra.data.scheduler.plugin.core.constant.PluginConstants;
import com.alinesno.infra.data.scheduler.tool.bean.PageInfoBean;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.noear.solon.annotation.Component;

/**
 * Vuepress网站爬虫解析类
 */
@Slf4j
@Component("vuepress" + PluginConstants.PAGE_PARSE_SUFFIX)
public class VuepressPageParse implements PageParse {

    @Override
    public PageInfoBean parse(Document doc, int depth , String url) {

        // 获取页面标题
        String title = doc.title();

        // 获取 class="page" 的内容
        Element pageContent = doc.selectFirst(".page");
        String pageText = (pageContent != null) ? pageContent.text() : doc.body().text();

        log.debug("正在抓取:{} , 标题:{} , depth:{} " , url , title , depth) ;

        PageInfoBean page = new PageInfoBean(url , title, pageText);
        page.setDepth(depth);

        return page ;
    }
}
