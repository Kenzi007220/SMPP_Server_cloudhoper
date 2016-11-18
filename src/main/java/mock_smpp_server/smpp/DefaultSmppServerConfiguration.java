package mock_smpp_server.smpp;

import com.cloudhopper.smpp.SmppServerConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultSmppServerConfiguration extends SmppServerConfiguration {

        private static final Logger logger = LoggerFactory.getLogger(DefaultSmppServerConfiguration.class);

        public DefaultSmppServerConfiguration() {
            setPort(2776);
            setMaxConnectionSize(10);
            setNonBlockingSocketsEnabled(true);
            setDefaultRequestExpiryTimeout(30000);
            setDefaultWindowMonitorInterval(15000);
            setDefaultWindowSize(5);
            setDefaultWindowWaitTimeout(getDefaultRequestExpiryTimeout());
            setDefaultSessionCountersEnabled(true);
            setJmxEnabled(true);
        }
}
