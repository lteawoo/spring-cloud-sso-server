package kr.taeu.gateway.filters;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {
        super(GlobalFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(GlobalFilter.Config config) {
        return (exchange, chain) -> {
            log.info("GlobalFilter baseMessage: {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("GlobalFilter Start: {}", exchange.getRequest());
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("GlobalFilter End: {}", exchange.getResponse());
                }
            }));
        };
    }

    @Getter
    public static class Config {
        private final String baseMessage;
        private final boolean preLogger;
        private final boolean postLogger;

        public Config(final String baseMessage,
                      final boolean preLogger,
                      final boolean postLogger) {
            this.baseMessage = baseMessage;
            this.preLogger = preLogger;
            this.postLogger = postLogger;
        }
    }
}
