package com.serotonin.modbus4j.sero.messaging;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * This class provides a stoppable listener for an input stream that sends arbitrary information. A read() call to an
 * input stream will typically not return as long as the stream is not sending any data. This class provides a way for
 * stream listeners to safely listen and still respond when they are told to stop.
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class InputStreamListener implements Runnable {

    private final InputStream in;
    private final DataConsumer consumer;
    private volatile boolean running = true;
    private byte[] mReadBuffer;

    /**
     * <p>Constructor for InputStreamListener.</p>
     *
     * @param in a {@link InputStream} object.
     * @param consumer a {@link DataConsumer} object.
     */
    public InputStreamListener(InputStream in, DataConsumer consumer) {
        this.in = in;
        this.consumer = consumer;
        this.mReadBuffer = new byte[1024];
    }

    /**
     * <p>start.</p>
     *
     * @param threadName a {@link String} object.
     */
    public void start(String threadName) {
        Thread thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * <p>stop.</p>
     */
    public void stop() {
        running = false;
        synchronized (this) {
            notify();
        }
    }

    /**
     * <p>run.</p>
     */
    public void run() {
        try {
            while(running) {
                try {
                    if (null == this.in) {
                        return;
                    }

                    int size = this.in.read(this.mReadBuffer);
                    if (-1 == size || 0 >= size) {
                        return;
                    }

                    byte[] readBytes = new byte[size];
                    System.arraycopy(this.mReadBuffer, 0, readBytes, 0, size);
                    Log.e("huanghui",bytes2HexStr(readBytes));
                    consumer.data(readBytes, size);
                } catch (IOException e) {
                    consumer.handleIOException(e);
                    if (StringUtils.equals(e.getMessage(), "Stream closed."))
                        break;
                    if (StringUtils.contains(e.getMessage(), "nativeavailable"))
                        break;
                }
            }
        }finally {
            running = false;
        }
    }


    public static String bytes2HexStr(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return "";
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            builder.append(buffer);
        }
        return builder.toString().toUpperCase();
    }
}
