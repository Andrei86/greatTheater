package bootsamples.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bootsamples.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Customer findByLogin(String login);

}
