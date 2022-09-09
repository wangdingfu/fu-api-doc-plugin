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
    Icon FU_REQUEST_BULK_EDIT = IconLoader.getIcon("icon/bulk_edit.svg", FuDocIcons.class);
    Icon FU_REQUEST_FORM = IconLoader.getIcon("icon/form.svg", FuDocIcons.class);
    Icon FU_REQUEST_FILE_BINARY = IconLoader.getIcon("icon/file-binary.svg", FuDocIcons.class);
    Icon FU_REQUEST_URLENCODED = IconLoader.getIcon("icon/urlencoded.svg", FuDocIcons.class);
    Icon FU_REQUEST_JSON = IconLoader.getIcon("icon/json.svg", FuDocIcons.class);
    Icon FU_REQUEST_IGNORE = IconLoader.getIcon("icon/ignore.svg", FuDocIcons.class);
    Icon FU_REQUEST_RAW = IconLoader.getIcon("icon/raw.svg", FuDocIcons.class);
    Icon FU_REQUEST_MAGIC = IconLoader.getIcon("icon/magic-wand.svg", FuDocIcons.class);
}
