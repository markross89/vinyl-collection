package com.example.marek.user;

import com.example.marek.UserService;
import com.example.marek.role.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final RoleRepository roleRepository;
	
	public UserController (UserService userService, RoleRepository roleRepository) {
		
		this.userService = userService;
		this.roleRepository = roleRepository;
	}
	
	@GetMapping("/add")
	public String createUser (@RequestParam String firstName,
							  @RequestParam String lastName,
							  @RequestParam String username,
							  @RequestParam String password,
							  @RequestParam String passwordRepeat) {
		
		if (password.equals(passwordRepeat)) {
			User user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUsername(username);
			user.setPassword(password);
			userService.saveUser(user);
			return "login/login";
		}
		else {
			return "redirect:/login";
		}
		
	}
	
	
}


