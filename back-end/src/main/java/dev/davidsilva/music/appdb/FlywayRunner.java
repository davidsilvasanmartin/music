package dev.davidsilva.music.appdb;

import lombok.AllArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FlywayRunner implements ApplicationRunner {
    private final Flyway flyway;

    @Override
    public void run(ApplicationArguments args) {
        // Wipe the db first. This removes all data
        // flyway.clean();
        flyway.migrate();
    }
}
