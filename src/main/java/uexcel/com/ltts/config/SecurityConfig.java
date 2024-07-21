package uexcel.com.ltts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uexcel.com.ltts.filter.JwtAuthenticationFilter;
import uexcel.com.ltts.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
      return   http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req->req.requestMatchers(
                        "api/v1/client-login","api/v1/client-signup",
                                "api/v1/verify-email","api/v1/fresh-token-vfy-email",
                                "api/v1/fresh-token-chg-email","api/v1/fresh-token-chg-pwd",
                                "api/v1/chg-pwd","api/v1/chg-email").permitAll()
                        .requestMatchers("api/v1/chg-pwd-within").hasAnyAuthority("USER")
                        .requestMatchers("api/v1/update-client").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsServiceImpl)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }


}
