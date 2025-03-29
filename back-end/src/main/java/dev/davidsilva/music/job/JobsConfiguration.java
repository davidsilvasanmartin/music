package dev.davidsilva.music.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class JobsConfiguration extends DefaultBatchConfiguration {
    private final DataSource dataSource;
    private final JpaTransactionManager transactionManager;

    public JobsConfiguration(@Qualifier("appDbDataSource") DataSource dataSource, @Qualifier("appDbTransactionManager") JpaTransactionManager transactionManager) {
        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    protected PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Bean
    @Primary
    public BatchProperties batchProperties() {
        BatchProperties properties = new BatchProperties();
        Jdbc jdbc = properties.getJdbc();

        // Hardcode the table prefix to match our SQL migration
        jdbc.setTablePrefix("BATCH_");

        // Disable automatic schema initialization
        jdbc.setInitializeSchema(DatabaseInitializationMode.NEVER);

        return properties;
    }
}
