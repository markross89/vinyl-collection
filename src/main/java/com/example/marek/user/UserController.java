package com.example.marek.user;

import com.example.marek.UserService;
import com.example.marek.role.Role;
import com.example.marek.role.RoleRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


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
	
	@GetMapping("/register")  // display register form
	public String showForm (Model model) {
		
		model.addAttribute("user", new User());
		return "/login/register";
	}
	
	@PostMapping("/register")  // adds a user
	public String processForm (@Valid User user, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "login/register";
		}
		else {
			for (User u : userRepository.findAll()) {
				if (u.getUsername().equals(user.getUsername())) {
					return "/login/messageRegister";
				}
			}
			userService.saveUser(user);
			model.addAttribute("user", user);
			return "login/messageAddUser";
		}
	}
	
	@GetMapping("/details")  // display details of  user
	public String showUserDetails (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("user", customUser.getUser());
		return "/login/userDetails";
	}
	
	@GetMapping("/editUser")  // display edit user form
	public String editUser (Model model, @AuthenticationPrincipal CurrentUser customUser) {
		
		model.addAttribute("user", customUser.getUser());
		return "/login/editUser";
	}
	
	@PostMapping("/editUser")  // update user
	public String saveUser (@Valid User user, BindingResult result) {
		
		if (result.hasErrors()) {
			return "user/editUser";
		}
		else {
			for (User u : userRepository.findAll()) {
				if (u.getUsername().equals(user.getUsername())) {
					return "/login/messageRegister";
				}
				else {
					userService.saveUser(user);
					return "/login/messageUpdate";
				}
			}
			return "redirect:/login";
		}
	}
	
	@GetMapping("/userList") // display users list
	public String userList (Model model) {
		
		model.addAttribute("users", userRepository.findAll());
		return "/login/usersList";
	}
	
	@GetMapping("/changeRole/{id}")  // display user role form
	public String roleForm (Model model, @PathVariable long id) {
		
		model.addAttribute("user", id);
		model.addAttribute("roles", roleRepository.findAll());
		return "/login/roleForm";
	}
	
	@PostMapping("/changeRole/{id}")  // update users role
	public String updateRole (@PathVariable long id, @RequestParam String role) {
		
		User user = userRepository.findById(id).get();
		Set<Role> roles = new HashSet<>();
		roles.add(roleRepository.findByName(role));
		user.setRoles(roles);
		userRepository.save(user);
		return "redirect:/user/userList";
	}
}


