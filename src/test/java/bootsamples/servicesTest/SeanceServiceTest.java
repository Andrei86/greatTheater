package bootsamples.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import bootsamples.adds.ZeroedDateTestPerformer;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.CinemaFactory;
import bootsamples.factories.SeanceFactory;
import bootsamples.model.Cinema;
import bootsamples.model.Genre;
import bootsamples.model.Seance;
import bootsamples.service.SeanceService;

/**
 * @author Andrei Shalkevich
 *
 */
public class SeanceServiceTest extends AbstractServiceTest{
	
	@Autowired
	private EntityManager testEntityManager;

	@Autowired
	private SeanceService seanceService;
	
	public CinemaFactory cinemaFactory = new CinemaFactory();
	public Cinema cinema2 = cinemaFactory.setFields("testCinemaName2", "ul. Testov, 34", true).newInstance();

	public SeanceFactory seanceFactory = new SeanceFactory();
	public Seance seance1 = seanceFactory.newInstance();
	public Seance seance2 = null;
	
	public ZeroedDateTestPerformer performer = new ZeroedDateTestPerformer(new Date());
	
	
	@Before
	public void before(){
		
		testEntityManager.persist(seance1.getCinema().getCinemaSchema());
		testEntityManager.persist(seance1.getCinema());
		
		cinema2.setCinemaSchema(seance1.getCinema().getCinemaSchema());
		testEntityManager.persist(cinema2);
		
		for(Genre genre : seance1.getMovie().getGenres())
			testEntityManager.persist(genre);
		
		testEntityManager.persist(seance1.getMovie());
		
		seance2 = seanceFactory.setFields(cinema2, seance1.getMovie()).newInstance();
		
		testEntityManager.persist(seance1);
		testEntityManager.persist(seance2);
	
	}
	
	@Test
	public void findSeanceByIdTest(){

		Seance seanceFromDB = seanceService.findSeanceById(seance1.getId());
		
		assertThat(seanceFromDB).isEqualTo(seance1);
	}

	@Test
	public void findSeanceByMovieDateTest() {
		
		String movieTitle = seance1.getMovie().getTitle();
		List<Seance> seanceListFromDB = seanceService.findSeanceByMovieDate(movieTitle, performer.zeroedDate());

		assertThat(seanceListFromDB).hasSize(2);
	}

	@Test
	public void findSeanceByCinemaDateTest() {

		String cinemaName = seance1.getCinema().getName();
		List<Seance> seanceListFromDB = seanceService.findSeanceByCinemaDate(cinemaName, performer.zeroedDate());

		assertThat(seanceListFromDB).hasSize(1);
	}
	
	
	@Test
	public void findSeanceByDateTest() {

		List<Seance> seanceListFromDB = seanceService.findSeanceByDate(performer.zeroedDate());

		assertThat(seanceListFromDB).hasSize(2);

	}
	
	@Test(expected=MyResourceNotFoundException.class)
	public void deleteSeanceByIdTest(){
		
		Integer seanceId = seance1.getId();
		seanceService.deleteSeanceById(seanceId);
		
		seanceService.findSeanceById(seanceId);
	}
	
	@Test
	public void createSeanceTest(){
		Seance seance = new Seance();
		seance.setCinema(seance1.getCinema());
		seance.setMovie(seance1.getMovie());
		seance.setDate(performer.addOneDayToDate());

		Seance createdSeance = seanceService.createSeance(seance);
		
		assertThat(createdSeance.getId()).isNotNull();
	}
	
	@Test
	public void updateSeanceTest(){
		
		
		Seance seanceFromDB = seanceService.findSeanceById(seance1.getId());
		
		seanceFromDB.setDate(performer.addOneDayToDate());
		seanceService.updateSeance(seanceFromDB);
		
		List<Seance> seanceListFromDB = seanceService.findSeanceByDate(performer.zeroedDate());
		
		assertThat(seanceListFromDB).hasSize(1);
	} 
}
