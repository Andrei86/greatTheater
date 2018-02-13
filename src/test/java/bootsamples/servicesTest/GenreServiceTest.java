package bootsamples.servicesTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.factories.GenreFactory;
import bootsamples.model.Genre;
import bootsamples.service.GenreService;


/**
 * @author Andrei Shalkevich
 *
 */
public class GenreServiceTest extends AbstractServiceTest{

	@Autowired
	private EntityManager testEntityManager;
	
	@Autowired
	private GenreService genreService;

	private GenreFactory genreFactory = new GenreFactory();
	private Genre genre1 = genreFactory.newInstance();
	private Genre genre2 = genreFactory.setName("testGenreName2").newInstance();
	
	
	@Before
	public void before() {
		testEntityManager.persist(genre1);
		testEntityManager.persist(genre2);
	}
	
	@Test
	public void findGenreByIdTest(){

		Genre genreFromDB = genreService.findGenreById(genre1.getId());
		
		assertThat(genreFromDB).isEqualTo(genre1);
	}
	
	@Test
	public void findGenreByNameTest(){

		Genre genreFromDB = genreService.findGenreByName(genre2.getName());

		assertThat(genreFromDB).isEqualTo(genre2);
	}
	
	
	@Test
	public void findAllGenresTest(){
		
		Pageable foundPage = new PageRequest(0, 3);
		List<Genre> genreListFromDB = genreService.findAllGenres(foundPage);
		
		assertThat(genreListFromDB).hasSize(2);
	}	

	@Test(expected=MyResourceNotFoundException.class)
	public void deleteGenreByIdTest(){
		
		Integer genreId = genre1.getId();
		genreService.deleteGenreById(genreId);
		
		genreService.findGenreById(genreId);
	}
	
	@Test
	public void deleteAllGenresTest()
	{
		genreService.deleteAllGenres();
		
		Pageable foundPage = new PageRequest(0, 3);
		List<Genre> genreListFromDB = genreService.findAllGenres(foundPage);
		
		assertThat(genreListFromDB).hasSize(0);
	}
	
	@Test
	public void createGenreTest(){
		String newName = "newTestGenreName";
		Genre newGenre = genreFactory.setName(newName).newInstance();

		Genre createdGenre = genreService.createGenre(newGenre);
		
		assertThat(createdGenre.getName()).isEqualTo(newName);
	}
	
	@Test
	public void updateGenreTest(){
		
		Genre genreFromDB = genreService.findGenreByName(genre1.getName());
		genreFromDB.setName("updatedName");
		
		Genre updatedGenre = genreService.updateGenre(genreFromDB);
		
		assertThat(updatedGenre.getName()).isEqualTo("updatedName");
	} 

}
