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
package com.serotonin.modbus4j.msg;

import com.serotonin.modbus4j.Modbus;
import com.serotonin.modbus4j.ProcessImage;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

/**
 * <p>WriteCoilRequest class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class WriteCoilRequest extends ModbusRequest {
    private int writeOffset;
    private boolean writeValue;

    /**
     * <p>Constructor for WriteCoilRequest.</p>
     *
     * @param slaveId a int.
     * @param writeOffset a int.
     * @param writeValue a boolean.
     * @throws ModbusTransportException if any.
     */
    public WriteCoilRequest(int slaveId, int writeOffset, boolean writeValue) throws ModbusTransportException {
        super(slaveId);
        this.writeOffset = writeOffset;
        this.writeValue = writeValue;
    }

    /** {@inheritDoc} */
    @Override
    public void validate(Modbus modbus) throws ModbusTransportException {
        ModbusUtils.validateOffset(writeOffset);
    }

    WriteCoilRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, writeOffset);
        ModbusUtils.pushShort(queue, writeValue ? 0xff00 : 0);
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        processImage.writeCoil(writeOffset, writeValue);
        return new WriteCoilResponse(slaveId, writeOffset, writeValue);
    }

    /** {@inheritDoc} */
    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_COIL;
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new WriteCoilResponse(slaveId);
    }

    /** {@inheritDoc} */
    @Override
    protected void readRequest(ByteQueue queue) {
        writeOffset = ModbusUtils.popUnsignedShort(queue);
        writeValue = ModbusUtils.popUnsignedShort(queue) == 0xff00;
    }
}
