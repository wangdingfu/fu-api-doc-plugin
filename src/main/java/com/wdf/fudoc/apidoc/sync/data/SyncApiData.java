package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.dto.ApiTreeKeyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 同步接口用户选择tab框数据
 *
 * @author wangdingfu
 * @date 2023-01-01 14:04:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncApiData extends ApiTreeKeyDTO {

    /**
     * 需要同步的接口
     */
    private FuDocItemData fuDocItemData;

}
