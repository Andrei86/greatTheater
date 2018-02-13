package bootsamples.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bootsamples.model.Customer;
import bootsamples.service.CustomerService;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private CustomerService customerService;

	public UserDetailsServiceImpl(CustomerService customerService) {
		
		this.customerService = customerService;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		Customer customer = customerService.findCustomerByLogin(login);
		if(customer == null){
			
			throw new UsernameNotFoundException(login);
			
		}
		return new User(customer.getLogin(), customer.getPassword(), emptyList());
	}

}
