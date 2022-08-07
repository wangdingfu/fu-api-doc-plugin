package com.wdf.fudoc.pojo.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.enumtype.CommentTagType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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
    private Map<String, List<CommentTagData>> tagMap;


    /**
     * 获取return tag 的值
     */
    public String getReturnComment() {
        return getTagComment(CommentTagType.RETURN.getName()).getValue();
    }


    /**
     * 根据指定tag名称获取第一条tag注释内容
     *
     * @param tag tag名称
     * @return 该tag的第一条注释内容
     */
    public CommentTagData getTagComment(String tag) {
        List<CommentTagData> tagComments = getTagComments(tag);
        return CollectionUtils.isNotEmpty(tagComments) ? tagComments.get(0) : new CommentTagData();
    }

    /**
     * 根据指定tag名称获取注释内容集合
     *
     * @param tag tag名称
     * @return 指定tag的注释内容集合
     */
    public List<CommentTagData> getTagComments(String tag) {
        List<CommentTagData> commentTagDataList;
        if (StringUtils.isNotBlank(tag) && MapUtils.isNotEmpty(tagMap) && CollectionUtils.isNotEmpty(commentTagDataList = tagMap.get(tag))) {
            return commentTagDataList;
        }
        return Lists.newArrayList();
    }

    /**
     * 获取@param注释的value
     *
     * @param paramName 当前行中的“paramName”
     * @return 上一行的 “当前行中的“paramName””
     */
    public String getCommentByParam(String paramName) {
        return getCommentTagValue(CommentTagType.PARAM.getName(), paramName);
    }

    /**
     * 根据参数名称获取对应注释
     *
     * @param tagName tag名称（例如当前行注释中的“tagName”）
     * @param key     tag的key（例如当前行注释中的“tag的key”）
     * @return 对应的注释内容
     */
    public String getCommentTagValue(String tagName, String key) {
        if (StringUtils.isBlank(tagName) || StringUtils.isBlank(key)) {
            return StringUtils.EMPTY;
        }
        List<CommentTagData> tagComments = getTagComments(tagName);
        if (CollectionUtils.isNotEmpty(tagComments)) {
            return tagComments.stream().filter(f -> key.equals(f.getName())).findFirst().orElse(new CommentTagData()).getValue();
        }
        return StringUtils.EMPTY;
    }


}
