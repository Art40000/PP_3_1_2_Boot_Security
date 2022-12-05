package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.*;
import ru.kata.spring.boot_security.demo.repository.*;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private PasswordEncoder PasswordEncoder;

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoder;
    }
    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.PasswordEncoder = passwordEncoder;
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    @PostConstruct
    public void init(){

        //password - user
        User user1 = new User("art@mail.com", "$2a$12$R93X0z48TKzdxB0YBHYRfO9WuCibHC1hGV4IrW2RL7GoOjfap7K96", "Art", 35);
        //password - admin
        User user2 = new User("em@mail.com", "$2a$12$jeg0PNOyY9Rl5qIe1NtofeKLgQZn5F03JYl5//1YvvRSwlu5o2dUK", "Em", 45) ;

        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);

        user1.setRoles(Set.of(roleUser));
        user2.setRoles(Set.of(roleUser, roleAdmin));

        userRepository.save(user1);
        userRepository.save(user2);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public void deleteById(Long id) {
         userRepository.deleteById(id);
    }
    @Override
    public List<User> findAll() {
        return  userRepository.findAll();
    }
    @Override
    public User save(User user) {
        user.setPassword(getPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }
//    @Override
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    };


    @Override
    @Transactional
    public UserDetails loadUserByUsername( String userName) throws UsernameNotFoundException {
        User user = findUserByEmail(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User " + userName + "not found");
        }
        return user;
    }
}
