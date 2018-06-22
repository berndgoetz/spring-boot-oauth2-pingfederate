package com.swissre.pcss.authdemo.configuration;

import com.swissre.pcss.authdemo.ApplicationProperties;
import com.swissre.pcss.authdemo.security.PingFederateAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableResourceServer
@EnableWebSecurity(debug = true)
@EnableOAuth2Sso
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // comment out from here to...

    @Autowired
    ApplicationProperties appProps;

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(pingFederateAccessTokenConverter());
    }

    public PingFederateAccessTokenConverter pingFederateAccessTokenConverter() {
        return new PingFederateAccessTokenConverter(appProps.getPingFederateKeyUrl());
    }

    // ... here to show what's going wrong with PingFederate's userinfo.openid endpoint
    // you should receive a 401. If not, let me know what you did better.

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatcher(new RequestHeaderRequestMatcher("Authorization"))
                .authorizeRequests().anyRequest().fullyAuthenticated();
    }

}