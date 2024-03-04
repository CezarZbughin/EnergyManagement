package com.cezar.energy.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "enduser",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class EndUser extends IdentityModel<Long> implements UserDetails {
    private String name;
    private String username;
    @JsonIgnore
    private String password;
    private String role;

    @JsonIgnore
    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role));
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Transient
    public void setRole(String role) {
        this.role = role;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getName() {
        return this.name;
    }
}
