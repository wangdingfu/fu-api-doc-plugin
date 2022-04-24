package com.wdf.apidoc.util;

import com.google.common.collect.Lists;
import com.wdf.apidoc.constant.ApiDocConstants;
import com.wdf.apidoc.pojo.bo.CommentLineBO;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 注释工具类
 * @date 2022-04-24 22:24:09
 */
public class CommentUtils {


    /**
     * 解析注释
     *
     * @param comment 注释内容
     * @return 每一行注释集合
     */

    /**
     * 解析注释 注释内容
     */
    public static List<CommentLineBO> parseComment(String comment) {
        List<CommentLineBO> resultList = Lists.newArrayList();
        if (StringUtils.isNotBlank(comment)) {
            int row = 0, length = comment.length();
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < length; j++) {
                char charAt = comment.charAt(j);
                if (charAt == 10) {
                    //10为换行符 表示已经读完了一行
                    CommentLineBO commentLineBO = parse(line.toString(), row);
                    if (Objects.isNull(commentLineBO)) {
                        //commentLineBO为null 则说明解析异常 不是注释 直接退出
                        break;
                    }
                    row++;
                    resultList.add(commentLineBO);
                }
                line.append(charAt);
            }
        }
        return resultList;
    }

    private static CommentLineBO parse(String lineText, int row) {
        CommentLineBO commentLineBO = new CommentLineBO();
        commentLineBO.setRow(row);
        if (StringUtils.isBlank(lineText)) {
            return commentLineBO;
        }
        if (row == 0) {
            //第一行解析
            if (lineText.startsWith(ApiDocConstants.Comment.COMMENT_START_1)) {
                commentLineBO.setPrefix(StringUtils.substringBefore(lineText, ApiDocConstants.Comment.COMMENT_X));
                if(lineText.endsWith(ApiDocConstants.Comment.COMMENT_END_1)){
                    commentLineBO.setSuffix(StringUtils.substringAfter(lineText, ApiDocConstants.Comment.COMMENT_X));
                }

            }else if(lineText.startsWith(ApiDocConstants.Comment.COMMENT_START_2)){

            }
            return null;
        }
        if(lineText.indexOf("@") > 0){
            //有注释标签
            String s = StringUtils.substringBetween(lineText, "@", " ");
        }
        //没有注释标签
        return null;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.substringBetween("@param", "@", " "));
    }


}
