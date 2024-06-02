package uexcel.com.ltts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uexcel.com.ltts.util.Repos;
import uexcel.com.ltts.util.Validation;

@Configuration
public class UtilsConfig {

    @Bean
    public Repos getRepos(){
        return new Repos();
    }

    @Bean
    public Validation getValidation(){
        return new Validation();
    }
}
