package com.licheedev.modbus4android;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author huanghui
 * @date on 2022/7/8
 * @title
 * @describe
 */
public abstract class SerialPortReadThread extends Thread {
    private static final String TAG = SerialPortReadThread.class.getSimpleName();
    private InputStream mInputStream;
    private byte[] mReadBuffer;

    public abstract void onDataReceived(byte[] var1);

    public SerialPortReadThread(InputStream inputStream) {
        this.mInputStream = inputStream;
        this.mReadBuffer = new byte[1024];
    }

    public void run() {
        super.run();

        while(!this.isInterrupted()) {
            try {
                if (null == this.mInputStream) {
                    return;
                }

                Log.i(TAG, "run: ");
                int size = this.mInputStream.read(this.mReadBuffer);
                if (-1 == size || 0 >= size) {
                    return;
                }

                byte[] readBytes = new byte[size];
                System.arraycopy(this.mReadBuffer, 0, readBytes, 0, size);
                Log.i(TAG, "run: readBytes = " + new String(readBytes));
                this.onDataReceived(readBytes);
            } catch (IOException var3) {
                var3.printStackTrace();
                return;
            }
        }

    }

    public synchronized void start() {
        super.start();
    }

    public void release() {
        this.interrupt();
        if (null != this.mInputStream) {
            try {
                this.mInputStream.close();
                this.mInputStream = null;
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }
}

