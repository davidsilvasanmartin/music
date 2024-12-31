package dev.davidsilva.music;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

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
    public DataSource appDbDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.data-source.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.data-source.url"));
        return dataSource;
    }

    @Bean(name = "appDbEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("appDb") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("dev.davidsilva.music.user").build();
    }

    @Bean(name = "appDbTransactionManager")
    @Primary
    public DataSourceTransactionManager appDbTransactionManager(@Qualifier("appDb") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

//    @Bean(name = "chainedTransactionManager")
//    @Autowired
//    public ChainedTransactionManager chainedTransactionManager(@Qualifier("appDbTransactionManager") DataSourceTransactionManager appDbTransactionManager, @Qualifier("beetsDbTransactionManager") DataSourceTransactionManager beetsDbTransactionManager) {
//        return new ChainedTransactionManager(appDbTransactionManager, beetsDbTransactionManager);
//    }
//
}
