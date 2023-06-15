package fr.kitsuapirest.config;

import fr.kitsuapirest.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {


    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return userService::loadUserByUsername;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/animes").permitAll()
                        .requestMatchers("/animes/search").permitAll()
                        .requestMatchers("/animes/{id}").permitAll()
                        .requestMatchers("/animes/{id}/comments/{commentId}").permitAll()
                        .requestMatchers(("/profil")).authenticated()
                        .requestMatchers("/watchlist").authenticated()
                        .requestMatchers("/ranking").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.DELETE, "/api/users/{userId}/watchlist/remove-anime/{animeId}").authenticated()
                        // Ajoutez ici d'autres autorisations pour votre API
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
