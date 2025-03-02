package dev.davidsilva.music.security.session;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * This class is needed as per https://docs.spring.io/spring-session/reference/getting-started/using-redis.html#_initializing_the_configuration_into_the_java_servlet_container
 */
public class SessionInitializer extends AbstractHttpSessionApplicationInitializer {
    public SessionInitializer() {
        super(RedisConfiguration.class);
    }
}
