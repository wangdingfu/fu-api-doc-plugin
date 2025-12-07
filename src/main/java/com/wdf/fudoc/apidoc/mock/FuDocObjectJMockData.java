package com.wdf.fudoc.apidoc.mock;

import com.github.jsonzou.jmockdata.JMockData;
import com.wdf.fudoc.util.FuStringUtils;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wangdingfu
 * @descption: JMockData 实现mock数据
 * @date 2022-05-22 21:49:10
 */
public class FuDocObjectJMockData implements FuDocObjectMock {

    /**
     * 获取线程安全的随机数生成器
     */
    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    /**
     * 手机号前三位常见号段
     */
    private static final String[] PHONE_PREFIXES = {
            "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
            "150", "151", "152", "153", "155", "156", "157", "158", "159",
            "170", "171", "172", "173", "175", "176", "177", "178",
            "180", "181", "182", "183", "184", "185", "186", "187", "188", "189",
            "191", "199"
    };

    /**
     * 常见姓氏
     */
    private static final String[] SURNAMES = {
            "王", "李", "张", "刘", "陈", "杨", "黄", "赵", "周", "吴",
            "徐", "孙", "马", "胡", "朱", "郭", "何", "林", "高", "罗"
    };

    /**
     * 常见名字用字
     */
    private static final String[] NAME_CHARS = {
            "伟", "芳", "娜", "敏", "静", "强", "磊", "洋", "勇", "军",
            "杰", "娟", "艳", "涛", "明", "超", "秀", "霞", "平", "刚",
            "华", "飞", "鹏", "辉", "嘉", "俊", "浩", "宇", "轩", "泽"
    };

    /**
     * 邮箱后缀
     */
    private static final String[] EMAIL_SUFFIXES = {
            "@qq.com", "@163.com", "@126.com", "@gmail.com", "@outlook.com",
            "@sina.com", "@foxmail.com", "@hotmail.com"
    };

    /**
     * 省份
     */
    private static final String[] PROVINCES = {
            "北京市", "上海市", "广东省", "江苏省", "浙江省", "山东省", "四川省", "湖北省", "河南省", "福建省"
    };

    /**
     * 城市
     */
    private static final String[] CITIES = {
            "深圳市", "广州市", "杭州市", "南京市", "苏州市", "成都市", "武汉市", "西安市", "重庆市", "青岛市"
    };

    /**
     * 区县
     */
    private static final String[] DISTRICTS = {
            "南山区", "福田区", "宝安区", "龙岗区", "罗湖区", "天河区", "海淀区", "朝阳区", "浦东新区", "江干区"
    };

    /**
     * JMockData框架mock数据
     *
     * @param classType java class类型
     * @param name      字段名
     * @param <T>       泛型
     * @return mock的值
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T mock(Class<T> classType, String name) {
        // 根据字段名生成特定格式的mock数据
        if (FuStringUtils.isNotBlank(name)) {
            // 统一处理：转小写并移除下划线（支持 user_name、userName、username 等格式）
            String normalizedName = name.toLowerCase().replace("_", "");
            Object result = mockByFieldName(classType, normalizedName);
            if (result != null) {
                return (T) result;
            }
        }
        // 默认使用JMockData生成
        return JMockData.mock(classType);
    }

    /**
     * 根据字段名生成特定格式的mock数据
     *
     * @param classType 字段类型
     * @param fieldName 字段名（已转小写并移除下划线）
     * @return mock数据，如果无法匹配则返回null
     */
    private Object mockByFieldName(Class<?> classType, String fieldName) {
        // ID字段（放在最前面，优先级最高）
        if (matches(fieldName, "id", "userid", "orderid", "productid", "goodsid", "itemid", "recordid",
                "articleid", "commentid", "messageid", "taskid", "projectid", "companyid", "deptid",
                "departmentid", "roleid", "menuid", "permissionid", "configid", "logid", "fileid", "imageid")) {
            return mockId(classType);
        }

        // 手机号
        if (matches(fieldName, "phone", "mobile", "tel", "telephone", "cellphone", "phonenumber", "mobilenumber")) {
            return mockPhone(classType);
        }

        // 邮箱
        if (matches(fieldName, "email", "mail", "emailaddress")) {
            return mockEmail(classType);
        }

        // 姓名
        if (matches(fieldName, "name", "username", "nickname", "realname", "truename", "fullname", "customername", "membername")) {
            return mockName(classType);
        }

        // 身份证号
        if (matches(fieldName, "idcard", "idno", "idnumber", "identitycard", "identityno", "cardno", "cardnumber")) {
            return mockIdCard(classType);
        }

        // 地址
        if (matches(fieldName, "address", "addr", "location", "street", "detailaddress")) {
            return mockAddress(classType);
        }

        // IP地址
        if (matches(fieldName, "ip", "ipaddress", "clientip", "serverip", "remoteip", "localip")) {
            return mockIp(classType);
        }

        // URL
        if (matches(fieldName, "url", "link", "website", "homepage", "pageurl", "imageurl", "picurl")) {
            return mockUrl(classType);
        }

        // 头像/图片URL
        if (matches(fieldName, "avatar", "photo", "headimg", "headurl", "avatarurl", "pic", "picture", "img", "image", "logo", "icon")) {
            return mockImageUrl(classType);
        }

        // 年龄
        if (matches(fieldName, "age")) {
            return mockAge(classType);
        }

        // 性别
        if (matches(fieldName, "sex", "gender")) {
            return mockSex(classType);
        }

        // 密码
        if (matches(fieldName, "password", "pwd", "passwd", "pass")) {
            return mockPassword(classType);
        }

        // 验证码
        if (matches(fieldName, "code", "verifycode", "captcha", "authcode", "checkcode", "validatecode")) {
            return mockCode(classType);
        }

        // 时间相关
        if (matches(fieldName, "createtime", "updatetime", "modifytime", "time", "timestamp", "datetime", "logintime", "registertime")) {
            return mockTimestamp(classType);
        }

        // 日期
        if (matches(fieldName, "date", "createdate", "updatedate", "birthday", "birthdate")) {
            return mockDate(classType);
        }

        // 金额
        if (matches(fieldName, "price", "amount", "money", "fee", "cost", "total", "balance", "pay", "payment")) {
            return mockMoney(classType);
        }

        // 数量
        if (matches(fieldName, "count", "num", "number", "quantity", "qty", "total", "size")) {
            return mockCount(classType);
        }

        // 状态
        if (matches(fieldName, "status", "state")) {
            return mockStatus(classType);
        }

        // 类型
        if (matches(fieldName, "type", "kind", "category")) {
            return mockType(classType);
        }

        // 描述/备注
        if (matches(fieldName, "desc", "description", "remark", "memo", "note", "comment", "content", "summary", "intro", "introduction")) {
            return mockDescription(classType);
        }

        // 标题
        if (matches(fieldName, "title", "subject", "headline")) {
            return mockTitle(classType);
        }

        // 版本
        if (matches(fieldName, "version", "ver")) {
            return mockVersion(classType);
        }

        // 排序
        if (matches(fieldName, "sort", "order", "seq", "sequence", "index", "rank")) {
            return mockSort(classType);
        }

        // 页码
        if (matches(fieldName, "page", "pageno", "pagenumber", "pagenum", "currentpage")) {
            return mockPage(classType);
        }

        // 每页大小
        if (matches(fieldName, "pagesize", "size", "limit", "perpage")) {
            return mockPageSize(classType);
        }

        // 公司/企业名称
        if (matches(fieldName, "company", "companyname", "enterprise", "enterprisename", "corp", "corporation", "firm")) {
            return mockCompany(classType);
        }

        // 银行卡号
        if (matches(fieldName, "bankcard", "bankcardno", "bankcardnumber", "bankaccount", "accountno", "accountnumber")) {
            return mockBankCard(classType);
        }

        // 经度
        if (matches(fieldName, "longitude", "lng", "lon")) {
            return mockLongitude(classType);
        }

        // 纬度
        if (matches(fieldName, "latitude", "lat")) {
            return mockLatitude(classType);
        }

        // 布尔值/开关
        if (matches(fieldName, "enabled", "disabled", "deleted", "active", "visible", "hidden", "locked", "flag", "isdeleted", "isenabled")) {
            return mockBoolean(classType);
        }

        return null;
    }

    /**
     * 检查字段名是否匹配任意一个关键词
     */
    private boolean matches(String fieldName, String... keywords) {
        for (String keyword : keywords) {
            if (fieldName.equals(keyword) || fieldName.endsWith(keyword)) {
                return true;
            }
        }
        return false;
    }

    // ============== Mock 方法实现 ==============

    private Object mockPhone(Class<?> classType) {
        String prefix = PHONE_PREFIXES[random().nextInt(PHONE_PREFIXES.length)];
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 8; i++) {
            sb.append(random().nextInt(10));
        }
        String phone = sb.toString();

        if (classType == Long.class || classType == long.class) {
            return Long.parseLong(phone);
        }
        return phone;
    }

    private Object mockEmail(Class<?> classType) {
        if (classType != String.class) return null;
        String prefix = "user" + (random().nextInt(90000) + 10000);
        String suffix = EMAIL_SUFFIXES[random().nextInt(EMAIL_SUFFIXES.length)];
        return prefix + suffix;
    }

    private Object mockName(Class<?> classType) {
        if (classType != String.class) return null;
        String surname = SURNAMES[random().nextInt(SURNAMES.length)];
        String firstName = NAME_CHARS[random().nextInt(NAME_CHARS.length)];
        // 50%概率生成两个字的名字
        if (random().nextBoolean()) {
            firstName += NAME_CHARS[random().nextInt(NAME_CHARS.length)];
        }
        return surname + firstName;
    }

    private Object mockIdCard(Class<?> classType) {
        if (classType != String.class) return null;
        // 简化实现：生成18位身份证号
        StringBuilder sb = new StringBuilder();
        // 6位地区码
        String[] areaCodes = {"110101", "310101", "440305", "330102", "320505"};
        sb.append(areaCodes[random().nextInt(areaCodes.length)]);
        // 8位出生日期 (1970-2000)
        int year = 1970 + random().nextInt(30);
        int month = 1 + random().nextInt(12);
        int day = 1 + random().nextInt(28);
        sb.append(year);
        sb.append(String.format("%02d", month));
        sb.append(String.format("%02d", day));
        // 3位顺序码
        sb.append(String.format("%03d", random().nextInt(1000)));
        // 1位校验码
        sb.append(random().nextInt(10));
        return sb.toString();
    }

    private Object mockAddress(Class<?> classType) {
        if (classType != String.class) return null;
        String province = PROVINCES[random().nextInt(PROVINCES.length)];
        String city = CITIES[random().nextInt(CITIES.length)];
        String district = DISTRICTS[random().nextInt(DISTRICTS.length)];
        int streetNo = random().nextInt(999) + 1;
        return province + city + district + "某某路" + streetNo + "号";
    }

    private Object mockIp(Class<?> classType) {
        if (classType != String.class) return null;
        return (random().nextInt(223) + 1) + "." +
                random().nextInt(256) + "." +
                random().nextInt(256) + "." +
                (random().nextInt(254) + 1);
    }

    private Object mockUrl(Class<?> classType) {
        if (classType != String.class) return null;
        String[] domains = {"example.com", "test.com", "demo.com", "sample.org"};
        return "https://www." + domains[random().nextInt(domains.length)] + "/page/" + (random().nextInt(9000) + 1000);
    }

    private Object mockImageUrl(Class<?> classType) {
        if (classType != String.class) return null;
        int size = (random().nextInt(5) + 1) * 100; // 100-500
        return "https://picsum.photos/" + size + "/" + size;
    }

    private Object mockAge(Class<?> classType) {
        int age = random().nextInt(50) + 18; // 18-67
        if (classType == Integer.class || classType == int.class) {
            return age;
        } else if (classType == Long.class || classType == long.class) {
            return (long) age;
        } else if (classType == String.class) {
            return String.valueOf(age);
        }
        return age;
    }

    private Object mockSex(Class<?> classType) {
        if (classType == Integer.class || classType == int.class) {
            return random().nextInt(2); // 0或1
        } else if (classType == String.class) {
            return random().nextBoolean() ? "男" : "女";
        }
        return random().nextInt(2);
    }

    private Object mockPassword(Class<?> classType) {
        if (classType != String.class) return null;
        return "******";
    }

    private Object mockCode(Class<?> classType) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random().nextInt(10));
        }
        String code = sb.toString();
        if (classType == Integer.class || classType == int.class) {
            return Integer.parseInt(code);
        } else if (classType == Long.class || classType == long.class) {
            return Long.parseLong(code);
        }
        return code;
    }

    private Object mockTimestamp(Class<?> classType) {
        long timestamp = System.currentTimeMillis();
        if (classType == Long.class || classType == long.class) {
            return timestamp;
        } else if (classType == String.class) {
            return String.valueOf(timestamp);
        } else if (classType == Integer.class || classType == int.class) {
            return (int) (timestamp / 1000);
        }
        return timestamp;
    }

    private Object mockDate(Class<?> classType) {
        if (classType != String.class) return null;
        int year = 2020 + random().nextInt(5);
        int month = 1 + random().nextInt(12);
        int day = 1 + random().nextInt(28);
        return String.format("%d-%02d-%02d", year, month, day);
    }

    private Object mockMoney(Class<?> classType) {
        double amount = random().nextDouble() * 10000;
        amount = Math.round(amount * 100) / 100.0; // 保留两位小数
        if (classType == BigDecimal.class) {
            return BigDecimal.valueOf(amount);
        } else if (classType == Double.class || classType == double.class) {
            return amount;
        } else if (classType == Float.class || classType == float.class) {
            return (float) amount;
        } else if (classType == Long.class || classType == long.class) {
            return Math.round(amount * 100); // 分为单位
        } else if (classType == Integer.class || classType == int.class) {
            return (int) Math.round(amount);
        } else if (classType == String.class) {
            return String.format("%.2f", amount);
        }
        return amount;
    }

    private Object mockCount(Class<?> classType) {
        int count = random().nextInt(100);
        if (classType == Integer.class || classType == int.class) {
            return count;
        } else if (classType == Long.class || classType == long.class) {
            return (long) count;
        } else if (classType == String.class) {
            return String.valueOf(count);
        }
        return count;
    }

    private Object mockStatus(Class<?> classType) {
        if (classType == Integer.class || classType == int.class) {
            return random().nextInt(3); // 0,1,2
        } else if (classType == String.class) {
            String[] statuses = {"正常", "禁用", "待审核"};
            return statuses[random().nextInt(statuses.length)];
        }
        return random().nextInt(3);
    }

    private Object mockType(Class<?> classType) {
        if (classType == Integer.class || classType == int.class) {
            return random().nextInt(5) + 1; // 1-5
        } else if (classType == String.class) {
            return "类型" + (random().nextInt(5) + 1);
        }
        return random().nextInt(5) + 1;
    }

    private Object mockDescription(Class<?> classType) {
        if (classType != String.class) return null;
        String[] descriptions = {"这是一段描述信息", "示例备注内容", "测试说明文字", "相关描述内容"};
        return descriptions[random().nextInt(descriptions.length)];
    }

    private Object mockTitle(Class<?> classType) {
        if (classType != String.class) return null;
        String[] titles = {"示例标题", "测试标题", "演示标题", "数据标题"};
        return titles[random().nextInt(titles.length)] + (random().nextInt(100) + 1);
    }

    private Object mockVersion(Class<?> classType) {
        if (classType == String.class) {
            return "v" + (random().nextInt(3) + 1) + "." + random().nextInt(10) + "." + random().nextInt(10);
        } else if (classType == Integer.class || classType == int.class) {
            return random().nextInt(10) + 1;
        }
        return "v1.0.0";
    }

    private Object mockSort(Class<?> classType) {
        int sort = random().nextInt(100);
        if (classType == Integer.class || classType == int.class) {
            return sort;
        } else if (classType == Long.class || classType == long.class) {
            return (long) sort;
        }
        return sort;
    }

    private Object mockPage(Class<?> classType) {
        int page = random().nextInt(10) + 1; // 1-10
        if (classType == Integer.class || classType == int.class) {
            return page;
        } else if (classType == Long.class || classType == long.class) {
            return (long) page;
        }
        return page;
    }

    private Object mockPageSize(Class<?> classType) {
        int[] sizes = {10, 20, 50, 100};
        int size = sizes[random().nextInt(sizes.length)];
        if (classType == Integer.class || classType == int.class) {
            return size;
        } else if (classType == Long.class || classType == long.class) {
            return (long) size;
        }
        return size;
    }

    // ============== 新增 Mock 方法 ==============

    /**
     * 生成ID（雪花算法风格的长整型ID）
     */
    private Object mockId(Class<?> classType) {
        // 生成一个类似雪花算法的ID（时间戳部分 + 随机数）
        long timestamp = System.currentTimeMillis();
        long randomPart = random().nextInt(10000);
        long id = timestamp * 10000 + randomPart;

        if (classType == Long.class || classType == long.class) {
            return id;
        } else if (classType == Integer.class || classType == int.class) {
            return random().nextInt(100000) + 1;
        } else if (classType == String.class) {
            return String.valueOf(id);
        }
        return id;
    }

    /**
     * 常见公司名称
     */
    private static final String[] COMPANY_PREFIXES = {
            "华为", "阿里", "腾讯", "百度", "京东", "美团", "字节", "小米", "网易", "滴滴",
            "顺丰", "海尔", "联想", "中兴", "格力", "比亚迪", "蔚来", "理想", "大疆", "商汤"
    };

    private static final String[] COMPANY_SUFFIXES = {
            "科技有限公司", "网络技术有限公司", "信息技术有限公司", "软件有限公司",
            "电子商务有限公司", "互联网有限公司", "数据服务有限公司", "智能科技有限公司"
    };

    private Object mockCompany(Class<?> classType) {
        if (classType != String.class) return null;
        String prefix = COMPANY_PREFIXES[random().nextInt(COMPANY_PREFIXES.length)];
        String suffix = COMPANY_SUFFIXES[random().nextInt(COMPANY_SUFFIXES.length)];
        return prefix + suffix;
    }

    /**
     * 生成银行卡号（符合Luhn算法的16位卡号）
     */
    private static final String[] BANK_PREFIXES = {
            "6222", "6228", "6217", "6225", "6214", // 工商、农业、建设等银行前缀
            "6259", "6226", "6227", "6229", "6230"
    };

    private Object mockBankCard(Class<?> classType) {
        if (classType != String.class) return null;
        StringBuilder sb = new StringBuilder();
        sb.append(BANK_PREFIXES[random().nextInt(BANK_PREFIXES.length)]);
        // 生成剩余12位数字
        for (int i = 0; i < 12; i++) {
            sb.append(random().nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 生成经度（中国范围：73.66-135.05）
     */
    private Object mockLongitude(Class<?> classType) {
        // 中国经度范围约为 73.66°E - 135.05°E
        double lng = 73.66 + random().nextDouble() * (135.05 - 73.66);
        lng = Math.round(lng * 1000000) / 1000000.0; // 保留6位小数

        if (classType == Double.class || classType == double.class) {
            return lng;
        } else if (classType == Float.class || classType == float.class) {
            return (float) lng;
        } else if (classType == String.class) {
            return String.format("%.6f", lng);
        } else if (classType == BigDecimal.class) {
            return BigDecimal.valueOf(lng);
        }
        return lng;
    }

    /**
     * 生成纬度（中国范围：3.86-53.55）
     */
    private Object mockLatitude(Class<?> classType) {
        // 中国纬度范围约为 3.86°N - 53.55°N
        double lat = 3.86 + random().nextDouble() * (53.55 - 3.86);
        lat = Math.round(lat * 1000000) / 1000000.0; // 保留6位小数

        if (classType == Double.class || classType == double.class) {
            return lat;
        } else if (classType == Float.class || classType == float.class) {
            return (float) lat;
        } else if (classType == String.class) {
            return String.format("%.6f", lat);
        } else if (classType == BigDecimal.class) {
            return BigDecimal.valueOf(lat);
        }
        return lat;
    }

    /**
     * 生成布尔值
     */
    private Object mockBoolean(Class<?> classType) {
        boolean value = random().nextBoolean();
        if (classType == Boolean.class || classType == boolean.class) {
            return value;
        } else if (classType == Integer.class || classType == int.class) {
            return value ? 1 : 0;
        } else if (classType == String.class) {
            return String.valueOf(value);
        }
        return value;
    }
}
