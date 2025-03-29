package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    public List<User> listAll(){
        return userRepository.findAll();
    }
    public List<Role> listRoles(){
        return roleRepository.findAll();
    }
    public void save(User user){
        if(user.getId() != null && user.getPassword().isEmpty()){
            User u = userRepository.findById(user.getId()).get();
            user.setPassword(u.getPassword());
        }else
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public boolean isEmailUnique(Integer id, String email) {
        boolean check = userRepository.existsByEmail(email);
        if (id!= -1&&check)
            return !userRepository.findIdByEmail(email).orElse(id).equals(id);
        return check;
    }
    public User get(Integer id){
         return userRepository.findById(id).orElseThrow(
                () ->  new UserNotFoundException("Could not find any user with ID: "+ id));
    }
    public boolean delete(Integer id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public void changeEnabled(Integer id, boolean enabled){
        userRepository.updateEnableStatus(id, enabled);
    }
}
