package bootsamples.servicesTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import bootsamples.additional.UserRole;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.CustomerFactory;
import bootsamples.model.Customer;
import bootsamples.service.CustomerService;

/**
 * @author Andrei Shalkevich
 *
 */
public class CustomerServiceTest extends AbstractServiceTest{
	
	@Autowired
	private EntityManager testEntityManager;
	
	@Autowired
	private CustomerService customerService;
	
	public CustomerFactory customerFactory = new CustomerFactory();
	public Customer customer1 = customerFactory.newInstance();
	public Customer customer2 = null;
	
	@Before
	public void before(){
		
		testEntityManager.persist(customer1);
		
	}
	
	@Test
	public void findCustomerByLoginTest(){

		Customer customerFromDB = customerService.findCustomerByLogin(customer1.getLogin());
		
		assertThat(customerFromDB).isEqualTo(customer1);
	}

	@Test
	public void findCustomerByIdTest(){

		Customer customerFromDB = customerService.findCustomerById(customer1.getId());

		assertThat(customerFromDB).isEqualTo(customer1);
	}
	
	@Test
	public void findAllCustomersTest(){
		
		Pageable foundPage = new PageRequest(0, 3);
		List<Customer> customerListFromDB = customerService.findAllCustomers(foundPage);
		
		assertThat(customerListFromDB).hasSize(1);
	}
	
	@Test(expected=MyResourceNotFoundException.class)
	public void deleteCustomerByIdTest(){
		
		Integer customerId = customer1.getId();
		customerService.deleteCustomerById(customerId);
		
		customerService.findCustomerById(customerId);
	}
	
	@Test
	public void createCustomerTest(){
		
		customerFactory.setFields("testLogin2", "testPass2", "testFirstName2",
		"testLastName2", "testEmail2", UserRole.user);
		
		Customer newCustomer = customerFactory.newInstance();

		Customer createdCustomer = customerService.createCustomer(newCustomer);
		
		assertThat(createdCustomer.getId()).isNotNull();
	}
	
	@Test
	public void updateCustomerTest(){
		
		Customer customerFromDB = customerService.findCustomerByLogin(customer1.getLogin());
		customerFromDB.setLastName("updatedLastName");
		
		Customer updatedCustomer = customerService.updateCustomer(customerFromDB);
		
		assertThat(updatedCustomer.getLastName()).isEqualTo("updatedLastName");
	} 
	
}
