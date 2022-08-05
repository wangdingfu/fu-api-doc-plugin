package com.wdf.fudoc.pojo.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.enumtype.CommentTagType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @description 注释数据对象
 * @Date 2022-04-21 14:11:36
 */
@Getter
@Setter
public class ApiDocCommentData {

    /**
     * 注释标题
     */
    private String commentTitle;

    /**
     * 注释详细描述信息
     */
    private String commentDetailInfo;

    /**
     * tag注释内容
     */
    private Map<String, Map<String, CommentTagData>> tagMap;


    /**
     * 获取return tag 的值
     */
    public String getReturnComment() {
        return getTagComment(CommentTagType.RETURN.getName()).getValue();
    }


    public CommentTagData getTagComment(String tag) {
        if (MapUtils.isNotEmpty(tagMap)) {
            Map<String, CommentTagData> paramCommentMap = tagMap.get(tag);
            if (MapUtils.isNotEmpty(paramCommentMap)) {
                return Lists.newArrayList(paramCommentMap.values()).get(0);
            }
        }
        return new CommentTagData();
    }

    /**
     * 根据参数名称获取对应注释
     *
     * @param param 参数名
     * @return 对应的注释内容
     */
    public String getCommentByParam(String param) {
        if (StringUtils.isNotBlank(param) && MapUtils.isNotEmpty(tagMap)) {
            Map<String, CommentTagData> paramCommentMap = tagMap.get(CommentTagType.PARAM.getName());
            CommentTagData commentTagData;
            if (MapUtils.isNotEmpty(paramCommentMap) && Objects.nonNull(commentTagData = paramCommentMap.get(param))) {
                return commentTagData.getValue();
            }
        }
        return StringUtils.EMPTY;
    }


}
