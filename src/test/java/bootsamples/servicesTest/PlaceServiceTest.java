package bootsamples.servicesTest;

import java.util.List;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import bootsamples.additional.Status;
import bootsamples.additional.UserRole;
import bootsamples.exceptions.InsufficientRightsException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.CustomerFactory;
import bootsamples.factories.PlaceFactory;
import bootsamples.model.CategoryCost;
import bootsamples.model.Cinema;
import bootsamples.model.CinemaSchema;
import bootsamples.model.Customer;
import bootsamples.model.Genre;
import bootsamples.model.Movie;
import bootsamples.model.Place;
import bootsamples.model.Seance;
import bootsamples.service.PlaceService;

/**
 * @author Andrei Shalkevich
 *
 */
public class PlaceServiceTest extends AbstractServiceTest{
	
	@Autowired
	private EntityManager testEntityManager;

	@Autowired
	private PlaceService placeService;
	
	public PlaceFactory placeFactory = new PlaceFactory();
	public Place place1 = placeFactory.newInstance();
	
	public Seance seance = place1.getSeance();
	
	public CinemaSchema schema = place1.getSeance().getCinema().getCinemaSchema();
	
	public Cinema cinema = place1.getSeance().getCinema();
	
	public Movie movie = place1.getSeance().getMovie();
	
	public List<Genre> genres = movie.getGenres();
	
	public CategoryCost categoryCost = place1.getCategoryCost();
	
	public CustomerFactory customerFactory = new CustomerFactory();
	public Customer customer = customerFactory.newInstance();
	
	public Place place2 = null;
	
	@Before
	public void before(){
		
		// Persist all entities for place in DB
		
		testEntityManager.persist(schema);
		
		testEntityManager.persist(cinema);
		
		testEntityManager.persist(customer);
		
		for(Genre genre : genres)
			testEntityManager.persist(genre);
		
		testEntityManager.persist(movie);
		
		testEntityManager.persist(seance);
		
		testEntityManager.persist(categoryCost);
		
		placeFactory.setFields(seance, null, categoryCost, 4, 8, Status.free);
		
		place2 = placeFactory.newInstance();
		
		testEntityManager.persist(place1);
		testEntityManager.persist(place2);
	}
	
	@Test
	public void findPlaceByIdTest(){

		Place placeFromDB = placeService.findPlaceById(place1.getId());
		
		assertThat(placeFromDB).isEqualTo(place1);
	}
	
	@Test
	public void findPlacesBySeanceIdTest(){

		List<Place> placeListFromDB = placeService.findPlacesBySeanceId(seance.getId());
		
		assertThat(placeListFromDB).hasSize(2);
	}
	
	@Test
	public void findPlacesByCustomerIdTest(){

		placeService.bookingPlace(place1.getId(), customer.getId());
		
		List<Place> placeListFromDB = placeService.findPlacesByCustomerId(customer.getId());
		
		assertThat(placeListFromDB).hasSize(1);
	}
	
	// not necessary test it!!!!!!!!!!!!!!!!!
	/*@Test
	public void changeStatusToBookedTest() {
		Integer placeId = place1.getId();
		
		placeService.changeStatusToBooked(placeId);

		Place placeFromDB = placeService.findPlaceById(placeId);

		assertThat(placeFromDB.getStatus()).isEqualTo(Status.booked);

	}
	
	@Test
	public void changeStatusToFreeTest() {
		Integer placeId = place1.getId();
		
		place1.setCustomer(customer);
		
		placeService.updatePlace(place1);
		
		placeService.changeStatusToBooked(placeId);
		
		//placeService.changeStatusToFree(customer.getId(), placeId);

		Place freePlaceFromDB = placeService.changeStatusToFree(customer.getId(), placeId);

		assertThat(freePlaceFromDB.getStatus()).isEqualTo(Status.free);

	}*/
	
	@Test(expected = MyResourceNotFoundException.class)
	public void deletePlaceByIdTest() {

		Integer placeId = place1.getId();
		placeService.deletePlaceById(placeId);

		placeService.findPlaceById(placeId);
	}
	
	@Test
	public void createPlaceTest() {
		placeFactory.setFields(seance, null, categoryCost, 4, 9, Status.free);

		Place newPlace = placeFactory.newInstance();

		Place createdPlace = placeService.createPlace(newPlace);

		assertThat(createdPlace.getPlace()).isEqualTo(newPlace.getPlace());
	}
	
	@Test
	public void updatePlaceTest() {

		Place placeFromDB = placeService.findPlaceById(place1.getId());

		placeFromDB.setRow(5);
		Place updatedPlace = placeService.updatePlace(placeFromDB);

		assertThat(updatedPlace.getRow()).isEqualTo(5);
	}
	
	@Test
	public void bookingPlaceTest() {

		Integer placeId = place1.getId();
		Integer customerId = customer.getId();

		Place bookedPlace = placeService.bookingPlace(placeId, customerId);

		assertThat(bookedPlace.getStatus()).isEqualTo(Status.booked);
		assertThat(bookedPlace.getCustomer()).isNotNull();
	}
	
	@Test
	public void cancellingPlaceTest() {
	
		Integer placeId = place1.getId();
		Integer customerId = customer.getId();

		placeService.bookingPlace(placeId, customerId);
		
		Place cancelledPlace = placeService.cancellingPlace(placeId, customerId);

		assertThat(cancelledPlace.getStatus()).isEqualTo(Status.free);
		assertThat(cancelledPlace.getCustomer()).isNull();
	}
	
	// attempt to cancel of booking of not yours ticket
	@Test(expected = InsufficientRightsException.class)
	public void InsufficientRightsToCancellingExceptionTest() {
	
		Integer placeId = place1.getId();
		Integer customerId = customer.getId();

		placeService.bookingPlace(placeId, customerId);
		
		customerFactory.setFields("newLogin", "newPassword", "newFirstName", "newLastName", "newEmail", UserRole.user);
		Customer newCust = customerFactory.newInstance();
		testEntityManager.persist(newCust);
		placeService.cancellingPlace(placeId, newCust.getId());

	}
	
	@Test
	public void busyPlaceTest() {
	
		Integer placeId = place1.getId();
		Integer customerId = customer.getId();

		placeService.bookingPlace(placeId, customerId);
		
		customerFactory.setFields("newLogin", "newPassword", "newFirstName", "newLastName", "newEmail", UserRole.admin);
		Customer newCust = customerFactory.newInstance();
		testEntityManager.persist(newCust);
		
		Place busyPlace = placeService.busyPlace(placeId, newCust.getId());
		
		assertThat(busyPlace.getStatus()).isEqualTo(Status.busy);
		assertThat(busyPlace.getCustomer().getId()).isEqualTo(customerId);

	}
	
	// attempt to get place busy for not admin user
	@Test(expected = InsufficientRightsException.class)
	public void InsufficientRightsToBusyExceptionTest() {
	
		Integer placeId = place1.getId();
		Integer customerId = customer.getId();

		placeService.bookingPlace(placeId, customerId);
		
		customerFactory.setFields("newLogin", "newPassword", "newFirstName", "newLastName", "newEmail", UserRole.user);
		Customer newCust = customerFactory.newInstance();
		testEntityManager.persist(newCust);
		
		placeService.busyPlace(placeId, newCust.getId());

	}
}
