package bootsamples.service;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bootsamples.dao.MovieRepository;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;
import bootsamples.model.Genre;
import bootsamples.model.Movie;

/**
 * @author Andrei Shalkevich
 *
 */
@Service
public class MovieService {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MovieService.class);

	private final MovieRepository movieRepository;
	private final GenreService genreService;

	public MovieService(MovieRepository movieRepository, GenreService genreService) {
		this.movieRepository = movieRepository;
		this.genreService = genreService;
	}
	
	public Movie findMovieById(Integer id) {
		
		LOGGER.info("Find movie by id = {} ", id);
		
		Movie movie = movieRepository.findOne(id);

		if (movie == null) {
			throw new MyResourceNotFoundException(String.format("movie with id= %s not found", id));
		}

		return movie;
	}
	
	public Movie findMovieByTitle(String title) {
		
		LOGGER.info("Find movie by title = {} ", title);
		
		Movie movie = movieRepository.findByTitle(title);

		if (movie == null) {
			throw new MyResourceNotFoundException(String.format("movie with title  %s not found", title));
		}

		return movie;
	}
	
	public List<Movie> findAllMovies(Pageable pageable) {
		
		LOGGER.info("Find all movies");

		List<Movie> movies = null;

		Page<Movie> page = movieRepository.findAll(pageable);

		movies = page.getContent();

		return movies;
	}
	
	@Transactional
	public void deleteMovieById(Integer id) {
		
		LOGGER.info("Delete movie by id = {} ", id);
		
		findMovieById(id);

		movieRepository.delete(id);
	}
	
	@Transactional
	public Movie createMovie(Movie movie) {
		
		LOGGER.info("Create movie with title = {} ", movie.getTitle());
		
		List<Genre> genres = movie.getGenres();

		try {
			findMovieByTitle(movie.getTitle());
		} catch (MyResourceNotFoundException e) {

			for (Genre genre : genres)
				genreService.findGenreByName(genre.getName());

			return movieRepository.save(movie);

		}
		throw new DuplicateEntityException("There is already such movie object exist");
	}
	
	@Transactional
	public Movie updateMovie(Movie movie) {
		
		LOGGER.info("Update movie with id = {} ", movie.getId());
		
		return movieRepository.save(movie);
	}
	
	@Transactional
	public void deleteAllMovies() {
		
		LOGGER.info("Delete all movies");
		
		movieRepository.deleteAll();
	}
	
	public List<Movie> findMoviesByDate(Date date){
		
		LOGGER.info("Find movie by date = {} ", date);
		
		List<Movie> movies = movieRepository.findByDate(date);
		
		if(movies.isEmpty()){
			throw new MyResourceNotFoundException(String.format("no movies found for date %s", date));
		}
		return movies;
	}

}
