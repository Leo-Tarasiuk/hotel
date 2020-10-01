package com.tarasiuk.project.hotel.security;

import com.tarasiuk.project.hotel.configuration.CustomLocaleResolver;
import com.tarasiuk.project.hotel.service.exception.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomLocaleResolver customLocaleResolver;
    private final UserPrincipalDetailService userPrincipalDetailService;

    public SecurityConfig(UserPrincipalDetailService userPrincipalDetailService, CustomLocaleResolver customLocaleResolver) {
        this.userPrincipalDetailService = userPrincipalDetailService;
        this.customLocaleResolver = customLocaleResolver;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/registration", "/signup",
                        "/fitness", "/spa", "/map", "/dining", "/", "/room").permitAll()
                .antMatchers("/account", "/booking-delete").authenticated()
                .antMatchers("/clients", "/block", "/booking-accept",
                        "/booking-all", "/booking-all-waiting").hasRole("ADMIN")
                .antMatchers("/change", "/booking-save", "/passport-save",
                        "/passport-account", "/review-add").hasRole("CLIENT")
                .and()
                .formLogin()
                .loginProcessingUrl("/signin")
                .loginPage("/login").permitAll()
                .usernameParameter("Username")
                .passwordParameter("Password")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .and()
                .rememberMe().tokenValiditySeconds(2592000).key("mySecret!").rememberMeParameter("checkRememberMe");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailService);

        return daoAuthenticationProvider;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(customLocaleResolver);
    }
}
