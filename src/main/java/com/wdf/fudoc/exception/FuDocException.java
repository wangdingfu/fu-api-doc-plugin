package com.wdf.fudoc.exception;

/**
 * @author wangdingfu
 * @date 2022-08-15 17:19:44
 */
public class FuDocException extends RuntimeException {


    public FuDocException() {
        super();
    }

    public FuDocException(String arg0) {
        super(arg0);
    }

    public FuDocException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public FuDocException(Throwable arg0) {
        super(arg0);
    }
}
