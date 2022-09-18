package com.wdf.fudoc.request.global;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-09-18 22:24:43
 */
@Getter
@Setter
public class RequestHistory {


    /**
     * 请求记录数量
     */
    private Integer count;


    /**
     * 请求id集合 按照请求顺序排序
     */
    private List<Integer> requestIds;


}
