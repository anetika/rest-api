package com.epam.esm.config;

import com.epam.esm.jwt.CustomAccessDeniedHandler;
import com.epam.esm.jwt.CustomAuthenticationEntryPoint;
import com.epam.esm.jwt.JwtConfigurer;
import com.epam.esm.jwt.JwtTokenProvider;
import com.epam.esm.matcher.AllEntityReadRequestMatcher;
import com.epam.esm.matcher.CertificateReadRequestMatcher;
import com.epam.esm.matcher.OrderCreateRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint entryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    private static final String ADMIN_ENDPOINT = "/**";
    private static final String ENTRY_ENDPOINT = "/auth/**";

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, CustomAuthenticationEntryPoint entryPoint, CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.entryPoint = entryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(ENTRY_ENDPOINT).permitAll()
                .requestMatchers(new CertificateReadRequestMatcher()).permitAll()
                .requestMatchers(new AllEntityReadRequestMatcher(), new OrderCreateRequestMatcher()).hasRole("USER")
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
