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
@Transactional
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepo) {
		this.customerRepository = customerRepo;
	}
	
	public Customer findCustomerByLogin(String login)
	{

		Customer customer = customerRepository.findByLogin(login);

		if (customer == null) {
			throw new MyResourceNotFoundException(String.format("customer with login %s not found", login));
		}

		return customer;

	}
	
	public Customer findCustomerById(Integer id) {

		Customer customer = customerRepository.findOne(id);

		if (customer == null) {
			throw new MyResourceNotFoundException(String.format("customer with id= %s not found", id));
		}

		return customer;

	}
	
	public List<Customer> findAllCustomers(Pageable pageable) {

		List<Customer> customers = null;

		Page<Customer> page = customerRepository.findAll(pageable);

		customers = page.getContent();

		return customers;
	}
	
	public void deleteCustomerById(Integer id) {
		findCustomerById(id);
		customerRepository.delete(id);
	}
	
	public Customer createCustomer(Customer customer) {
		try {
			findCustomerByLogin(customer.getLogin());
		} catch (MyResourceNotFoundException e) {
			return customerRepository.save(customer);
		}
		throw new DuplicateEntityException(String.format("There is login %s already reserved", customer.getLogin()));
	}
	
	public Customer updateCustomer(Customer customer) {
		return customerRepository.save(customer);

	}
}
