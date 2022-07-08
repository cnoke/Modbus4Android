package com.licheedev.modbus4android;

/**
 * @author huanghui
 * @date on 2022/7/8
 * @title
 * @describe
 */
public interface OnSerialPortDataListener {
    void onDataReceived(byte[] var1);

    void onDataSent(byte[] var1);
}
