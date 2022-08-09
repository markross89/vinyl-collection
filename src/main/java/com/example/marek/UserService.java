package com.example.marek;

import com.example.marek.user.User;


public interface UserService {
	
	User findByUserName (String name);
	
	void saveUser (User user);
	
	
}
