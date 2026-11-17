package com.umut.project.database.mywebservice.security;

import com.umut.project.database.mywebservice.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CurrentUser implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities;

    public CurrentUser(User user)
    {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isActivate();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getValue()));
    }

    @Override
    public String getUsername()
    {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }
}
