package demo.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootTest
@SpringBootConfiguration
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Profile("test")
@EnableTransactionManagement
@ComponentScan(basePackages = {"demo"})
@EnableJpaRepositories(basePackages = {"demo.repository", "demo.model"})
public @interface TestApplicationConfig {
}
