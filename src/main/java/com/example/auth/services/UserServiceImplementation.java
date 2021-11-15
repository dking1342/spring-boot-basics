package com.example.auth.services;

import com.example.auth.model.AppRole;
import com.example.auth.model.AppUser;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j // logger
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null){
            log.error("user not found");
            throw new UsernameNotFoundException("user not found");
        } else {
            log.info("user found: {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getApp_roles()
                .forEach(role ->{
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                });

        return new org.springframework.security.core.userdetails.User(appUser.getUsername(),appUser.getPassword(),authorities);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("saving new user to the database");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        log.info("saving role to the database");
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}",roleName,username);
        AppUser appUser = userRepository.findByUsername(username);
        AppRole appRole = roleRepository.findByName(roleName);
        appUser.getApp_roles().add(appRole);
    }

    @Override
    public AppUser getUser(String username) {
        log.info("fetching user {}",username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("fetching all users");
        return userRepository.findAll();
    }


}
