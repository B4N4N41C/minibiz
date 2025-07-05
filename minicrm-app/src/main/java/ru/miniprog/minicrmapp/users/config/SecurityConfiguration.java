package ru.miniprog.minicrmapp.users.config;

import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import ru.miniprog.minicrmapp.users.service.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
        private final UserService userService;

        public SecurityConfiguration(UserService userService) {
                this.userService = userService;
        }

        @Bean
        public HttpFirewall configureFirewall() {
                StrictHttpFirewall firewall = new StrictHttpFirewall();
                firewall.setAllowedHeaderValues(value -> true);
                return firewall;
        }

        @Bean
        public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(
                @Value("${jwt.cookie-token-key}") String cookieTokenKey) throws Exception {
                return new TokenCookieJweStringSerializer(new DirectEncrypter(
                        OctetSequenceKey.parse(cookieTokenKey)));
        }

        @Bean
        TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
                @Value("${jwt.cookie-token-key}") String cookieTokenKey,
                JdbcTemplate jdbcTemplate) throws Exception {
                return new TokenCookieAuthenticationConfigurer()
                        .tokenCookieStringDeserializer(new TokenCookieJweStringDeserializer(
                                new DirectDecrypter(
                                        OctetSequenceKey.parse(cookieTokenKey))));
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                       TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
                                                       TokenCookieJweStringSerializer tokenCookieJweStringSerializer) throws Exception {
                var tokenCookieSessionAuthenticationStrategy = new TokenCookieSessionAuthenticationStrategy();
                tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);
                http.formLogin(form -> form
                                .defaultSuccessUrl("/", true)
                                .permitAll())
                        // .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/swagger-ui/**", "/swagger-resources/*",
                                        "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/api/statistics/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                        .sessionManagement(sessionManagement -> sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .sessionAuthenticationStrategy(
                                        tokenCookieSessionAuthenticationStrategy))
                        .csrf(csrf -> csrf.disable());

                http.with(tokenCookieAuthenticationConfigurer, Customizer.withDefaults());
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userService.userDetailsService());
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}
