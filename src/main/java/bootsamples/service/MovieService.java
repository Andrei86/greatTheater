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
@Transactional
public class MovieService {

	private final MovieRepository movieRepository;
	private final GenreService genreService;

	public MovieService(MovieRepository movieRepository, GenreService genreService) {
		this.movieRepository = movieRepository;
		this.genreService = genreService;
	}
	
	public Movie findMovieById(Integer id) {
		Movie movie = movieRepository.findOne(id);

		if (movie == null) {
			throw new MyResourceNotFoundException(String.format("movie with id= %s not found", id));
		}

		return movie;
	}
	
	public Movie findMovieByTitle(String title) {

		Movie movie = movieRepository.findByTitle(title);

		if (movie == null) {
			throw new MyResourceNotFoundException(String.format("movie with title  %s not found", title));
		}

		return movie;
	}
	
	public List<Movie> findAllMovies(Pageable pageable) {

		List<Movie> movies = null;

		Page<Movie> page = movieRepository.findAll(pageable);

		movies = page.getContent();

		return movies;
	}
	
	public void deleteMovieById(Integer id) {
		findMovieById(id);

		movieRepository.delete(id);
	}

	public Movie createMovie(Movie movie) {
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
	
	public Movie updateMovie(Movie movie) {

		return movieRepository.save(movie);
	}
	
	public void deleteAllMovies() {
		movieRepository.deleteAll();
	}
	
	public List<Movie> findMoviesByDate(Date date){
		
		List<Movie> movies = movieRepository.findByDate(date);
		
		if(movies.isEmpty()){
			throw new MyResourceNotFoundException(String.format("no movies found for date %s", date));
		}
		return movies;
	}

}
