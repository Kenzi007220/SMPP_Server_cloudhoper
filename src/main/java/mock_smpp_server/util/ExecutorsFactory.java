package mock_smpp_server.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ExecutorsFactory {

  private static Map<String, ExecutorService> executors = new HashMap<>();

  public static ExecutorService getCachedExecutor(String name) {
    return Optional.ofNullable(executors.get(name)).map(a -> a).orElseGet(() -> {
      ExecutorService executor = createcachedThreadPool(name);
      executors.put(name, executor);
      return executor;
    });
  }
    public static ExecutorService createcachedThreadPool(String name) {
        return Executors.newCachedThreadPool(
            new ThreadFactoryBuilder().setNameFormat("mercurio-" + name + "%d").build());
    }
    public static ScheduledThreadPoolExecutor getScheduledExecutor(String name,int maxConcurrentSessions) {
        return (ScheduledThreadPoolExecutor) Optional.ofNullable(executors.get(name)).map(a -> a).orElseGet(() -> {
            ScheduledThreadPoolExecutor scheduledExecutor = createScheduledThreadPool(name, maxConcurrentSessions);

            executors.put(name, scheduledExecutor);
            return scheduledExecutor;
        });
    }

  public static ScheduledThreadPoolExecutor createScheduledThreadPool(String name, int maxConcurrentSessions) {
    return (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(maxConcurrentSessions,
        new ThreadFactoryBuilder().setNameFormat("mercurio-SmppServerSessionWindowMonitorPool").build());

  }

}
