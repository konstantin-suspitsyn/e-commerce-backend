package com.github.konstantin.suspitsyn.ecommercebackend.configuration;

import com.github.konstantin.suspitsyn.ecommercebackend.user.UserRole;
import com.github.konstantin.suspitsyn.ecommercebackend.user.UserService;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.filter.CustomAuthenticationFilter;
import com.github.konstantin.suspitsyn.ecommercebackend.user.loginregistration.filter.CustomAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/registration/**").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/users/self/**").authenticated();
        http.authorizeRequests().antMatchers("/users/**").hasAnyAuthority(UserRole.ADMIN.name());
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/product-categories/**").hasAnyAuthority(UserRole.ADMIN.name());
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/product-categories/**").hasAnyAuthority(UserRole.ADMIN.name());
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/product-categories/**").hasAnyAuthority(UserRole.ADMIN.name());
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement( ).maximumSessions(1).maxSessionsPreventsLogin(false);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
