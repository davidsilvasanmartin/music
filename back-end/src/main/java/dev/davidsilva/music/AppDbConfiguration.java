package dev.davidsilva.music;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;

/**
 * Configuration class for the primary application database
 * <p>
 * Note that database type (sqlite) as well as its path are hardcoded because they will not change
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"dev.davidsilva.music.user", "dev.davidsilva.music.audit"},
        entityManagerFactoryRef = "appDbEntityManagerFactory",
        transactionManagerRef = "appDbTransactionManager"
)
@RequiredArgsConstructor
public class AppDbConfiguration {
    private final Environment env;

    @Bean(name = "appDbDataSource")
    @Primary
    public DataSource appDbDataSource() throws IOException, ClassNotFoundException {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // See the following if we need to get rid of the warning https://www.baeldung.com/configuration-properties-in-spring-boot
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(env.getProperty("app-db.url"));
        return dataSource;
    }

    @Bean(name = "appDbEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("appDbDataSource") DataSource dataSource) {
        HashMap<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        return builder.dataSource(dataSource).packages("dev.davidsilva.music.user", "dev.davidsilva.music.audit").properties(propertiesMap).build();
    }

    @Bean(name = "appDbTransactionManager")
    @Primary
    public DataSourceTransactionManager appDbTransactionManager(@Qualifier("appDbDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
