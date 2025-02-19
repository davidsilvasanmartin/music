package dev.davidsilva.music;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


/**
 * TODO
 * <p>
 * This is Claude on the "log not working" incident
 * You're welcome! Yes, the switch from `DataSourceTransactionManager` to `JpaTransactionManager` was crucial here since you're using Spring Data JPA repositories.
 * Just for future reference, this is a common gotcha when working with Spring transactions. Here's a quick rule of thumb:
 * - Use `JpaTransactionManager` when working with JPA/Hibernate (like with `@Repository` and Spring Data JPA)
 * - Use `DataSourceTransactionManager` when working with plain JDBC or Spring's JdbcTemplate
 * - If you mix both JPA and JDBC operations, use `JpaTransactionManager` as it can handle both
 * <p>
 * TODO I need to propagate this change to BeetsDbConfiguration
 */

/**
 * Configuration class for the primary application database
 * <p>
 * Note that database type (sqlite) as well as its path are hardcoded because they will not change
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"dev.davidsilva.music.audit", "dev.davidsilva.music.security"},
        entityManagerFactoryRef = "appDbEntityManagerFactory",
        transactionManagerRef = "appDbTransactionManager"
)
@RequiredArgsConstructor
public class AppDbConfiguration {
    private final Environment env;

    @Bean(name = "appDbDataSource")
    @Primary
    public DataSource appDbDataSource() throws IOException, ClassNotFoundException {
        final SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(env.getProperty("app-db.url"));
        // The following properties are supposed to help with proper transaction support
        // Also, see this https://stackoverflow.com/questions/12605651/sqlite-and-foreign-key-support
        SQLiteConfig config = dataSource.getConfig();
        config.enforceForeignKeys(true);
        config.setJournalMode(SQLiteConfig.JournalMode.WAL);
        config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
        dataSource.setConfig(config);
        return dataSource;
    }

    @Bean(name = "appDbEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("appDbDataSource") DataSource dataSource) {
        HashMap<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        return builder.dataSource(dataSource).packages("dev.davidsilva.music.audit", "dev.davidsilva.music.security").properties(propertiesMap).build();
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
