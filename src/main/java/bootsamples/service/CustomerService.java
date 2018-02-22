package bootsamples.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bootsamples.dao.CustomerRepository;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Customer;

/**
 * @author Andrei Shalkevich
 *
 */
@Service
public class CustomerService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CustomerService.class);

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepo) {
		this.customerRepository = customerRepo;
	}
	
	
	public Customer findCustomerByLogin(String login)
	{
		
		LOGGER.info("Find customer by login = {} ", login);
		
		Customer customer = customerRepository.findByLogin(login);

		if (customer == null) {
			throw new MyResourceNotFoundException(String.format("customer with login %s not found", login));
		}

		return customer;

	}
	
	public Customer findCustomerById(Integer id) {
		
		LOGGER.info("Find customer by id = {} ", id);

		Customer customer = customerRepository.findOne(id);

		if (customer == null) {
			throw new MyResourceNotFoundException(String.format("customer with id= %s not found", id));
		}

		return customer;

	}
	
	public List<Customer> findAllCustomers(Pageable pageable) {
		
		LOGGER.info("Find all customers");
		
		List<Customer> customers = null;

		Page<Customer> page = customerRepository.findAll(pageable);

		customers = page.getContent();

		return customers;
	}
	
	@Transactional
	public void deleteCustomerById(Integer id) {
		
		LOGGER.info("Delete customer by id = {} ", id);
		
		findCustomerById(id);
		customerRepository.delete(id);
	}
	
	@Transactional
	public Customer createCustomer(Customer customer) {
		
		LOGGER.info("Create customer with login = {} ", customer.getLogin());
		
		try {
			findCustomerByLogin(customer.getLogin());
		} catch (MyResourceNotFoundException e) {
			return customerRepository.save(customer);
		}
		throw new DuplicateEntityException(String.format("There is login %s already reserved", customer.getLogin()));
	}
	
	@Transactional
	public Customer updateCustomer(Customer customer) {
		
		LOGGER.info("Update customer with id = {} ", customer.getId());
		
		return customerRepository.save(customer);

	}
}
