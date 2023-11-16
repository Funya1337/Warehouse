package com.tumanyan.warehouse.service;

import com.tumanyan.warehouse.entity.User;
import com.tumanyan.warehouse.entity.roles.Role;
import com.tumanyan.warehouse.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    //Gleb
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserPrincipal(user);
    }

    // Boris
    public boolean registrationUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRole(Collections.singleton(Role.ADMIN));
        userRepository.save(user);
        return true;
    }

    public User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName());
    }

    public void deleteUser(HttpServletRequest request) {
        userRepository.delete(getAuthUser());
        SecurityContextHolder.getContext().setAuthentication(null);
        request.getSession().invalidate();
    }

    public Iterable<User> getUserList() {
        return userRepository.findAll();
    }

    public String blockUser(long id) {
        User user = userRepository.findByUserId(id);
        if (user == null) {
            return "Такого пользователя не сущесвует!";
        }

        if (getAuthUser().getUserID() == id) {
            return "Вы указали свой ID!";
        }

        if (user.getRole().contains(Role.ADMIN)) {
            return "Невозможно заблокировать администратора!";
        }

        user.setActive(false);
        userRepository.save(user);
        return "Пользователь успешно заблокирован!";
    }

}
