package com.alinesno.infra.data.scheduler.tool.parse;

import com.alinesno.infra.data.scheduler.tool.bean.PageInfoBean;
import org.jsoup.nodes.Document;

public interface PageParse {

    /**
     * 解析网页并返回文本
     *
     * @param doc
     * @param depth
     * @param url
     * @return
     */
    PageInfoBean parse(Document doc, int depth , String url)  ;

}
