package com.shopme.admin;

import com.shopme.admin.security.ShopmeUserDetailsService;
import com.shopme.admin.user.RoleRepository;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
@Controller
@EntityScan({"com.shopme.common.entity"})
public class ShopmeBackEndApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ShopmeBackEndApplication.class, args);
	}
	@GetMapping("/")
	public String home() {
		return "index";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
