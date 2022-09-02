package icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * 图标管理
 * @author wangdingfu
 * @date 2022-08-21 00:44:26
 */
public interface FuDocIcons {


    Icon FU_DOC = IconLoader.getIcon("icon/fudoc.svg", FuDocIcons.class);
    Icon FU_REQUEST_SEND = IconLoader.getIcon("icon/send.svg", FuDocIcons.class);
    Icon FU_REQUEST_HEADER = IconLoader.getIcon("icon/header.svg", FuDocIcons.class);
    Icon FU_REQUEST_PARAMS = IconLoader.getIcon("icon/params.svg", FuDocIcons.class);
    Icon FU_REQUEST_BODY = IconLoader.getIcon("icon/body.svg", FuDocIcons.class);
}
