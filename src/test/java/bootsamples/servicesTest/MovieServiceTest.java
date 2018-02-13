package bootsamples.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.MovieFactory;
import bootsamples.model.Genre;
import bootsamples.model.Movie;
import bootsamples.service.MovieService;

/**
 * @author Andrei Shalkevich
 *
 */
public class MovieServiceTest extends AbstractServiceTest{
	
	@Autowired
	private EntityManager testEntityManager;

	@Autowired
	private MovieService movieService;
	
	public MovieFactory movieFactory = new MovieFactory();
	public Movie movie1 = movieFactory.newInstance();
	public Movie movie2 = movieFactory.setFields("testMovieTitle2", "12+", 115, "good movie2").newInstance();
	
	
	
	@Before
	public void before() throws InterruptedException{
	for(Genre genre : movie1.getGenres())
		testEntityManager.persist(genre);
		
		testEntityManager.persist(movie1);
		testEntityManager.persist(movie2);
		
	}
	
	@Test
	public void findMovieByIdTest(){

		Movie movieFromDB = movieService.findMovieById(movie1.getId());
		
		assertThat(movieFromDB).isEqualTo(movie1);
	}
	
	@Test
	public void findMovieByTitleTest(){
		
		String movieTitle = movie2.getTitle();
		Movie movieFromDB = movieService.findMovieByTitle(movieTitle);

		assertThat(movieFromDB.getTitle()).isEqualTo(movieTitle);
	}
	
	@Test
	public void findMovieByDateTest(){
		
		Date now = new Date();
		List<Movie> movieListFromDB = movieService.findMoviesByDate(now);

		assertThat(movieListFromDB).hasSize(2);
	}
	
	@Test
	public void findAllMoviesTest(){
		
		Pageable foundPage = new PageRequest(0, 3);
		List<Movie> movieListFromDB = movieService.findAllMovies(foundPage);
		
		assertThat(movieListFromDB).hasSize(2);
	}
	
	@Test(expected=MyResourceNotFoundException.class)
	public void deleteMovieByIdTest(){
		
		Integer movieId = movie1.getId();
		movieService.deleteMovieById(movieId);
		
		movieService.findMovieById(movieId);
	}
	
	@Test
	public void deleteAllMoviesTest()
	{
		movieService.deleteAllMovies();
		
		Pageable foundPage = new PageRequest(0, 3);
		List<Movie> movieListFromDB = movieService.findAllMovies(foundPage);
		
		assertThat(movieListFromDB).hasSize(0);
	}
	
	@Test
	public void createMovieTest(){
		String newTitle = "newTestMovieTitle";
		Movie newMovie = movieFactory.setFields(newTitle, "6+", 114, "cool movie").newInstance();

		Movie createdMovie = movieService.createMovie(newMovie);
		
		assertThat(createdMovie.getTitle()).isEqualTo(newTitle);
	}
	
	@Test(expected = DuplicateEntityException.class)
	public void duplicateEntityExceptionTest() {

		String newTitle = "testMovieTitle1";
		Movie newMovie = movieFactory.setFields(newTitle, "6+", 114, "cool movie").newInstance();

		movieService.createMovie(newMovie);

	}
	
	@Test
	public void updateMovieTest(){
		
		Movie movieFromDB = movieService.findMovieByTitle(movie1.getTitle());
		String newAgeBracket = "10+";
		movieFromDB.setAgeBracket(newAgeBracket);
		
		Movie updatedMovie = movieService.updateMovie(movieFromDB);
		
		assertThat(updatedMovie.getAgeBracket()).isEqualTo("10+");
	} 
}
