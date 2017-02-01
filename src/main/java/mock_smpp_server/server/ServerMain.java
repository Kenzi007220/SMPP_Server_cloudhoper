package mock_smpp_server.server;

import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.DeliverSmResp;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.type.Address;
import mock_smpp_server.model.ObjectDeliverSm;
import mock_smpp_server.util.DataCodingToCharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static mock_smpp_server.smpp.DefaultSmppServerHandler.charset;
import static mock_smpp_server.smpp.DefaultSmppServerHandler.listSmppSessions;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ServerMain {

  Logger logger = LoggerFactory.getLogger(ServerMain.class);

  @GetMapping("/send/{client}")
  public String gettingSessionByName(@PathVariable String client) {
    if (!listSmppSessions.containsKey(client))
      return null;

    sendMessageAndShowLogger(listSmppSessions.get(client), null);

    return listSmppSessions.get(client).toString();
  }

  @GetMapping("/")
  public String gettingMainPage() {

    return "It is a main page of mock server";

  }

  @GetMapping("/closeSessions/all")
  @ResponseBody
  public String closeAllSessions() {

    listSmppSessions.values().stream().forEach(smppSession -> smppSession.destroy());
    return "Sossions destroyed";

  }

  @GetMapping("/showSessions/all")
  @ResponseBody
  public List<String> showAllSessions() {

    return listSmppSessions.entrySet().stream()
        .map(v -> v.getKey() + " - " + v.getValue().toString()).collect(Collectors.toList());

  }

  @RequestMapping(value = "/post/all", method = POST)
  @ResponseBody
  public void postAll(@RequestBody ObjectDeliverSm deliverSm) {

    logger.info("String deliverSM is {}", deliverSm.toString());

    listSmppSessions.values().stream()
        .forEach(smppSession -> sendMessageAndShowLogger(smppSession, deliverSm));
  }

  @GetMapping("/send/all")
  @ResponseBody
  public List<String> getAll() {

    listSmppSessions.values().stream()
        .forEach(smppSession -> sendMessageAndShowLogger(smppSession, null)); // if you need to test
                                                                              // DeliverSM
    return listSmppSessions.entrySet().stream()
        .map(v -> v.getKey() + " - " + v.getValue().toString()).collect(Collectors.toList());
  }

  private void sendMessageAndShowLogger(SmppSession smppSession, ObjectDeliverSm deliverSm) {
    logger.info("Hash code of session - " + smppSession.toString().hashCode());

    sendMoMessage(smppSession, deliverSm, 0);
  }

  @GetMapping("/charset/{charsetNum}")
  public String gettingallCheckCharset(@PathVariable Integer charsetNum) {

    charset = DataCodingToCharsetUtil.getCharacterSet((charsetNum));
    return " succesfull changed charset to " + charset;
  }

  private void sendMoMessage(SmppSession session, ObjectDeliverSm objDeliverSm, Integer charset) {

    if (objDeliverSm == null) {
      objDeliverSm = new ObjectDeliverSm("50", "50", "2", "4", "DEFAULT_MESSAGE");
    }

    DeliverSm pdu0 = new DeliverSm();

    pdu0.setSequenceNumber(2); // if client send to me deliverSM
    pdu0.setSourceAddress(new Address((byte) 0x01, (byte) 0x01, objDeliverSm.getSourceAddress()));
    pdu0.setDestAddress(new Address((byte) 0x01, (byte) 0x01, objDeliverSm.getDestAddress()));
    pdu0.setEsmClass((byte) Integer.parseInt(objDeliverSm.getEsmClass()));
    pdu0.setProtocolId((byte) 0x00);
    pdu0.setPriority((byte) 0x00);
    pdu0.setScheduleDeliveryTime(null);
    pdu0.setValidityPeriod(null);
    pdu0.setRegisteredDelivery((byte) Integer.parseInt(objDeliverSm.getRegisteredDelivered()));
    pdu0.setReplaceIfPresent((byte) 0x00);
    pdu0.setDataCoding((byte) 0x00);

    pdu0.setDefaultMsgId((byte) 0x00);

    try {
      pdu0.setShortMessage(objDeliverSm.getShortMessage().getBytes());
    } catch (Exception e) {
      logger.error("Error!", e);
    }
    sendRequestPdu(session, pdu0);
  }

  private void sendRequestPdu(SmppSession session, DeliverSm pdu0) {
    try {
      WindowFuture<Integer, PduRequest, PduResponse> future =
          session.sendRequestPdu(pdu0, 10000, false);
      if (!future.await()) {
        logger.error("Failed to receive deliver_sm_resp within specified time");
      } else if (future.isSuccess()) {
        DeliverSmResp deliverSmResp = (DeliverSmResp) future.getResponse();
        logger.info("deliver_sm_resp: commandStatus [" + deliverSmResp.getCommandStatus() + "="
            + deliverSmResp.getResultMessage() + "]");
      } else {
        logger.error("Failed to properly receive deliver_sm_resp: " + future.getCause());
      }
    } catch (Exception e) {
    }
  }
}


