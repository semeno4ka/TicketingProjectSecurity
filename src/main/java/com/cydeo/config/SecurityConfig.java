package com.cydeo.config;

import com.cydeo.service.SecurityService;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final SecurityService securityService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.securityService = securityService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }


    //    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//        //Overriding Spring User class to create our own users with our passwords. Hardcoded. Will then connect with DB
//        List<UserDetails> userList= new ArrayList<>();
//
//        userList.add(
//                new User("mike",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));// coming from security, noit your entity)
//
//        userList.add(
//                new User("ozzy",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//        return new InMemoryUserDetailsManager(userList);// saves in memory
//}

    @Bean
    public DefaultSecurityFilterChain filterCHain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/user/**").hasAuthority("Admin")// in DB we have, need to match DB way
                .antMatchers("/project/**").hasAuthority("Manager") // can define more than one role with hasANyRole and other methods
                .antMatchers("/task/employee/**").hasAuthority("Employee")
                .antMatchers("/task/**").hasAuthority("Manager")
                // .antMatchers("/user/**").hasRole("ADMIN")//under user controller
                // .antMatchers("/project/**").hasRole("MANAGER") //has role has ROLE_ by default, that is why we need hasAuthority and use Admin as written in DB
                // .antMatchers("/task/employee/**").hasRole("EMPLOYEE")
                // .antMatchers("/task/**").hasRole("MANAGER")
                // .antMatchers("/task/**").hasAnyRole("EMPLOYEE","ADMIN")
                // .antMatchers("/task/**").hasAuthority("ROLE_EMPLOYEE") needs to match SimpleAuthority
                .antMatchers(// can be either controller endpoint or directory. Everyone should be able to get access to below
                        "/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**")
                .permitAll()// access to files above
                .anyRequest()
                .authenticated()
                .and()// combines the portions
              //  .httpBasic()//pop up window. Below we provide our form
                .formLogin()
                .loginPage("/login")// our login page inside controller - endpoint
                // .defaultSuccessUrl("/welcome")// where will it land in after success login (endpoint from controller or default)
                .successHandler(authenticationSuccessHandler)//requests bean from AuthenticationSuccessHandler, need config class and injection
                .failureUrl("/login?error=true")// if logIn was unsuccess
                .permitAll()//login should be accessed by all (order does not matter, can put right after form)
                .and()
                .logout()// put href in Logout that lead to logout from security
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//looks where the logout button in html
                .logoutSuccessUrl("/login")// lands back on login page from controller
                .and()
                .rememberMe()
                .tokenValiditySeconds(120)
                .key("cydeo")
                .userDetailsService(securityService)// need injection to use the SS we have in Service Package(need to capture the user)
                .and()
                .build();// required in the end

    }




}