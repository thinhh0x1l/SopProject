package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserService userService;
    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class,1);
        User test = new User("tes1t@gmail.com","123456","Hoang","Thinh");
        test.addRole(roleAdmin);
        userRepository.save(test);
        assertThat(test.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateNewUserWithTwoRoles() {
        User test2 = new User("test5@gmail.com","123456","Ko","LOP");
        test2.addRole(new Role(3));
        test2.addRole(new Role(4));
        userRepository.save(test2);
    }
    @Test
    public void testListAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    public void testGetUserById(){
        User user = userRepository.findById(1).get();
        user.setFirstName("aaaaaaaa");
    }

    @Test
    public void testUpdateUserRole(){
        User user = userRepository.findById(3).get();
        //Role roleAdmin = entityManager.find(Role.class,4);
        user.getRoles().remove(new Role(4));
    }
    @Test
    public void testDeleteUserById(){
        userRepository.deleteById(3);
    }
    @Test
    public void testGetUserByEmail(){
        String email = "seo@gmail.com";
        boolean u = userRepository.existsByEmail(email);
        assertThat(u).isTrue();
    }

    @Test
    public void testChangeEnabled(){
        userService.changeEnabled(1,false);
    }
}
