package com.example.marek.user;

import com.example.marek.UserService;
import com.example.marek.role.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	
	public UserController (UserService userService, RoleRepository roleRepository, UserRepository userRepository) {
		
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/register")
	public String showForm (Model model) {
		
		model.addAttribute("user", new User());
		return "/login/register";
	}
	
	@PostMapping("/register")
	public String processForm (@Valid User user, BindingResult result) {
		
		if (result.hasErrors()) {
			return "login/register";
		}
		else {
			for (User u : userRepository.findAll()) {
				if (u.getUsername().equals(user.getUsername())) {
					return "/login/messageRegister";
				}
				else {
					userService.saveUser(user);
				}
			}
			return "redirect:/login";
		}

//	@GetMapping("/add")
//	public String createUser (@RequestParam String firstName,
//							  @RequestParam String lastName,
//							  @RequestParam String username,
//							  @RequestParam String password,
//							  @RequestParam String passwordRepeat) {
//
//		if (password.equals(passwordRepeat)) {
//			User user = new User();
//			user.setFirstName(firstName);
//			user.setLastName(lastName);
//			user.setUsername(username);
//			user.setPassword(password);
//			userService.saveUser(user);
//			return "login/login";
//		}
//		else {
//			return "redirect:/login";
//		}
//
//	}
	}
}


