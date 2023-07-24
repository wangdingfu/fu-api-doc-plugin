package com.wdf.fudoc.test.action;

import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author wangdingfu
 * @date 2023-07-24 11:08:54
 */
public class PipedProcess extends Process {

    private PipedOutputStream outputStream;
    private PipedInputStream inputStream;
    private PipedInputStream errorInputStream;


    @Getter
    private PipedInputStream inputForOutputStream;
    @Getter
    private PipedOutputStream outForInputStream;
    @Getter
    private PipedOutputStream outForErrorInputStream;

    public PipedProcess() {
        try {
            this.inputStream = new PipedInputStream(2048);
            this.outForInputStream = new PipedOutputStream(inputStream);

            this.errorInputStream = new PipedInputStream(2048);
            this.outForErrorInputStream = new PipedOutputStream(errorInputStream);

            this.inputForOutputStream = new PipedInputStream(2048);
            this.outputStream = new PipedOutputStream(inputForOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public InputStream getErrorStream() {
        return errorInputStream;
    }

    @Override
    public int waitFor() throws InterruptedException {
        return 0;
    }

    @Override
    public int exitValue() {
        return 0;
    }

    @Override
    public void destroy() {
        IOUtils.closeQuietly(
                this.errorInputStream,
                this.inputForOutputStream,
                this.inputStream,
                this.outForErrorInputStream,
                this.outForInputStream,
                this.outputStream
        );
    }
}
