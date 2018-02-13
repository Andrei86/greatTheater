package bootsamples.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bootsamples.additional.UserRole;
import bootsamples.dto.CustomerInDTO;
import bootsamples.dto.CustomerOutDTO;
import bootsamples.dto.IdDTO;
import bootsamples.model.Customer;
import bootsamples.service.CustomerService;

/**
 * @author Andrei Shalkevich
 *
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PlaceController placeController;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	// 
	@RequestMapping(value = "/{login}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerByLogin(@PathVariable("login") String login)
	{
		
		Customer customer = customerService.findCustomerByLogin(login);
		
		CustomerOutDTO customerOutDTO = entity2dto(customer);
		
		return new ResponseEntity<CustomerOutDTO>(customerOutDTO, HttpStatus.OK);
	}
	
	// getCustomerByLogin - под капотом, в контроллере не надо
	
	//----- sign-up -------
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> signUp(@Valid @RequestBody CustomerInDTO customerInDTO){
		
		//System.out.println(bCryptPasswordEncoder.encode(customerInDTO.getPassword()));
		
		customerInDTO.setPassword(bCryptPasswordEncoder.encode(customerInDTO.getPassword()));
		
		//customerInDTO.setPassword(Base64.getEncoder().encodeToString(customerInDTO.getPassword().getBytes()));
		
		Customer customer = dto2entity(customerInDTO);
		
		customerService.createCustomer(customer);
		
		return new ResponseEntity<IdDTO>(new IdDTO(customer.getId()), HttpStatus.CREATED);
		
	}
	//------
	
	/*@RequestMapping(value = "/add", method = RequestMethod.POST) // authentication
	public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerInDTO customerInDTO){
		
		Customer customer = dto2entity(customerInDTO);
		
		customerService.createCustomer(customer);
		
		return new ResponseEntity<IdDTO>(new IdDTO(customer.getId()), HttpStatus.CREATED);
	}*/
	
	/*@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT) // change password??
	public ResponseEntity<?> updateCustomer(@PathVariable("id") Integer customerId, 
	@RequestBody CustomerInDTO сustomerInDTO){
		
		
		// else можно кидануть ошибку об отсутствии изменяемого объекта
		
		Customer customer = dto2entity(сustomerDTO);
		
		customerService.createOrUpdateCustomer(customer);
		
		return new ResponseEntity<CustomerInDTO>(HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/{login}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCustomer(@PathVariable("login") String login, @RequestBody CustomerInDTO customerInDTO){
		
		Customer customer = customerService.findCustomerByLogin(login);
		// you can change only first name, last name and Email
		//отрефакторить!
		
		customer.setFirstName(customerInDTO.getFirstName() != null ? 
							customerInDTO.getFirstName() : customer.getFirstName());
		
		customer.setLastName(customerInDTO.getLastName() != null ? 
							customerInDTO.getLastName() : customer.getLastName());
							
		customer.setEmail(customerInDTO.getEmail() != null ? 
							customerInDTO.getEmail() : customer.getEmail());
							
		customerService.updateCustomer(customer);
		
		return new ResponseEntity<IdDTO>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomerById(@PathVariable("id") Integer id){
		
		customerService.deleteCustomerById(id);
		
		return new ResponseEntity<Customer>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllCustomers(@PageableDefault Pageable pageable){
		
		List<Customer> customers = customerService.findAllCustomers(pageable);
		
		List<CustomerOutDTO> customerOutDTOs = new ArrayList<CustomerOutDTO>();
		
		for(Customer customer :	customers){
			
			CustomerOutDTO customerOutDTO = entity2dto(customer);
			
			customerOutDTOs.add(customerOutDTO);
		}
		
		
		return new ResponseEntity<List<CustomerOutDTO>>(customerOutDTOs, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{custId}/places/booking/{placeId}", method = RequestMethod.PUT)
	public ResponseEntity<?> placeBookingByCustomer(@PathVariable("custId") Integer custId, 
			@PathVariable("placeId") Integer placeId){
		
		return placeController.placeBooking(placeId, custId);
	}
	
	@RequestMapping(value = "/{custId}/places/booking", method = RequestMethod.PUT)
	public ResponseEntity<?> placesBookingByCustomer(@PathVariable("custId") Integer custId, 
			@RequestParam(required=true) List<Integer> id){
		
		return placeController.placesBooking(id, custId);
	}
	
	@RequestMapping(value = "/{custId}/places/cancel/{placeId}", method = RequestMethod.PUT)
	public ResponseEntity<?> placeCancelByCustomer(@PathVariable("custId") Integer custId, 
			@PathVariable("placeId") Integer placeId){
		
		return placeController.cancelPlaceBooking(placeId, custId);
	}
	
	@RequestMapping(value = "/{custId}/places/cancel", method = RequestMethod.PUT)
	public ResponseEntity<?> placesCancelByCustomer(@PathVariable("custId") Integer custId, 
			@RequestParam(required=true) List<Integer> id){
		
		return placeController.cancelPlacesBooking(id, custId);
	}
	
	 @RequestMapping(value = "/{custId}/places/busy/{placeId}", method = RequestMethod.PUT)
	public ResponseEntity<?> placeBusyByCustomer(@PathVariable("custId") Integer custId, 
			@PathVariable("placeId") Integer placeId){
		
		return placeController.busyPlaceSet(custId, placeId);
	}
	
	@RequestMapping(value = "/{custId}/places/busy", method = RequestMethod.PUT)
	public ResponseEntity<?> placesBusyByCustomer(@PathVariable("custId") Integer custId, 
			@RequestParam(required=true) List<Integer> id){
		
		return placeController.busyPlacesSet(id, custId);
	}
	
	@RequestMapping(value = "/{id}/places", method = RequestMethod.GET)// раб
	public ResponseEntity<?> getPlacesByCustomer(@PathVariable("id") Integer id){
		
		return placeController.getPlacesByCustomerId(id);
	}
	
	private Customer dto2entity(CustomerInDTO customerInDTO) {
		Customer customer = new Customer();
		if (customerInDTO.getId() != null)
			customer.setId(customerInDTO.getId());

		customer.setLogin(customerInDTO.getLogin());

		// тут прогоняем пароль через кодировку!!

		customer.setPassword(customerInDTO.getPassword()); // already encoded password
		customer.setFirstName(customerInDTO.getFirstName());
		customer.setLastName(customerInDTO.getLastName());
		customer.setEmail(customerInDTO.getEmail());
		customer.setUserRole(UserRole.user);

		return customer;
	}

	
	private CustomerOutDTO entity2dto(Customer customer) {
		
		CustomerOutDTO customerOutDTO = new CustomerOutDTO();

		customerOutDTO.setId(customer.getId());
		customerOutDTO.setLogin(customer.getLogin());
		customerOutDTO.setFirstName(customer.getFirstName());
		customerOutDTO.setLastName(customer.getLastName());
		customerOutDTO.setEMail(customer.getEmail());

		return customerOutDTO;
	}

}
