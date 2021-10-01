package com.sda.webproject.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;// creeaza conexiunea cu baza de date

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/","/css/**","/js/**","/login-form")
                .permitAll();
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated();
        http
                .formLogin()
                .loginPage("/")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/");
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
    }


    @Autowired
    public void configureGlobal (AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception{
        auth
                .jdbcAuthentication()
                .dataSource(this.dataSource)
                .passwordEncoder(passwordEncoder);
        System.out.println(passwordEncoder.encode("12345"));
    }
}
