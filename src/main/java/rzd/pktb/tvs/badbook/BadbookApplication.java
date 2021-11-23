package rzd.pktb.tvs.badbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@PropertySource("classpath:application.properties")
public class BadbookApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BadbookApplication.class, args);
    }

    @Bean
    public static DataSource dataSource(@Value("${db.classname}") String classname,
                                        @Value("${db.classname}") String url/*,
                                        /*@Value("${db.username}") String username,
                                        @Value("${db.password}") String password*/) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(classname);
        dataSource.setUrl(url);
        /*dataSource.setUsername(username);
        dataSource.setPassword(password);*/
        return dataSource;
    }

    @Bean
    @Autowired
    public JdbcTemplate jdbcTemplate (DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
