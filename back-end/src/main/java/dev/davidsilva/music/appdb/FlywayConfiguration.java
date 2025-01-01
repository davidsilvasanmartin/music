package dev.davidsilva.music.appdb;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {
    private final DataSource dataSource;

    public FlywayConfiguration(@Qualifier("appDbDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public Flyway flyway() {
        return Flyway
                .configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .table("migrations")
                .cleanDisabled(false)
                .load();
    }
}
