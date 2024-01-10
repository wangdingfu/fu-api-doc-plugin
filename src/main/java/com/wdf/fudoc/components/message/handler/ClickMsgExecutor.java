package com.wdf.fudoc.components.message.handler;

import com.wdf.api.base.FuBundle;
import com.wdf.api.notification.FuDocNotification;
import com.wdf.api.storage.po.FuDocConfigPO;
import com.wdf.api.msg.bo.FuMsgItemBO;
import com.wdf.api.storage.FuDocConfigStorage;

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
