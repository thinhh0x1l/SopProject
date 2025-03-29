package com.shopme.admin.user;


import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.PATH;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole() {
        Role roleAdmin = new Role("Test","test");;
        Role roleSaved = roleRepository.save(roleAdmin);
        assertThat(roleSaved.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role roleSalesperson = new Role("Salesperson","manage product price, customers, shipping, orders and sales report");;
        Role roleEditor = new Role("Editor","manage categories, brands, products, articles and menus");;
        Role roleShipper = new Role("Shipper","view products, view orders and update order status");;
        Role roleAssistant = new Role("Assistant","manage questions and reviews");
        roleRepository.saveAll(List.of(roleSalesperson,roleEditor,roleShipper,roleAssistant));
    }
    @Test
    public void testDeleteRole() {
        roleRepository.deleteById(6);
    }
}
