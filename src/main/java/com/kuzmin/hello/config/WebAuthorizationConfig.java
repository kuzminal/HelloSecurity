package com.kuzmin.hello.config;

import com.kuzmin.hello.filter.AuthenticationLoggingFilter;
import com.kuzmin.hello.filter.RequestValidationFilter;
import com.kuzmin.hello.filter.StaticKeyAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebAuthorizationConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private StaticKeyAuthenticationFilter filter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic();
        http.addFilterBefore(
                new RequestValidationFilter(),
                BasicAuthenticationFilter.class)
                .addFilterAfter(
                        new AuthenticationLoggingFilter(),
                        BasicAuthenticationFilter.class)
                .authorizeRequests()
                .mvcMatchers("/hello").hasRole("ADMIN")
                .mvcMatchers("/ciao").hasRole("MANAGER")
                .mvcMatchers(HttpMethod.GET, "/a")
                .authenticated()
                .mvcMatchers(HttpMethod.POST, "/a")
                .permitAll()
                .mvcMatchers("/a/b/**")
                .authenticated()
                .mvcMatchers("/product/{code:^[0-9]*$}")
                .permitAll()
                .anyRequest()
                .denyAll();
    }
}
