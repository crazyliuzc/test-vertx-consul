package test;

import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.KeyValue;
import io.vertx.ext.consul.Watch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by crazyliu on 2020/9/24.
 */
public class TestConsul {
    final private static Logger log = LoggerFactory.getLogger(TestConsul.class);
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        ConsulClientOptions options = new ConsulClientOptions()
                .setHost("192.168.6.107")
                .setPort(8500)
                .setTimeout(50000)
//                        .setIdleTimeoutUnit(TimeUnit.SECONDS)
//                        .setIdleTimeout(1)
//                .setMaxWaitQueueSize(-1)
//                        .setMaxPoolSize(500)
//                .setTcpKeepAlive(true)
                .setLogActivity(false);
        for (int i = 0; i < 300; i++) {
            final int finalI = i;
            Watch<KeyValue> watch = Watch.key("test123"+i, vertx, options);
            watch.setHandler(keyValueWatchResult -> {
                if (keyValueWatchResult.succeeded()) {
                    KeyValue keyValue = keyValueWatchResult.nextResult();
                    log.info("{} Monitor timer:{}",finalI,keyValue.toJson().encodePrettily());
                } else {
                    log.error(finalI+" watch error" ,keyValueWatchResult.cause());
                    watch.stop();
                }
            }).start();
        }
    }
}
