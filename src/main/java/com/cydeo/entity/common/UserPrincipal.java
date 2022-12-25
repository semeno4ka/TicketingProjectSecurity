package com.cydeo.entity.common;

import com.cydeo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    // by UserDetails Spring pushes you to implement particular methods
    private User user;//entity we will pass and convert to Spring user
    public UserPrincipal(User user) {
        this.user = user;
    }
    //performs mapping between UserSpring and DB
    public Long getId(){
        return this.user.getId();
    }//is used in Listener to connect
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //GA how Spring understands roles(interface), impl is SimpleGrantedAuthority which looks for roles,
        List<GrantedAuthority> authorityList=new ArrayList<>();//GA is what is used by Spring behind the scene
        GrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole().getDescription());
        authorityList.add(authority);
        return authorityList;
    }

    @Override
    public String getPassword() { //   return user.getPassWord();
        return this.user.getPassWord();// will return DB user password and pass it Spring User (mapping)
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }


}
