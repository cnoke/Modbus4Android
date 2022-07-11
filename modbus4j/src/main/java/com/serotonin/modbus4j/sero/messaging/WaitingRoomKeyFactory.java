package com.serotonin.modbus4j.sero.messaging;


/**
 * <p>WaitingRoomKeyFactory interface.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public interface WaitingRoomKeyFactory {
    /**
     * <p>createWaitingRoomKey.</p>
     *
     * @param request a {@link OutgoingRequestMessage} object.
     * @return a {@link WaitingRoomKey} object.
     */
    WaitingRoomKey createWaitingRoomKey(OutgoingRequestMessage request);

    /**
     * <p>createWaitingRoomKey.</p>
     *
     * @param response a {@link IncomingResponseMessage} object.
     * @return a {@link WaitingRoomKey} object.
     */
    WaitingRoomKey createWaitingRoomKey(IncomingResponseMessage response);
}
