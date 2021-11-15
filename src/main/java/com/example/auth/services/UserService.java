package com.example.auth.services;

import com.example.auth.model.AppRole;
import com.example.auth.model.AppUser;

import java.util.List;

public interface UserService {

    AppUser saveUser(AppUser user);
    AppRole saveRole(AppRole role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser>getUsers();
}
