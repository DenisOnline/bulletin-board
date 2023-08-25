package ru.bulletin_board.bulletin_board.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bulletin_board.bulletin_board.models.enums.Role;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    @Email(message = "Неверный формат email адреса")
    @NotEmpty(message = "Поле не может быть пустым")
    @Size(max = 255, message = "Email не должен превышать 255 символов")
    private String email;

    @Column(name = "phoneNumber")
    @NotEmpty(message = "Поле не может быть пустым")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Номер телефона должен содержать только цифры и символ '+'")
    private String phoneNumber;

    @Column(name = "name")
    @NotEmpty(message = "Поле не может быть пустым")
    private String name;

    @Column(name = "password")
    @NotEmpty(message = "Поле не может быть пустым")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать минимум 8 символов, включая хотя бы одну букву верхнего и нижнего регистра, цифру и специальный символ (@#$%^&+=!)")
    private String password;

    //    @Column(name = "avatar")
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "image_id")
//    private Image avatar;
    @Column(name = "active")
    private boolean active;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> products = new ArrayList<>();

    private LocalDateTime dateOfCreated;

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
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
        return active;
    }
}
