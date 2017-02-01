package mock_smpp_server;

import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.type.SmppChannelException;
import mock_smpp_server.smpp.DefaultSmppServerConfiguration;
import mock_smpp_server.smpp.DefaultSmppServerHandler;
import mock_smpp_server.util.ExecutorsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class StartServ {
  private ExecutorService rateLimitThreadPoolExecutor =
      ExecutorsFactory.getCachedExecutor("rate-limit-thread-pool"); //
  private ScheduledThreadPoolExecutor rateLimitScheduledThreadPoolExecutor =
      ExecutorsFactory.getScheduledExecutor("mercurio-SmppServerSessionWindowMonitorPool", 999);

  @Autowired
  DefaultSmppServerHandler defaultSmppServerHandler;

  @Autowired
  DefaultSmppServerConfiguration defaultSmppServerConfiguration;

  private static final Logger logger = LoggerFactory.getLogger(StartServ.class);

  ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

  ScheduledThreadPoolExecutor monitorExecutor =
      (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, new ThreadFactory() {
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
    DefaultSmppServer server =
        new DefaultSmppServer(defaultSmppServerConfiguration, defaultSmppServerHandler,
            rateLimitThreadPoolExecutor, rateLimitScheduledThreadPoolExecutor);
    try {
      server.start();
    } catch (SmppChannelException e) {
      logger.error("Error starting server...", e);
    }
  }
}
