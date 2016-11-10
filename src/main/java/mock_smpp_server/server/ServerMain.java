package mock_smpp_server.server;

import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.DeliverSmResp;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.type.Address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static mock_smpp_server.smpp.DefaultSmppServerHandler.listSmppSessions;

@RestController
public class ServerMain {

    Logger logger = LoggerFactory.getLogger(ServerMain.class);

    @GetMapping("/send/{client}")
    public String gettingSessionByBame(@PathVariable String client) {
        if (!listSmppSessions.containsKey(client))
            return null;

        // if you need to test DeliverSM
        //sendMoMessage(listSmppSessions.get(client));

        return listSmppSessions.get(client).toString();
    }

    @GetMapping("/send/all")
    @ResponseBody
    public List<String> gettingall() {

        for (SmppSession session : listSmppSessions.values()) {
            logger.info("Hash code of session - " + session.toString().hashCode());
            sendMoMessage(session);
        }
        return listSmppSessions.entrySet().stream().map(v -> v.getKey() + " - " + v.getValue().toString()).collect(Collectors.toList());
    }

    private void sendMoMessage(SmppSession session) {

        DeliverSm pdu0 = new DeliverSm();

        pdu0.setSequenceNumber(2);
        pdu0.setSourceAddress(new Address((byte) 0x01, (byte) 0x01, "44951361920"));
        pdu0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, "40404"));
        pdu0.setEsmClass((byte) 0x04);
        pdu0.setProtocolId((byte) 0x00);
        pdu0.setPriority((byte) 0x00);
        pdu0.setScheduleDeliveryTime(null);
        pdu0.setValidityPeriod(null);
        pdu0.setRegisteredDelivery((byte) 0x00);
        pdu0.setReplaceIfPresent((byte) 0x00);
        pdu0.setDataCoding((byte) 0x00);
        pdu0.setDefaultMsgId((byte) 0x00);

        try {
            pdu0.setShortMessage("test message".getBytes());
        } catch (Exception e) {
            logger.error("Error!", e);
        }
        sendRequestPdu(session, pdu0);
    }

    private void sendRequestPdu(SmppSession session, DeliverSm pdu0) {
        try {
            WindowFuture<Integer, PduRequest, PduResponse> future = session.sendRequestPdu(pdu0, 10000, false);
            if (!future.await()) {
                logger.error("Failed to receive deliver_sm_resp within specified time");
            } else if (future.isSuccess()) {
                DeliverSmResp deliverSmResp = (DeliverSmResp) future.getResponse();
                logger.info("deliver_sm_resp: commandStatus [" + deliverSmResp.getCommandStatus() + "=" + deliverSmResp.getResultMessage() + "]");
            } else {
                logger.error("Failed to properly receive deliver_sm_resp: " + future.getCause());
            }
        } catch (Exception e) {
        }
    }
}




