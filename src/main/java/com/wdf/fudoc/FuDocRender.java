package com.wdf.fudoc;

import com.wdf.fudoc.config.FreeMarkerConfig;
import com.wdf.fudoc.pojo.data.FuDocItemData;

import java.util.List;

/**
 * @author wangdingfu
 * @descption: 根据数据渲染生成MarkDown接口文档
 * @date 2022-05-30 23:20:00
 */
public class FuDocRender {


    /**
     * 将多个接口文档数据一起渲染成markdown格式接口文档内容
     *
     * @param dataList 接口文档数据集合
     * @return markdown格式的接口文档内容
     */
    public static String markdownRender(List<FuDocItemData> dataList) {
        StringBuilder sb = new StringBuilder();
        for (FuDocItemData fuDocItemData : dataList) {
            sb.append(markdownRender(fuDocItemData)).append("\r\n\r\n\r\n");
        }
        return sb.toString();
    }


    /**
     * 根据组装好的数据渲染成markdown接口文档内容
     *
     * @param fuDocItemData 组装完毕的接口文档数据
     * @return 渲染完毕后markdown接口文档内容
     */
    public static String markdownRender(FuDocItemData fuDocItemData) {
        return FreeMarkerConfig.generateContent(fuDocItemData, "fu_doc.ftl");
    }

}
