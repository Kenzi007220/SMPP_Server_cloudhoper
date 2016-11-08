package mock_smpp_server.connection;

import com.cloudhopper.commons.util.windowing.WindowFuture;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.*;
import com.cloudhopper.smpp.type.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;

@Component
@Scope("prototype")
public class TestSmppSessionHandlerDM extends DefaultSmppSessionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TestSmppSessionHandlerDM.class);
    private WeakReference<SmppSession> sessionRef;

    public TestSmppSessionHandlerDM setSession(SmppSession session) {
        this.sessionRef = new WeakReference<SmppSession>(session);
        return this;
    }

    public TestSmppSessionHandlerDM() {}

    public TestSmppSessionHandlerDM(SmppSession session) {
        this.sessionRef = new WeakReference<SmppSession>(session);
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        SmppSession session = sessionRef.get();

        PduResponse response = pduRequest.createResponse();

        if (pduRequest.getCommandId() == SmppConstants.CMD_ID_SUBMIT_SM) {
            DeliverSm mt = (DeliverSm) pduRequest;
            Address mtSourceAddress = mt.getSourceAddress();
            Address mtDestinationAddress = mt.getDestAddress();
            byte dataCoding = mt.getDataCoding();
            byte[] shortMessage = mt.getShortMessage();

            sendDeliveryReceipt(session, mtDestinationAddress, mtSourceAddress, dataCoding);
            sendMoMessage(session, mtDestinationAddress, mtSourceAddress, shortMessage, dataCoding);
        }

        return response;
    }

    private void sendDeliveryReceipt(SmppSession session, Address mtDestinationAddress, Address mtSourceAddress, byte dataCoding) {


          DeliverSm deliver = new DeliverSm();
//        deliver.setEsmClass(SmppConstants.ESM_CLASS_MT_SMSC_DELIVERY_RECEIPT);
//        deliver.setSourceAddress(mtDestinationAddress);
//        deliver.setDestAddress(mtSourceAddress);
//        deliver.setDataCoding(dataCoding);
          sendRequestPdu(session, deliver);


    }

    private void sendMoMessage(SmppSession session, Address moSourceAddress, Address moDestinationAddress, byte[] textBytes, byte dataCoding) {

        DeliverSm deliver = new DeliverSm();

        deliver.setSourceAddress(moSourceAddress);
        deliver.setDestAddress(moDestinationAddress);
        deliver.setDataCoding(dataCoding);
        try {
            deliver.setShortMessage(textBytes);
        } catch (Exception e) {
            logger.error("Error!", e);
        }

        sendRequestPdu(session, deliver);
    }

    private void sendRequestPdu(SmppSession session, DeliverSm deliver) {
        try {
            WindowFuture<Integer, PduRequest, PduResponse> future = session.sendRequestPdu(deliver, 10000, false);
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
