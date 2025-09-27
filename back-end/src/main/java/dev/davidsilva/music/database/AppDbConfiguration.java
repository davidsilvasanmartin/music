package dev.davidsilva.music.database;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;

/**
 * Configuration class for the primary application database
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
                "dev.davidsilva.music.audit",
                "dev.davidsilva.music.security",
                "dev.davidsilva.music.album",
                "dev.davidsilva.music.genre",
                "dev.davidsilva.music.song",
                "dev.davidsilva.music.artist",
                "dev.davidsilva.music.playlist"
        },
        entityManagerFactoryRef = "appDbEntityManagerFactory",
        transactionManagerRef = "appDbTransactionManager"
)
@RequiredArgsConstructor
public class AppDbConfiguration {
    private final Environment env;

    @Bean(name = "appDbDataSource")
    @Primary
    public DataSource appDbDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("app-db.url"))
                .username(env.getProperty("app-db.username"))
                .password(env.getProperty("app-db.password"))
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "appDbEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("appDbDataSource") DataSource dataSource) {
        HashMap<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return builder.dataSource(dataSource)
                .packages(
                        "dev.davidsilva.music.audit",
                        "dev.davidsilva.music.security",
                        "dev.davidsilva.music.album",
                        "dev.davidsilva.music.genre",
                        "dev.davidsilva.music.song",
                        "dev.davidsilva.music.artist",
                        "dev.davidsilva.music.playlist"
                )
                .persistenceUnit("appPersistenceUnit")
                .properties(propertiesMap)
                .build();
    }


    @Bean(name = "appDbEntityManager")
    @Primary
    public EntityManager appDbEntityManager(@Qualifier("appDbEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return Objects.requireNonNull(factory.getObject()).createEntityManager();
    }

    @Bean(name = "appDbTransactionManager")
    @Primary
    public JpaTransactionManager appDbTransactionManager(@Qualifier("appDbEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(Objects.requireNonNull(factory.getObject()));
    }
}
