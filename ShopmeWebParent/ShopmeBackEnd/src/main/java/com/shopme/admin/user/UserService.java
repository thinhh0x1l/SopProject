package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {
    public static final int USERS_PER_PAGE= 5 ;
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
    public Page<User> listByPage(int pageNum,String sortField, String sortDir){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        return userRepository.findAll(PageRequest.of(pageNum-1, USERS_PER_PAGE, sort));
    }
    public User save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getId() != null) {
            User u = userRepository.findById(user.getId()).get();
            user.setPhotos(u.getPhotos());
            if (user.getPassword().isEmpty())
                user.setPassword(u.getPassword());
        }
        return userRepository.save(user);
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
