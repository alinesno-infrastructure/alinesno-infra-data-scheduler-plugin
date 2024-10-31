package com.alinesno.infra.data.scheduler.tool.office;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.style.*;
import com.deepoove.poi.plugin.markdown.MarkdownRenderData;
import com.deepoove.poi.plugin.markdown.MarkdownRenderPolicy;
import com.deepoove.poi.plugin.markdown.MarkdownStyle;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomifeng1010
 * @version 1.0
 * @date: 2024-08-24 17:23
 * @Description
 */
@UtilityClass
@Slf4j
public class MarkdownUtil {


    /**
     * markdown转html
     *
     * @param markdownContent
     * @return
     */
    public String markdownToHtml(String markdownContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(document);
        log.info(htmlContent);
        return htmlContent;
    }


    /**
     * 将markdown格式内容转换为word并保存在本地
     *
     * @param markdownContent
     * @param outputFileName
     */
    public void toDoc(String markdownContent, String outputFileName) {
        log.info("markdownContent:{}", markdownContent);
        MarkdownRenderData code = new MarkdownRenderData();
        code.setMarkdown(markdownContent);
        MarkdownStyle style = MarkdownStyle.newStyle();
        style = setMarkdownStyle(style);
        code.setStyle(style);
//      markdown样式处理与word模板中的标签{{md}}绑定
        Map<String, Object> data = new HashMap<>();
        data.put("md", code);

        Configure config = Configure.builder().bind("md", new MarkdownRenderPolicy()).build();
        //            获取classpath
        String path = MarkdownUtil.class.getClassLoader().getResource("").getPath();
        log.info("classpath:{}", path);
        //由于部署到linux上后，程序是从jar包中去读取resources下的文件的，所以需要使用流的方式读取，所以获取流，而不是直接使用文件路径

        // 所以可以这样获取 InputStream resourceAsStream = MarkdownUtil.class.getClassLoader().getResourceAsStream("");
        // 建议使用spring的工具类来获取，如下
//            ClassPathResource resource = new ClassPathResource("markdown" + File.separator + "markdown_template.docx");
//            InputStream resourceAsStream = resource.getInputStream();
//            XWPFTemplate.compile(resourceAsStream, config)
//                    .render(data)
//                    .writeToFile(path + "out_markdown_" + outputFileName + ".docx");

    }

    /**
     * 设置转换为word文档时的基本样式
     *
     * @param style
     * @return
     */
    public MarkdownStyle setMarkdownStyle(MarkdownStyle style) {
//        一定设置为false,不然生成的word文档中各元素前边都会加上有层级效果的一串数字,
//        比如一级标题 前边出现1 二级标题出现1.1 三级标题出现1.1.1这样的数字
        style.setShowHeaderNumber(false);
        // 修改默认的表格样式
        // table header style(表格头部,通常为表格顶部第一行,用于设置列标题)
        RowStyle headerStyle = new RowStyle();
        CellStyle cellStyle = new CellStyle();
//        设置表格头部的背景色为灰色
        cellStyle.setBackgroundColor("cccccc");
        Style textStyle = new Style();
//        设置表格头部的文字颜色为黑色
        textStyle.setColor("000000");
//        头部文字加粗
        textStyle.setBold(true);
//        设置表格头部文字大小为12
        textStyle.setFontSize(12);
//       设置表格头部文字垂直居中
        cellStyle.setVertAlign(XWPFTableCell.XWPFVertAlign.CENTER);

        cellStyle.setDefaultParagraphStyle(ParagraphStyle.builder().withDefaultTextStyle(textStyle).build());
        headerStyle.setDefaultCellStyle(cellStyle);
        style.setTableHeaderStyle(headerStyle);

//        table border style(表格边框样式)
        BorderStyle borderStyle = new BorderStyle();
//        设置表格边框颜色为黑色
        borderStyle.setColor("000000");
//        设置表格边框宽度为3px
        borderStyle.setSize(3);
//        设置表格边框样式为实线
        borderStyle.setType(XWPFTable.XWPFBorderType.SINGLE);
        style.setTableBorderStyle(borderStyle);

//        设置普通的引用文本样式
        ParagraphStyle quoteStyle = new ParagraphStyle();
//        设置段落样式
        quoteStyle.setSpacingBeforeLines(0.5d);
        quoteStyle.setSpacingAfterLines(0.5d);

//        设置段落的文本样式
        Style quoteTextStyle = new Style();
        quoteTextStyle.setColor("000000");
        quoteTextStyle.setFontSize(8);
        quoteTextStyle.setItalic(true);
        quoteStyle.setDefaultTextStyle(quoteTextStyle);
        style.setQuoteStyle(quoteStyle);

        return style;
    }


    public static void main(String[] args) {
        String markdownContent = "# 一级标题\n" +
                "## 二级标题\n" +
                "### 三级标题\n" +
                "#### 四级标题\n" +
                "##### 五级标题\n" +
                "###### 六级标题\n" +
                "## 段落\n" +
                "这是一段普通的段落。\n" +
                "## 列表\n" +
                "### 无序列表\n" +
                "- 项目1\n" +
                "- 项目2\n" +
                "- 项目3\n" +
                "### 有序列表\n" +
                "1. 项目1\n" +
                "2. 项目2\n" +
                "3. 项目3\n" +
                "## 链接\n" +
                "[百度](https://www.baidu.com)\n" +
                "## 图片\n" +
                "![图片描述](https://www.baidu.com/img/bd_logo1.png)\n" +
                "## 表格\n" +
                "| 表头1 | 表头2 | 表头3 |\n" +
                "|-------|-------|-------|\n" +
                "| 单元格1 | 单元格2 | 单元格3 |\n" +
                "| 单元格4 | 单元格5 | 单元格6 |";
        toDoc(markdownContent, "test23");


    }
}