package dev.davidsilva.music.beets;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration class for the database created by Beets that stores the music
 * <p>
 * Note that database type (sqlite) is hardcoded because it will not change
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "dev.davidsilva.music.beets.album",
                "dev.davidsilva.music.beets.item"
        },
        entityManagerFactoryRef = "beetsDbEntityManagerFactory",
        transactionManagerRef = "beetsDbTransactionManager"
)
@RequiredArgsConstructor
public class BeetsDbConfiguration {
    private final Environment env;

    @Bean(name = "beetsDbDataSource")
    public DataSource beetsDbDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        // TODO when I run the import albums job, there is some console error about the SQLite connection not being readonly:
        //  Cannot change read-only flag after establishing a connection. Use SQLiteConfig#setReadOnly and SQLiteConfig.createConnection()
        // String readonlyUrl = env.getProperty("beets-db.url") + "?mode=ro";
        dataSource.setUrl(env.getProperty("beets-db.url"));
        Properties connectionProps = new Properties();
        connectionProps.setProperty("ReadOnly", "true");
        dataSource.setConnectionProperties(connectionProps);
        return dataSource;
    }

    @Bean(name = "beetsDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean beetsEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("beetsDbDataSource") DataSource dataSource) {
        HashMap<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");

        // Configure Hibernate for read-only operations
        propertiesMap.put("hibernate.connection.readOnly", "true");
        propertiesMap.put("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_HOLD");
        propertiesMap.put("jakarta.persistence.lock.timeout", "0");

        return builder.dataSource(dataSource).packages(
                "dev.davidsilva.music.beets.album",
                "dev.davidsilva.music.beets.item"
        ).persistenceUnit("beetsPersistenceUnit").properties(propertiesMap).build();
    }

    @Bean(name = "beetsDbTransactionManager")
    public JpaTransactionManager beetsDbTransactionManager(@Qualifier("beetsDbEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(Objects.requireNonNull(factory.getObject()));
    }
}
