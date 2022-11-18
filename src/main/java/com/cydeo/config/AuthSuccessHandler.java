package com.cydeo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
     //impl to make landing page based on the Role. Will be injected in SecurityConfig
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());//(captures user role)
        // one user might have more than one role
        //good for Interview to give example of Java usage
        if(roles.contains("Admin")){ response.sendRedirect("/user/create");}
        if(roles.contains("Manager")){ response.sendRedirect("/task/create");}
        if(roles.contains("Employee")){ response.sendRedirect("/task/employee/pending-tasks");}

    }
}