package nl.devoteam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Database {
	
	@Autowired
	UserRepository users;
	
	 public Iterable<User> findUsers() {
	        return users.findAll();
	    }
	
}
