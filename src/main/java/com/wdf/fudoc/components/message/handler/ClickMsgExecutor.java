package com.wdf.fudoc.components.message.handler;

import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.common.po.FuDocConfigPO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.storage.FuDocConfigStorage;

/**
 * @author wangdingfu
 * @date 2022-11-30 17:37:20
 */
public class ClickMsgExecutor implements FuMsgExecutor {


    @Override
    public void execute(String msgId, FuMsgItemBO fuMsgItemBO) {
        String value = fuMsgItemBO.getValue();
        boolean flag;
        if ("close icon".equals(value)) {
            flag = false;
        } else if ("open icon".equals(value)) {
            flag = true;
        } else {
            //后期重构
            return;
        }
        FuDocConfigStorage instance = FuDocConfigStorage.INSTANCE;
        FuDocConfigPO fuDocConfigPO = instance.readData();
        fuDocConfigPO.setShowControllerIcon(flag);
        instance.saveData(fuDocConfigPO);
        FuDocNotification.notifyInfo(FuBundle.message(flag ? "fudoc.icon.open.title" : "fudoc.icon.close.title"));
    }
}
