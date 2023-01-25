package cz.rekvalifikace.projekt.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import cz.rekvalifikace.projekt.service.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfiguration(final JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .httpBasic(withDefaults())
                .userDetailsService(jpaUserDetailsService)
                .authorizeRequests()
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/home")
//                .addLogoutHandler(((request, response, authentication) -> {
//                    SecurityContextHolder.getContext().setAuthentication(null);
//                    SecurityContextHolder.getContext().getAuthentication().getAuthorities().removeAll(
//                            SecurityContextHolder.getContext().getAuthentication().getAuthorities());
//                }))
                .and()
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
