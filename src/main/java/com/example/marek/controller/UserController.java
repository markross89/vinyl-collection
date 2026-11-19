
package com.example.marek.controller;

import com.example.marek.dto.UserDto;
import com.example.marek.entity.Role;
import com.example.marek.entity.User;
import com.example.marek.repository.RoleRepository;
import com.example.marek.repository.UserRepository;
import com.example.marek.security.CurrentUser;
import com.example.marek.service.UserService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public UserController(UserService userService,
	                      RoleRepository roleRepository,
	                      UserRepository userRepository) {
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	// REGISTRATION

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("userDto", new UserDto());
		return "login/register";
	}

	@PostMapping("/register")
	public String processRegisterForm(@Valid @ModelAttribute("userDto") UserDto userDto,
	                                  BindingResult result,
	                                  RedirectAttributes redirectAttributes) {

		validatePassword(userDto, result, true);

		String username = normalizeUsername(userDto.getUsername());

		if (username != null && userRepository.existsByUsername(username)) {
			result.rejectValue("username", "user.username.exists");
		}

		if (result.hasErrors()) {
			return "login/register";
		}

		User user = new User();
		user.setFirstName(userDto.getFirstName().trim());
		user.setLastName(userDto.getLastName().trim());
		user.setUsername(username);
		user.setPassword(userDto.getPassword());

		userService.saveUser(user);

		redirectAttributes.addFlashAttribute(
				"notificationMessage", "register.success"
		);
		redirectAttributes.addFlashAttribute("notificationType", "success");

		return "redirect:/login";
	}

	// USER DETAILS

	@GetMapping("/details")
	public String showUserDetails(Model model,
	                              @AuthenticationPrincipal CurrentUser customUser) {

		User user = userRepository.findById(customUser.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User not found"));

		model.addAttribute("user", user);

		return "login/userDetails";
	}

	// EDIT PROFILE

	@GetMapping("/editUser")
	public String editUser(Model model,
	                       @AuthenticationPrincipal CurrentUser customUser) {

		User user = userRepository.findById(customUser.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User not found"));

		UserDto userDto = new UserDto();
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setUsername(user.getUsername());

		model.addAttribute("userDto", userDto);

		return "login/editUser";
	}


	@PostMapping("/editUser")
	public String saveUser(@Valid @ModelAttribute("userDto") UserDto userDto,
	                       BindingResult result,
	                       @AuthenticationPrincipal CurrentUser customUser,
	                       RedirectAttributes redirectAttributes) {

		validatePassword(userDto, result, false);

		String username = normalizeUsername(userDto.getUsername());

		User user = userRepository.findById(customUser.getUser().getId())
				.orElseThrow(() -> new IllegalStateException("User not found"));

		if (username != null) {
			userRepository.findByUsername(username).ifPresent(existingUser -> {
				if (!existingUser.getId().equals(user.getId())) {
					result.rejectValue("username", "user.username.exists");
				}
			});
		}

		if (result.hasErrors()) {
			return "login/editUser";
		}

		user.setFirstName(userDto.getFirstName().trim());
		user.setLastName(userDto.getLastName().trim());
		user.setUsername(username);

		userService.updateUser(user, userDto.getPassword());

		// Refresh authenticated principal.
		Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toSet());

		CurrentUser updatedPrincipal = new CurrentUser(
				user.getUsername(),
				user.getPassword(),
				authorities,
				user
		);

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(
						updatedPrincipal,
						null,
						updatedPrincipal.getAuthorities()
				)
		);

		redirectAttributes.addFlashAttribute("profileUpdated", true);
		redirectAttributes.addFlashAttribute(
				"notificationMessage", "profile.edit.success"
		);
		redirectAttributes.addFlashAttribute("notificationType", "success");

		return "redirect:/user/editUser";
	}

	@PostMapping("/deleteAccount")
	public String deleteAccount(
			@AuthenticationPrincipal CurrentUser customUser,
			HttpServletRequest request,
			HttpServletResponse response,
			RedirectAttributes redirectAttributes) {

		Long userId = customUser.getUser().getId();

		userService.deleteUser(userId);

		SecurityContextHolder.clearContext();

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		redirectAttributes.addFlashAttribute(
				"notificationMessage", "profile.delete.success"
		);
		redirectAttributes.addFlashAttribute("notificationType", "success");

		return "redirect:/login";
	}


	// USER LIST - ADMIN

	@GetMapping("/userList")
	public String userList(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "login/usersList";
	}

	// CHANGE USER ROLE - ADMIN

	@GetMapping("/changeRole/{id}")
	public String roleForm(Model model, @PathVariable long id) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("User not found"));

		model.addAttribute("user", user);
		model.addAttribute("roles", roleRepository.findAll());

		return "login/roleForm";
	}

	@PostMapping("/changeRole/{id}")
	public String updateRole(@PathVariable long id,
	                         @RequestParam String role) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("User not found"));

		Role selectedRole = roleRepository.findByName(role)
				.orElseThrow(() -> new IllegalStateException("Role not found"));

		Set<Role> roles = new HashSet<>();
		roles.add(selectedRole);

		user.setRoles(roles);
		userRepository.save(user);

		return "redirect:/user/userList";
	}

	// VALIDATION HELPERS

	private void validatePassword(UserDto userDto,
	                              BindingResult result,
	                              boolean registration) {

		String password = userDto.getPassword();
		String confirmPassword = userDto.getConfirmPassword();

		boolean passwordProvided = password != null && !password.trim().isEmpty();

		if (registration && !passwordProvided) {
			result.rejectValue("password", "user.password.required");
			return;
		}

		if (passwordProvided && password.length() < 8) {
			result.rejectValue("password", "user.password.size");
		}

		if (registration && (confirmPassword == null || confirmPassword.isEmpty())) {
			result.rejectValue("confirmPassword", "user.password.confirm.required");
		} else if (passwordProvided && !password.equals(confirmPassword)) {
			result.rejectValue("confirmPassword", "user.password.confirm.mismatch");
		}
	}

	private String normalizeUsername(String username) {
		if (username == null) {
			return null;
		}

		return username.trim().toLowerCase();
	}
}
