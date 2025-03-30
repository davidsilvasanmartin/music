package dev.davidsilva.music.schemaupdate;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlywayRunner implements ApplicationRunner {
    private final Flyway flyway;

    @Override
    public void run(ApplicationArguments args) {
        // Wipe the db first. This removes all data. Useful for development
        flyway.clean();
        flyway.migrate();
    }
}
