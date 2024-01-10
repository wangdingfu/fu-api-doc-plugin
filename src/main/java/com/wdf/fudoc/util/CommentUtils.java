package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.pojo.bo.CommentLineBO;
import com.wdf.fudoc.util.FuStringUtils;

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
    public static List<CommentLineBO> parseComment(String comment) {
        List<CommentLineBO> resultList = Lists.newArrayList();
        if (FuStringUtils.isNotBlank(comment)) {
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
        if (FuStringUtils.isBlank(lineText)) {
            return commentLineBO;
        }
        if (row == 0) {
            //第一行解析
            if (lineText.startsWith(FuDocConstants.Comment.COMMENT_START_1)) {
                commentLineBO.setPrefix(FuStringUtils.substringBefore(lineText, FuDocConstants.Comment.COMMENT_X));
                if (lineText.endsWith(FuDocConstants.Comment.COMMENT_END_1)) {
                    commentLineBO.setSuffix(FuStringUtils.substringAfter(lineText, FuDocConstants.Comment.COMMENT_X));
                }
            } else if (lineText.startsWith(FuDocConstants.Comment.COMMENT_START_2)) {

            }
            return null;
        }
        if (parseTag(lineText, commentLineBO)) {
            //有注释标签
            return commentLineBO;
        }

        //没有注释标签
        return null;
    }


    private static boolean parseTag(String lineText, CommentLineBO commentLineBO) {
        if (FuStringUtils.isNotBlank(lineText)) {
            StringBuilder sb = null;
            String tag = null, param = null;
            for (int i = 0; i < lineText.length(); i++) {
                char charAt = lineText.charAt(i);
                if (Objects.isNull(tag)) {
                    if (charAt == 64) {
                        sb = Objects.isNull(sb) ? new StringBuilder() : sb;
                        continue;
                    }
                    if (Objects.isNull(sb)) {
                        if (charAt == 32 || charAt == 42 || charAt == 10) {
                            continue;
                        }
                        break;
                    }
                    if (charAt == 32 || charAt == 10) {
                        tag = sb.toString();
                        sb = null;
                        continue;
                    }
                    sb.append(charAt);
                } else if (Objects.isNull(param)) {
                    if (Objects.isNull(sb) && (charAt == 32 || charAt == 10)) {
                        continue;
                    }
                    sb = Objects.isNull(sb) ? new StringBuilder() : sb;
                    if (charAt == 32 || charAt == 10) {
                        param = sb.toString();
                        sb = null;
                        continue;
                    }
                    sb.append(charAt);
                } else {
                    if (Objects.isNull(sb) && (charAt == 32 || charAt == 10)) {
                        continue;
                    }
                    sb = Objects.isNull(sb) ? new StringBuilder() : sb;
                    sb.append(charAt);
                }
            }
            commentLineBO.setTag(Objects.nonNull(tag) ? tag : FuStringUtils.EMPTY);
            commentLineBO.setKey(Objects.nonNull(param) ? param : FuStringUtils.EMPTY);
            commentLineBO.setContent(Objects.nonNull(sb) ? sb.toString() : FuStringUtils.EMPTY);
            return true;
        }
        return false;
    }


}
