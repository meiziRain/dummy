package com.meizi.admin.auth;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


/**
 * @program: dummy
 * @description: 默认的登录用户类,继承security的UserDetails类
 * @author: Meizi
 * @create: 2020-01-06 16:06
 **/
public class DefaultJwtUserDetails implements UserDetails {

    @JsonIgnore
    private Long id;
    /**
     * 登录账户
     */
    private String username;
    /**
     * 登录密码
     */
    @JsonIgnore
    private String password;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 权限信息
     */
    @JsonIgnore
    private Collection<GrantedAuthority> authorities;
    /**
     * 是否启用
     */
    private boolean enabled;
    /**
     * 创建日期
     */
    private Date createAt;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Collection getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultJwtUserDetails{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", sex=").append(sex);
        sb.append(", authorities=").append(authorities);
        sb.append(", enabled=").append(enabled);
        sb.append(", createAt=").append(createAt);
        sb.append('}');
        return sb.toString();
    }

    public DefaultJwtUserDetails() {
    }

    public DefaultJwtUserDetails(Long id, String username, String password, String email, String phone, Integer sex, Collection<GrantedAuthority> authorities, boolean enabled, Date createAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.sex = sex;
        this.authorities = authorities;
        this.enabled = enabled;
        this.createAt = createAt;
    }
}