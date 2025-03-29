package com.shopme.admin;

import com.shopme.admin.user.RoleRepository;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
@EntityScan({"com.shopme.common.entity"})
public class ShopmeBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeBackEndApplication.class, args);

	}
	@GetMapping("/")
	public String home() {
		return "index";
	}

}
