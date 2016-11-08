package mock_smpp_server.connection;

import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;

@Component
@Scope("prototype")
public class TestSmppSessionHandler extends DefaultSmppSessionHandler {

    private WeakReference<SmppSession> sessionRef;

    private static final Logger log = LoggerFactory.getLogger(TestSmppSessionHandler.class);

    public TestSmppSessionHandler setSession(SmppSession session) {
        this.sessionRef = new WeakReference<SmppSession>(session);
        return this;
    }

    public TestSmppSessionHandler() {
    }

    public TestSmppSessionHandler(SmppSession session) {
        this.sessionRef = new WeakReference<SmppSession>(session);
    }

    @Override
    public PduResponse firePduRequestReceived(PduRequest pduRequest) {
        SmppSession session = sessionRef.get();

        log.info("Session object ->>> " + this);

        // mimic how long processing could take on a slower smsc
        try {
            //Thread.sleep(50);
        } catch (Exception e) {
        }

        return pduRequest.createResponse();
    }






    //////////////////  EQUALS, HASHCODE .........  ///////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestSmppSessionHandler that = (TestSmppSessionHandler) o;

        return sessionRef != null ? sessionRef.equals(that.sessionRef) : that.sessionRef == null;
    }

    @Override
    public int hashCode() {
        return sessionRef != null ? sessionRef.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TestSmppSessionHandler{" +
                "sessionRef=" + sessionRef +
                '}';
    }
}

