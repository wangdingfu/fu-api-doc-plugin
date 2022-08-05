package com.wdf.fudoc;

import com.wdf.fudoc.config.FreeMarkerConfig;
import com.wdf.fudoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.data.FuDocDataContent;
import com.wdf.fudoc.pojo.data.FuDocEnumData;
import com.wdf.fudoc.pojo.data.FuDocEnumItemData;
import com.wdf.fudoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.pojo.data.FuDocParamData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            sb.append(markdownRender(fuDocItemData)).append("\r\n");
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
        return render(fuDocItemData, "fu_doc.ftl");
    }


    public static String paramRender(List<FuDocParamData> fuDocParamDataList) {
        Map<String, Object> map = new HashMap<>();
        map.put("requestParams", fuDocParamDataList);
        return render(map, "fu_doc_object.ftl");
    }


    public static String enumRender(FuDocEnumData fuDocEnumData,Integer type) {
        String template = YesOrNo.YES.getCode() == type ? "fu_doc_enum.ftl" : "fu_doc_enum_table.ftl";
        return render(fuDocEnumData, template);
    }

    public static String render(Object data, String templateName) {
        return FreeMarkerConfig.generateContent(data, templateName);
    }


}
