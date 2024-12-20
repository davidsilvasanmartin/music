package dev.davidsilva.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"dev.davidsilva.music.album", "dev.davidsilva.music.song"},
        entityManagerFactoryRef = "beetsDbEntityManagerFactory",
        transactionManagerRef = "appDbTransactionManager"
)
public class BeetsDbConfiguration {
    @Autowired
    Environment env;

    @Bean(name = "beetsDb")
    public DataSource beetsDbDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.data-source.driver-class-name")));
        dataSource.setUrl(env.getProperty("custom-data-source.url"));
        return dataSource;
    }

    @Bean(name = "beetsDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("beetsDb") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("dev.davidsilva.music.album", "dev.davidsilva.music.song").build();
    }

    @Bean(name = "beetsDbTransactionManager")
    public DataSourceTransactionManager beetsDbTransactionManager(@Qualifier("beetsDb") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
