package mock_smpp_server.smpp;

import com.cloudhopper.smpp.SmppServerHandler;
import com.cloudhopper.smpp.SmppServerSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.BaseBind;
import com.cloudhopper.smpp.pdu.BaseBindResp;
import com.cloudhopper.smpp.type.SmppProcessingException;
import mock_smpp_server.connection.TestSmppSessionHandler;
import mock_smpp_server.connection.TestSmppSessionHandlerDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public  class DefaultSmppServerHandler implements SmppServerHandler, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSmppServerHandler.class);

    ApplicationContext applicationContext;

    @Override
    public void sessionBindRequested(Long aLong, SmppSessionConfiguration smppSessionConfiguration, BaseBind baseBind) throws SmppProcessingException {
        logger.info("The bind was requested!!!!!!!!!!");
        smppSessionConfiguration.setName("Application.SMPP." + smppSessionConfiguration.getSystemId());

    }

    @Override
    public void sessionCreated(Long aLong, SmppServerSession smppServerSession, BaseBindResp baseBindResp) throws SmppProcessingException {
        logger.info("The bind was created!!!!!!!!!!");

        TestSmppSessionHandler testSmppSessionHandler = (TestSmppSessionHandler)applicationContext.getBean("testSmppSessionHandler");
        logger.info("Hashcode of this session testSmppSessionHandler -" + testSmppSessionHandler.toString().hashCode() + "; smppServerSession - " + smppServerSession.toString().hashCode());
        smppServerSession.serverReady(testSmppSessionHandler.setSession(smppServerSession));

        TestSmppSessionHandlerDM testSmppSessionHandlerDM = (TestSmppSessionHandlerDM)applicationContext.getBean("testSmppSessionHandlerDM");
        logger.info("Hashcode of this session testSmppSessionHandlerDM -" + testSmppSessionHandlerDM.toString().hashCode() + "; smppServerSession - " + smppServerSession.toString().hashCode());
        smppServerSession.serverReady(testSmppSessionHandlerDM.setSession(smppServerSession));

    }

    @Override
    public void sessionDestroyed(Long aLong, SmppServerSession smppServerSession) {
        logger.info("Session destroyed: {}", smppServerSession);
        // print out final stats
        if (smppServerSession.hasCounters()) {
            logger.info(" final session rx-submitSM: {}", smppServerSession.getCounters().getRxSubmitSM());
        }

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    }
}
