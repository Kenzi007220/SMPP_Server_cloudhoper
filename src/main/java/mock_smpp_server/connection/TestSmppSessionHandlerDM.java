package mock_smpp_server.connection;

import com.cloudhopper.commons.charset.Charset;
import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.BaseSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.type.Address;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;

import static com.cloudhopper.smpp.SmppConstants.CMD_ID_DATA_SM;
import static com.cloudhopper.smpp.SmppConstants.CMD_ID_DELIVER_SM;
import static com.cloudhopper.smpp.SmppConstants.CMD_ID_ENQUIRE_LINK;
import static com.cloudhopper.smpp.SmppConstants.CMD_ID_SUBMIT_SM;
import static mock_smpp_server.smpp.DefaultSmppServerHandler.charset;

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
        // logger.info(pduRequest.getCommandId());

        // decode shortMessage
        if (pduRequest.getCommandId() == CMD_ID_SUBMIT_SM ||
                pduRequest.getCommandId() == CMD_ID_DELIVER_SM ||
                pduRequest.getCommandId() == CMD_ID_DATA_SM ||
                pduRequest.getCommandId() == CMD_ID_ENQUIRE_LINK) {

            BaseSm requestWithMessage = (BaseSm) pduRequest;

            byte[] byteMessage = requestWithMessage.getShortMessage();
            String message = CharsetUtil.decode(byteMessage, charset);
            System.out.println(charset);
            logger.info("You recieve a messege ----> " + message + " <---- , dest adress - " +
                    requestWithMessage.getDestAddress() + ", delivery adress - " + requestWithMessage.getSourceAddress() +
                    " charset - " + charset);
        }

        SmppSession session = sessionRef.get();

        PduResponse response = pduRequest.createResponse();

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

    public static Charset getCharacterSet(int code) {

        switch (code) {
            case 0:
                return CharsetUtil.CHARSET_ISO_8859_1;
            case 4:
                return CharsetUtil.CHARSET_GSM8;
            case 8:
                return CharsetUtil.CHARSET_UCS_2;
            default:
                return CharsetUtil.CHARSET_GSM;
        }
    }
}



