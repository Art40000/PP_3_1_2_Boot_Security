package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import java.util.List;
public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    List<User> findAll();
    User findById(long id);
    User save(User user);
    void deleteById(long id);
    public RoleRepository getRoleRepository();
}
