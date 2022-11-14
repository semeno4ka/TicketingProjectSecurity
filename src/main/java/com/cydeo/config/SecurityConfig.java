package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        //Overriding Spring User class to create our own users with our passwords. Hardcoded. Will then connect with DB
        List<UserDetails> userList= new ArrayList<>();

        userList.add(
                new User("mike",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));// coming from security, noit your entity)

        userList.add(
                new User("ozzy",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));

        return new InMemoryUserDetailsManager(userList);// saves in memory
}

    @Bean
    public DefaultSecurityFilterChain filterCHain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/user/**").hasRole("ADMIN")//under user controller
                .antMatchers("/project/**").hasRole("MANAGER")
                .antMatchers("/task/employee/**").hasRole("EMPLOYEE")
                .antMatchers("/task/**").hasRole("MANAGER")
               // .antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN")
               // .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE") needs to match SimpleAuthority
                .antMatchers(
                        "/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
              //  .httpBasic()//pop up window
                .formLogin()
                .loginPage("/login")// our login page
                .defaultSuccessUrl("/welcome")// where will it end
                .failureUrl("/login?error=true")
                .permitAll()//login should be accessed by all
                .and()
                .build();

    }




}