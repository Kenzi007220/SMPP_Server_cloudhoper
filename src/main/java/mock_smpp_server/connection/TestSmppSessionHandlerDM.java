package mock_smpp_server.connection;

import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
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

    private String name;

    public TestSmppSessionHandlerDM() {
    }

    public TestSmppSessionHandlerDM(SmppSession session) {
        this.sessionRef = new WeakReference<SmppSession>(session);
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        SmppSession session = sessionRef.get();

        PduResponse response = pduRequest.createResponse();

//        if (pduRequest.getCommandId() == SmppConstants.CMD_ID_SUBMIT_SM) {
//            SubmitSm mt = (SubmitSm) pduRequest;
//            Address mtSourceAddress = mt.getSourceAddress();
//            Address mtDestinationAddress = mt.getDestAddress();
//            byte dataCoding = mt.getDataCoding();
//            byte[] shortMessage = mt.getShortMessage();
//
//            sendDeliveryReceipt(session, mtDestinationAddress, mtSourceAddress, dataCoding);
//            sendMoMessage(session, mtDestinationAddress, mtSourceAddress, shortMessage, dataCoding);
//        }


//            DeliverSm mt = (DeliverSm) pduRequest;
//            byte dataCoding = mt.getDataCoding();
//            byte[] shortMessage = mt.getShortMessage();

        //sendDeliveryReceipt(session, mtDestinationAddress, mtSourceAddress, dataCoding);
        //sendMoMessage(session);


        // sendMoMessage(session,shortMessage, dataCoding);

        return response;
    }

    private void sendDeliveryReceipt(SmppSession session, Address mtDestinationAddress, Address mtSourceAddress, byte dataCoding) {

//        DeliverSm deliver = new DeliverSm();
//        deliver.setEsmClass(SmppConstants.ESM_CLASS_MT_SMSC_DELIVERY_RECEIPT);
//        deliver.setSourceAddress(mtDestinationAddress);
//        deliver.setDestAddress(mtSourceAddress);
//        deliver.setDataCoding(dataCoding);
//        sendRequestPdu(session, deliver);

    }

    /////////////////// Getters, setters, etc////////////////////////

    public TestSmppSessionHandlerDM setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestSmppSessionHandlerDM setSession(SmppSession session) {
        this.sessionRef = new WeakReference<SmppSession>(session);
        return this;
    }
}
