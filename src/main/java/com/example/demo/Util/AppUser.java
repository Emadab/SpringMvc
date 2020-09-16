package com.example.demo.Util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class AppUser implements UserDetails {
    private static final long serialVersionUID = 2053941784865663756L;
    private String username;
    private String password;
    private boolean enabled;

    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private Set<GrantedAuthority> authorities;

    public AppUser(String username, String password, boolean enabled, boolean accountNonExpired,
                   boolean credentialsNonExpired, boolean accountNonLocked, Set<GrantedAuthority> authorities) {
        super();
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "AppUser [username=" + username + ", password=" + password + ", enabled=" + enabled
                + ", accountNonExpired=" + accountNonExpired + ", credentialsNonExpired=" + credentialsNonExpired
                + ", accountNonLocked=" + accountNonLocked + ", authorities=" + authorities + "]";
    }
}
