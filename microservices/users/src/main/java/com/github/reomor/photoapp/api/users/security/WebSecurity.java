package com.github.reomor.photoapp.api.users.security;

import com.github.reomor.photoapp.api.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final Environment environment;
    private final CustomIpAuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurity(
            Environment environment,
            CustomIpAuthenticationProvider authenticationProvider,
            UserService userService,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.environment = environment;
        this.authenticationProvider = authenticationProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // fixme
        //auth.authenticationProvider(authenticationProvider);
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers("/**").permitAll().and().addFilter(getAuthenticationFilter());
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(environment, userService, authenticationManager());
        //authenticationFilter.setAuthenticationManager(authenticationManager());
        final String filterProcessesUrl = environment.getProperty("login.url.path");
        if (filterProcessesUrl != null && !filterProcessesUrl.isBlank()) {
            authenticationFilter.setFilterProcessesUrl(filterProcessesUrl);
        }
        return authenticationFilter;
    }
}
