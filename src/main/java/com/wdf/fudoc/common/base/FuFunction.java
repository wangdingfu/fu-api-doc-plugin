package com.wdf.fudoc.common.base;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author wangdingfu
 * @date 2023-07-12 21:50:38
 */
@FunctionalInterface
public interface FuFunction<T, R> extends Function<T, R>, Serializable {
}
