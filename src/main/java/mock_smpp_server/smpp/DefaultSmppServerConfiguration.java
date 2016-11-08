package mock_smpp_server.smpp;

import com.cloudhopper.smpp.SmppServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultSmppServerConfiguration extends SmppServerConfiguration {

        private static final Logger logger = LoggerFactory.getLogger(DefaultSmppServerConfiguration.class);

        public DefaultSmppServerConfiguration() {
            this.setPort(2776);
            this.setMaxConnectionSize(10);
            this.setNonBlockingSocketsEnabled(true);
            this.setDefaultRequestExpiryTimeout(30000);
            this.setDefaultWindowMonitorInterval(15000);
            this.setDefaultWindowSize(5);
            this.setDefaultWindowWaitTimeout(this.getDefaultRequestExpiryTimeout());
            this.setDefaultSessionCountersEnabled(true);
            this.setJmxEnabled(true);
        }
}
