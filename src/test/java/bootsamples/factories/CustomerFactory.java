package bootsamples.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import bootsamples.additional.UserRole;
import bootsamples.model.Customer;

/**
 * @author Andrei Shalkevich
 *
 */
public class CustomerFactory {
	
	private String login;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private UserRole userRole;
	
	public CustomerFactory() {
		
		login = "testLogin1";
		password = "testPass1";
		firstName = "testFirstName1";
		lastName = "testLastName1";
		email = "testEmail1";
		userRole = UserRole.admin;
	}
	
	public Customer newInstance() {
		
		return new Customer(login, password, firstName, lastName, email, userRole);
	}
	
	public CustomerFactory setFields(String login, String password, String firstName,
			String lastName, String email, UserRole userRole) {
		
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userRole = userRole;
		return this;
	}
}
