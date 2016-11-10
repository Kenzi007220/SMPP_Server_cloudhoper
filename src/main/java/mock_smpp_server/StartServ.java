package mock_smpp_server;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.type.SmppChannelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import mock_smpp_server.smpp.DefaultSmppServerConfiguration;
import mock_smpp_server.smpp.DefaultSmppServerHandler;

@SpringBootApplication
public class StartServ {

    @Autowired
    DefaultSmppServerHandler defaultSmppServerHandler;

    @Autowired
    DefaultSmppServerConfiguration defaultSmppServerConfiguration;

    private static final Logger logger = LoggerFactory.getLogger(StartServ.class);

    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    ScheduledThreadPoolExecutor monitorExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
        private AtomicInteger sequence = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("SmppServerSessionWindowMonitorPool-" + sequence.getAndIncrement());
            return t;
        }
    });

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StartServ.class, args);
    }

    @PostConstruct
    public void defaultSmppServer() {
        DefaultSmppServer server = new DefaultSmppServer(defaultSmppServerConfiguration, defaultSmppServerHandler, executor, monitorExecutor);
        try {
            server.start();
        } catch (SmppChannelException e) {
            logger.error("Error starting server...", e);
        }
    }

}
