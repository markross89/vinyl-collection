package com.example.marek.user;

import com.example.marek.UserService;
import com.example.marek.role.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@GetMapping("/create-user")
	@ResponseBody
	public String createUser() {
		User user = new User();
		user.setFirstName("user");
		user.setLastName("user");
		user.setUsername("user");
		user.setPassword("user");
		userService.saveUser(user);
		return "marek";
	}
	
	
	
}


