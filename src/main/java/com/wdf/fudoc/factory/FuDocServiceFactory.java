package com.wdf.fudoc.factory;

import com.wdf.fudoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.service.FuDocService;
import com.wdf.fudoc.service.GenControllerFuDocServiceImpl;
import com.wdf.fudoc.service.GenInterfaceFuDocServiceImpl;
import com.wdf.fudoc.service.GenObjectFuDocServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-07-18 20:56:57
 */
public class FuDocServiceFactory {

    private static final Map<JavaClassType, FuDocService> fuDocServiceMap = new HashMap<>();


    static {
        fuDocServiceMap.put(JavaClassType.CONTROLLER, new GenControllerFuDocServiceImpl());
        fuDocServiceMap.put(JavaClassType.OBJECT, new GenObjectFuDocServiceImpl());
        fuDocServiceMap.put(JavaClassType.INTERFACE, new GenInterfaceFuDocServiceImpl());
        fuDocServiceMap.put(JavaClassType.ENUM, new GenInterfaceFuDocServiceImpl());
    }

    public static FuDocService getFuDocService(JavaClassType javaClassType) {
        if (Objects.nonNull(javaClassType)) {
            return fuDocServiceMap.get(javaClassType);
        }
        return null;
    }
}
