package com.example.taskify_backend.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.taskify_backend.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    // Constructor để khởi tạo đối tượng từ Entity của bạn
    public UserDetailsImpl(Long id, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    // Static method giúp build UserDetailsImpl từ User Entity (giả sử bạn có class
    // User Entity)

    public static UserDetailsImpl build(User user) {
        // Nếu user có Role, bạn cần convert Role sang GrantedAuthority ở đây
        // Ví dụ đơn giản: gán quyền mặc định là USER nếu chưa có bảng Role
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(), // Password này phải là chuỗi đã mã hóa trong DB
                authorities);
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password; // Phải trả về password đã mã hóa, không được throw Exception
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Phải trả về quyền, không được throw Exception
    }

    // Các method bắt buộc khác của UserDetails
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
        return true;
    }
}