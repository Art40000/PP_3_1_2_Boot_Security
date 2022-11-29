package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        User user1 = new User("art@mail.com", "$2a$12$mKw06lhyBF3BMiRxIv0BAOzT5AFjnHc0TUofciUgtwj6ehLYbvSzm", "Art", 35);
        //password - admin
        User user2 = new User("em@mail.com", "$2a$12$FHoonI124ssoRQrAMpPuq.6rfI698NSJ60qDu99HKUPtZUM2Y57le", "Em", 45) ;

        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);

        user1.setRoles(Set.of(roleUser));
        user2.setRoles(Set.of(roleUser, roleAdmin));

        this.save(user1);
        this.save(user2);
    }


    @Override
    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
    @Override
    @Transactional
    public User findById(long id) {
        return userRepository.findById(id);
    }
    @Override
    @Transactional
    public void deleteById(long id) {
         userRepository.deleteById(id);
    }
    @Override
    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = findUserByEmail(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User " + userName + "not found");
        }

        return new org.springframework.security.core.userdetails.User (user.getEmail(), user.getPassword(),
                user.getRoles());

    }
}
