package bootsamples.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.CinemaFactory;
import bootsamples.model.Cinema;
import bootsamples.service.CinemaService;

/**
 * @author Andrei Shalkevich
 *
 */
public class CinemaServiceTest extends AbstractServiceTest{
	
	@Autowired
	private EntityManager testEntityManager;

	@Autowired
	private CinemaService cinemaService;
	
	public CinemaFactory cinemaFactory = new CinemaFactory();
	public Cinema cinema1 = cinemaFactory.newInstance();
	public Cinema cinema2 = cinemaFactory.setFields("testCinemaName2", "ul. Testov, 34", true).newInstance();
	
	
	@Before
	public void before(){
		
		testEntityManager.persist(cinema1.getCinemaSchema());
		testEntityManager.persist(cinema1);
		testEntityManager.persist(cinema2);
	
	}
	
	@Test
	public void findCinemaByCityTest(){

		List<Cinema> cinemasFromDB = cinemaService.findCinemaByCity(cinema1.getCity());
		
		assertThat(cinemasFromDB).hasSize(2);
	}
	
	@Test
	public void findCinemaByNameTest(){
		
		String cinemaName = cinema2.getName();
		Cinema cinemaFromDB = cinemaService.findCinemaByName(cinemaName);

		assertThat(cinemaFromDB.getName()).isEqualTo(cinemaName);
	}
	
	@Test
	public void findCinemaByIdTest(){

		Cinema cinemaFromDB = cinemaService.findCinemaById(cinema1.getId());
		
		assertThat(cinemaFromDB).isEqualTo(cinema1);
	}
	
	@Test
	public void findAllCinemasTest(){
		
		Pageable foundPage = new PageRequest(0, 3);
		List<Cinema> cinemaListFromDB = cinemaService.findAllCinemas(foundPage);
		
		assertThat(cinemaListFromDB).hasSize(2);
	}
	
	@Test(expected=MyResourceNotFoundException.class)
	public void deleteCinemaByIdTest(){
		
		Integer cinemaId = cinema1.getId();
		cinemaService.deleteCinemaById(cinemaId);
		
		cinemaService.findCinemaById(cinemaId);
	}
	
	@Test
	public void deleteAllCinemasTest()
	{
		cinemaService.deleteAllCinemas();
		
		Pageable foundPage = new PageRequest(0, 3);
		List<Cinema> cinemaListFromDB = cinemaService.findAllCinemas(foundPage);
		
		assertThat(cinemaListFromDB).hasSize(0);
	}
	
	@Test
	public void createCinemaTest(){
		String newName = "newTestCinemaName";
		Cinema newCinema = cinemaFactory.setFields(newName, "new Address", true).newInstance();

		Cinema createdCinema = cinemaService.createCinema(newCinema);
		
		assertThat(createdCinema.getName()).isEqualTo(newName);
	}
	
	@Test(expected = DuplicateEntityException.class)
	public void duplicateEntityExceptionTest() {

		String newName = "testCinemaName1";
		Cinema newCinema = cinemaFactory.setFields(newName, "new Address2", true).newInstance();

		cinemaService.createCinema(newCinema);

	}
	
	@Test
	public void updateCinemaTest(){
		
		Cinema cinemaFromDB = cinemaService.findCinemaByName(cinema1.getName());
		cinemaFromDB.setIsActive(false);
		
		Cinema updatedCinema = cinemaService.updateCinema(cinemaFromDB);
		
		assertThat(updatedCinema.getIsActive()).isEqualTo(false);
	} 
}
