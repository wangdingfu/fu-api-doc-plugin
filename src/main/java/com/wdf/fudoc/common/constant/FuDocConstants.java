package com.wdf.fudoc.common.constant;


/**
 * @author wangdingfu
 * @descption: 类型常量
 * @date 2022-04-10 21:42:57
 */
public interface FuDocConstants {


    String KEY = "key";
    String VALUE = "value";

    String FU_DOC = "Fu Doc";
    String FU_DOC_PATH = "fu-doc";
    String FU_REQUEST_PATH = "fu-request";

    String FU_DOC_FILE = "FuDocFile.";

    String ROOT = "root";


    String API_ID = "apiId";
    String FILE = "file";

    String IDEA_DIR = ".idea";
    String API_DIR = "api";


    String SPRING_PARAM = "springParam";
    String ROOT_PARAM_TYPE = "rootParamType";

    String PATH_VARIABLE = "path_variable";

    String CONTENT_TYPE = "Content-Type";


    String DEFAULT_HOST = "http://localhost";

    interface AnnotationAttr {
        String MESSAGE = "message";
        String NAME = "name";
        String DESCRIPTION = "description";
        String GROUPS = "groups";
        String METHOD = "method";
        String REQUIRE = "require";
    }

    interface ModifierProperty {
        String STATIC = "static";
        String FINAL = "final";
        String VOID = "void";
    }

    interface ExtInfo {
        String IS_ATTR = "isAttr";
        /**
         * 标识当前对象还未解析完成
         */
        String IS_EARLY = "isEarly";

        String GENERICS_TYPE = "genericsType";

        String REFERENCE_DESC_ID = "referenceDescId";
        String ROOT = "root";
    }

    interface ClassPkg {
        String BIG_DECIMAL = "java.math.BigDecimal";
        String TIMESTAMP = "java.sql.Timestamp";
        String LOCAL_TIME = "java.time.LocalTime";
        String LOCAL_DATE = "java.time.LocalDate";
        String LOCAL_DATE_TIME = "java.time.LocalDateTime";
        String BIG_INTEGER = "java.math.BigInteger";
    }

    interface Notify {
        String NOTIFY_GROUP = "com.wdf.fudoc.notification.group";
        String NOTIFY_START_GROUP = "com.wdf.fudoc.notification.start.group";
    }


    interface Comment {

        /**
         * 请求参数的注释tag
         * TODO 后期可改成从配置中取
         */
        String PARAM = "param";
        /**
         * 方法返回值注释tag
         */
        String RETURN = "return";

        /**
         * psi中描述注释内容的类型
         */
        String PSI_COMMENT_DATA = "DOC_COMMENT_DATA";
        String PSI_COMMENT_TAG_VALUE = "DOC_TAG_VALUE_ELEMENT";
        String PSI_PARAMETER_REF = "DOC_PARAMETER_REF";


        String COMMENT_START_1 = "/*";
        String COMMENT_START_2 = "//";
        String COMMENT_END_1 = "*/";

        String COMMENT_X = "*";
    }

    interface Enum {
        String CODE = "code";
        String CODE_INDEX = "index";


        String MSG = "msg";
        String MSG_MESSAGE = "message";
        String MSG_VIEW = "view";
        String MSG_DESC = "desc";
    }


    interface SettingForm {

    }


}
