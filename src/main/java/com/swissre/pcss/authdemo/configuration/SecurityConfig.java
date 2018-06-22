package com.swissre.pcss.authdemo.configuration;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/favicon.ico", "/webjars/**", "/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(
                new OrRequestMatcher(
                        new AntPathRequestMatcher("/"), // index page for public access
                        EndpointRequest.to("health", "info"), // public actuator endpoints
                        new AntPathRequestMatcher("/cfhealth"), // public cloudfoundry health endpoint
                        new AntPathRequestMatcher("/error")))
            .authorizeRequests().anyRequest().permitAll();
    }

}