package com.alinesno.infra.data.scheduler.tool.bean;

import lombok.Data;
import lombok.ToString;

/**
 * 页面信息Bean类
 * 该类用于封装页面的基本信息，包括标题、链接和内容
 */
@ToString
@Data
public class PageInfoBean {

    // 页面标题
    private String title ;

    // 页面链接
    private String link ;

    // 深度
    private int depth ;

    // 页面内容
    private String content ;

    public PageInfoBean(String link , String title, String content) {
        this.link = link;
        this.title = title;
        this.content = content;
    }
}
