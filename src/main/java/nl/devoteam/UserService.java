package nl.devoteam;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
	@Autowired
	UserRepository users;
	
	public User generateUser() {
		int length = 10;
	    boolean useLetters = true;
	    boolean useNumbers = false;
		String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
		User user = new User();
		user.setClient_name(generatedString+"_client");
		user.setUser_name(generatedString+"_user");
		return user;
	}
}
