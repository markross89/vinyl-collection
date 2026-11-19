
package com.example.marek.config;

import com.example.marek.entity.User;
import com.example.marek.security.CurrentUser;
import com.example.marek.security.SpringDataUserDetailsService;
import com.example.marek.service.AlbumService;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.util.Locale;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final SpringDataUserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final AlbumService albumService;
	private final MessageSource messageSource;
	private final LocaleResolver localeResolver;

	public SecurityConfig(
			SpringDataUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder,
			AlbumService albumService,
			MessageSource messageSource,
			LocaleResolver localeResolver) {

		this.userDetailsService = userDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.albumService = albumService;
		this.messageSource = messageSource;
		this.localeResolver = localeResolver;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.authorizeRequests()
				.antMatchers("/user/register").permitAll()
				.antMatchers("/user/userList", "/user/changeRole/**").hasRole("ADMIN")
				.antMatchers("/user/details", "/user/editUser").hasAnyRole("USER", "ADMIN")
				.antMatchers(
						"/album/**",
						"/home/add/**",
						"/box/**",
						"/track/**",
						"/tracklist/**"
				).hasAnyRole("USER", "ADMIN")
				.and()
				.formLogin()
				.loginPage("/login")
				.successHandler((request, response, authentication) -> {

					// Język wybrany przez użytkownika w aplikacji (PL/EN).
					Locale locale = localeResolver.resolveLocale(request);

					// Komunikat o poprawnym logowaniu.
					request.getSession().setAttribute(
							"vcLoginMessage",
							messageSource.getMessage(
									"login.success",
									null,
									locale
							)
					);

					String albumId = request.getParameter("addAlbumId");

					if (albumId != null && !albumId.trim().isEmpty()) {

						String albumMessage;
						String albumMessageType;

						try {

							Long id = Long.valueOf(albumId);

							Object principal = authentication.getPrincipal();

							if (principal instanceof CurrentUser) {

								CurrentUser currentUser = (CurrentUser) principal;
								User user = currentUser.getUser();

								boolean added =
										albumService.addAlbumToUser(id, user);

								if (added) {

									albumMessage = messageSource.getMessage(
											"album.add.success",
											null,
											locale
									);

									albumMessageType = "success";

								} else {

									albumMessage = messageSource.getMessage(
											"album.add.exists",
											null,
											locale
									);

									albumMessageType = "info";
								}

							} else {

								throw new IllegalStateException(
										"Nieprawidłowy użytkownik."
								);
							}

						} catch (NumberFormatException | IllegalStateException exception) {

							albumMessage = messageSource.getMessage(
									"album.add.error",
									null,
									locale
							);

							albumMessageType = "error";
						}

						request.getSession().setAttribute(
								"vcAlbumMessage",
								albumMessage
						);

						request.getSession().setAttribute(
								"vcAlbumMessageType",
								albumMessageType
						);
					}

					// Jeżeli logowanie nastąpiło w celu dodania albumu,
// zachowujemy dotychczasowe przekierowanie.
					if (albumId != null && !albumId.trim().isEmpty()) {
						response.sendRedirect(
								request.getContextPath() + "/album/albums"
						);
						return;
					}

// W pozostałych przypadkach wracamy do strony,
// która wcześniej wymagała zalogowania.
					SavedRequestAwareAuthenticationSuccessHandler successHandler =
							new SavedRequestAwareAuthenticationSuccessHandler();

					successHandler.setDefaultTargetUrl("/album/albums");

					successHandler.onAuthenticationSuccess(
							request,
							response,
							authentication
					);
				})
				.permitAll()
				.and()
				.logout()
				.logoutSuccessHandler((request, response, authentication) -> {

					// Wybrany język zachowujemy także po wylogowaniu.
					Locale locale = localeResolver.resolveLocale(request);

					request.getSession().setAttribute(
							"vcLogoutMessage",
							messageSource.getMessage(
									"logout.success",
									null,
									locale
							)
					);

					response.sendRedirect(
							request.getContextPath() + "/home/start"
					);
				})
				.permitAll()
				.and()
				.exceptionHandling()
				.accessDeniedPage("/403");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {

		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder);
	}
}
