package com.wdf.fudoc.console;

import com.wdf.fudoc.console.holder.ValueHolder;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Objects;

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

    private final ValueHolder<Integer> valueHolder = new ValueHolder<>();

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
    public int waitFor() {
        Integer value = valueHolder.value();
        return Objects.isNull(value) ? 0 : value;
    }

    @Override
    public int exitValue() {
        Integer value = valueHolder.value();
        return Objects.isNull(value) ? 0 : value;
    }

    public void setExitValue(Integer exitValue) {
        valueHolder.success(exitValue);
        destroy();
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
