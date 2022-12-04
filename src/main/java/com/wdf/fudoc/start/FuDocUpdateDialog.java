package com.wdf.fudoc.start;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.wdf.fudoc.components.FuHtmlComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-12-03 17:50:12
 */
public class FuDocUpdateDialog implements StartupActivity {

    private static String html = "<html>\n" +
            "<head>\n" +
            "    <style type=\"text/css\">\n" +
            "        body {\n" +
            "            margin: 10px;\n" +
            "            background: #C0C0C0;\n" +
            "            color: #333333;\n" +
            "        }\n" +
            "\n" +
            "        code {\n" +
            "            background-color: #eeee11;\n" +
            "            margin: 4px;\n" +
            "        }\n" +
            "\n" +
            "        pre {\n" +
            "            padding: 10px;\n" +
            "        }\n" +
            "\n" +
            "        li {\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "\n" +
            "        .endText {\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1 style=\"text-align: center\">【Fu Doc】更新公告</h1>\n" +
            "\n" +
            "<h2> 亲爱的JAVA程序员们：</h2>\n" +
            "\n" +
            "<p class=\"title\"> <b> &emsp;&emsp;【Fu Doc】本次重磅推出了 <span style=\"color: red\">一键发起http请求</span> 功能 极大的提升了伙伴们调试接口的效率</b></p><br>\n" +
            "<p class=\"title\"> <b> &emsp;&emsp;&nbsp;无需每次繁琐的去PostMan手动维护一大堆接口 让您更加专注的在IDEA上开发和调试接口</b></p><br>\n" +
            "\n" +
            "\n" +
            "<b> 【特性介绍】 </b>\n" +
            "<ol>\n" +
            "    <li style=\"color: red\">无需任何配置 自动识别当前项目的端口以及接口请求参数内容自动填充</li>\n" +
            "\n" +
            "    <li style=\"color: #422517\">将鼠标置于接口方法体上 直接通过快捷键 <b>ALT+R</b> 弹出请求窗口</li>\n" +
            "\n" +
            "    <li style=\"color: #e67700\">和生成接口文档功能联动 生成的接口文档示例数据会使用最近一次请求记录的真实数据</li>\n" +
            "\n" +
            "    <li style=\"color: #4f6f46\">发起请求后会自动保存本次请求记录 方便下次继续发起（支持手动保存）</li>\n" +
            "\n" +
            "    <li style=\"color: #2e59a7\">支持批量编辑请求参数 同PostMan中的<b>Bulk Edit</b>功能 </li>\n" +
            "\n" +
            "    <li style=\"color: #b0436f\">支持文件上传和下载 当检测到响应结果为文件时 自动切换保存文件窗口</li>\n" +
            "</ol>\n" +
            "<div></div>\n" +
            "\n" +
            "<b> 【示意图】 </b>\n" +
            "<div style=\"margin-top: 10px\">\n" +
            "    <img src=\"https://bigsight-app.oss-cn-shenzhen.aliyuncs.com/upload/20221204/5b0f3028500cf588cfea9b2b3b3ae1c4.jpg\" >\n" +
            "    <br>\n" +
            "    <img src=\"https://bigsight-app.oss-cn-shenzhen.aliyuncs.com/upload/20221204/454d7885484107664f197e4b84e87274.jpg\" >\n" +
            "</div>\n" +
            "<div></div>\n" +
            "<b> 【结尾语】 </b>\n" +
            "<div></div>\n" +
            "\n" +
            "<div>\n" +
            "    <div class=\"endText\" style=\"color: #5c940d\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;【Fu Doc】的初衷一直都是为了提升java程序员们的工作效率. 后续将持续推出更多更好用的功能<br></div>\n" +
            "    <div class=\"endText\" style=\"color: #5c940d\">如果觉得这个插件还可以的话. 非常希望大家能向身边的朋友和同事们推广 只有使用的人越来越多 这</div>\n" +
            "    <div class=\"endText\" style=\"color: #5c940d\">个插件才会越加的完善和成熟 从而也可以更大程度的给大家带来效率上的提升</div>\n" +
            "    <div class=\"endText\" style=\"color: #e67700;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;大家如果在使用过程中有什么问题和建议 非常欢迎大家在<b>github</b>或者<b>码云</b>上提交给我. 或则给我发</div>\n" +
            "    <div class=\"endText\" style=\"color: #e67700;\">送邮件（<b>wangdingfu1024@163.com</b>）我会及时回复并及时解决大家的问题.</div>\n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    @Override
    public void runActivity(@NotNull Project project) {
        new FuHtmlComponent(project, "【Fu Doc 更新公告】", html).showAndGet();
    }
}
