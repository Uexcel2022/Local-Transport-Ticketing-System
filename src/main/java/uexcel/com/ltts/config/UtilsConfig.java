package uexcel.com.ltts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uexcel.com.ltts.util.RepositoryService;
import uexcel.com.ltts.util.Validation;

@Configuration
public class UtilsConfig {

    @Bean
    public RepositoryService getRepos(){
        return new RepositoryService();
    }

    @Bean
    public Validation getValidation(){
        return new Validation();
    }
}
