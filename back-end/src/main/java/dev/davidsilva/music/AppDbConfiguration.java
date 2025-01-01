package dev.davidsilva.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
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
        basePackages = {"dev.davidsilva.music.user"},
        entityManagerFactoryRef = "appDbEntityManagerFactory",
        transactionManagerRef = "appDbTransactionManager"
)
public class AppDbConfiguration {
    @Autowired
    Environment env;

    @Bean(name = "appDb")
    @Primary
    public DataSource appDbDataSource() throws IOException, ClassNotFoundException {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // See the following if we need to get rid of the warning https://www.baeldung.com/configuration-properties-in-spring-boot
        dataSource.setDriverClassName("org.sqlite.JDBC");
        String appDbUrl = new ClassPathResource("db/app.db").getURL().toString();
        dataSource.setUrl("jdbc:sqlite:" + appDbUrl);
        return dataSource;
    }

    @Bean(name = "appDbEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("appDb") DataSource dataSource) {
        HashMap<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        return builder.dataSource(dataSource).packages("dev.davidsilva.music.user").properties(propertiesMap).build();
    }

    @Bean(name = "appDbTransactionManager")
    @Primary
    public DataSourceTransactionManager appDbTransactionManager(@Qualifier("appDb") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
