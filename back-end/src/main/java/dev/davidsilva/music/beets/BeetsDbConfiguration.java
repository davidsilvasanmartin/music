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

/**
 * Configuration class for the database created by Beets that stores the music
 * <p>
 * Note that database type (sqlite) is hardcoded because it will not change
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"dev.davidsilva.music.song", "dev.davidsilva.music.beets"},
        entityManagerFactoryRef = "beetsDbEntityManagerFactory",
        /*
         * TODO the following seems like it should be beetsDbTransactionManager. This could be a bug, but when replacing
         *  it by the beets one, the app does not work. Need to fix this
        */
        transactionManagerRef = "beetsDbTransactionManager"
)
@RequiredArgsConstructor
public class BeetsDbConfiguration {
    private final Environment env;

    @Bean(name = "beetsDbDataSource")
    public DataSource beetsDbDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(env.getProperty("beets-db.url"));
        return dataSource;
    }

    @Bean(name = "beetsDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("beetsDbDataSource") DataSource dataSource) {
        HashMap<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        return builder.dataSource(dataSource).packages("dev.davidsilva.music.song", "dev.davidsilva.music.beets").properties(propertiesMap).build();
    }

    @Bean(name = "beetsDbTransactionManager")
    public JpaTransactionManager beetsDbTransactionManager(@Qualifier("beetsDbEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        return new JpaTransactionManager(Objects.requireNonNull(factory.getObject()));
    }
}
