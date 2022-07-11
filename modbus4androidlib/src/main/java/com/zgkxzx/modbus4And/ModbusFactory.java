/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.zgkxzx.modbus4And;

import com.zgkxzx.modbus4And.base.ModbusUtils;
import com.zgkxzx.modbus4And.code.RegisterRange;
import com.zgkxzx.modbus4And.exception.ModbusIdException;
import com.zgkxzx.modbus4And.exception.ModbusTransportException;
import com.zgkxzx.modbus4And.ip.IpParameters;
import com.zgkxzx.modbus4And.ip.listener.TcpListener;
import com.zgkxzx.modbus4And.ip.tcp.TcpMaster;
import com.zgkxzx.modbus4And.ip.tcp.TcpSlave;
import com.zgkxzx.modbus4And.ip.udp.UdpMaster;
import com.zgkxzx.modbus4And.ip.udp.UdpSlave;
import com.zgkxzx.modbus4And.msg.ModbusRequest;
import com.zgkxzx.modbus4And.msg.ReadCoilsRequest;
import com.zgkxzx.modbus4And.msg.ReadDiscreteInputsRequest;
import com.zgkxzx.modbus4And.msg.ReadHoldingRegistersRequest;
import com.zgkxzx.modbus4And.msg.ReadInputRegistersRequest;
import com.zgkxzx.modbus4And.serial.SerialPortWrapper;
import com.zgkxzx.modbus4And.serial.ascii.AsciiMaster;
import com.zgkxzx.modbus4And.serial.ascii.AsciiSlave;
import com.zgkxzx.modbus4And.serial.rtu.RtuMaster;
import com.zgkxzx.modbus4And.serial.rtu.RtuSlave;

public class ModbusFactory {
    //
    // Modbus masters
    //
    public ModbusMaster createRtuMaster(SerialPortWrapper wrapper) {
        return new RtuMaster(wrapper);
    }

    /**
     * Create an RTU Master with specific message frame timing
     * 
     * @param params
     * @param characterSpacingNs
     * @param messageFrameSpacingNs
     * @return
     */
    public ModbusMaster createRtuMaster(SerialPortWrapper wrapper, long characterSpacingNs, long messageFrameSpacingNs) {
        return new RtuMaster(wrapper, characterSpacingNs, messageFrameSpacingNs);
    }
    
    public ModbusMaster createAsciiMaster(SerialPortWrapper wrapper) {
        return new AsciiMaster(wrapper);
    }

    public ModbusMaster createTcpMaster(IpParameters params, boolean keepAlive) {
        return new TcpMaster(params, keepAlive);
    }

    public ModbusMaster createUdpMaster(IpParameters params) {
        return new UdpMaster(params);
    }

    public ModbusMaster createTcpListener(IpParameters params) {
        return new TcpListener(params);
    }

    //
    // Modbus slaves
    //
    public ModbusSlaveSet createRtuSlave(SerialPortWrapper wrapper) {
        return new RtuSlave(wrapper);
    }

    public ModbusSlaveSet createAsciiSlave(SerialPortWrapper wrapper) {
        return new AsciiSlave(wrapper);
    }

    public ModbusSlaveSet createTcpSlave(boolean encapsulated) {
        return new TcpSlave(encapsulated);
    }

    public ModbusSlaveSet createUdpSlave(boolean encapsulated) {
        return new UdpSlave(encapsulated);
    }

    //
    // Modbus requests
    //
    public ModbusRequest createReadRequest(int slaveId, int range, int offset, int length)
            throws ModbusTransportException, ModbusIdException {
        ModbusUtils.validateRegisterRange(range);

        if (range == RegisterRange.COIL_STATUS)
            return new ReadCoilsRequest(slaveId, offset, length);

        if (range == RegisterRange.INPUT_STATUS)
            return new ReadDiscreteInputsRequest(slaveId, offset, length);

        if (range == RegisterRange.INPUT_REGISTER)
            return new ReadInputRegistersRequest(slaveId, offset, length);

        return new ReadHoldingRegistersRequest(slaveId, offset, length);
    }
}
