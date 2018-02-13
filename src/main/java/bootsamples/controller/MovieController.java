package bootsamples.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bootsamples.model.Genre;
import bootsamples.model.Movie;
import bootsamples.service.GenreService;
import bootsamples.service.MovieService;
import bootsamples.dto.IdDTO;
import bootsamples.dto.MovieDTO;
import bootsamples.exceptions.dateException.MyDateFormatException;
import bootsamples.exceptions.notFound.MyResourceNotFoundException;

@RestController
@RequestMapping("/movies")
public class MovieController {
	
	@Autowired
	MovieService movieService;
	
	@Autowired
	GenreService genreService; // это должно быть в service!!!!!????????
	
	private Pattern patt = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMovieById(@PathVariable("id") Integer id)
	{
		Movie movie = movieService.findMovieById(id);
		
		MovieDTO movieDTO = entity2dto(movie);
		
		return new ResponseEntity<MovieDTO>(movieDTO, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getMovie(@PageableDefault Pageable pageable, 
			@RequestParam(required=false) String title,
			@RequestParam(required=false) String date){ // переименовать метод
		
		Date utilDate = null;
		
		MovieDTO movieDTO = null;
		List<MovieDTO> moviesDTO = new ArrayList<MovieDTO>();
		List<Movie> movies = new ArrayList<Movie>();
		
		if(title != null){
			Movie movie = movieService.findMovieByTitle(title);		
			if (movie == null) {
				throw new MyResourceNotFoundException(String.format("movie with title  %s not found", title));
			}
			movieDTO = entity2dto(movie);		
			return new ResponseEntity<MovieDTO>(movieDTO, HttpStatus.OK);
		}
		else if(date != null){
			
			if(!(patt.matcher(date).matches()))
				throw new MyDateFormatException("Please, insert date in format dd-mm-yyyy"); // это не надо т.к. есть валидация
			else
			try {
				utilDate = df.parse(date);
			} catch (Exception e) {}
				
				movies = movieService.findMoviesByDate(utilDate);
			}
		else
			movies = movieService.findAllMovies(pageable);// тут можно отдавать фильмы которые сегодня в прокате
		
		for(Movie movie : movies)
		{
			moviesDTO.add(entity2dto(movie));
		}
		
		return new ResponseEntity<List<MovieDTO>>(moviesDTO, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMovieById(@PathVariable("id") Integer id){
		
		movieService.deleteMovieById(id);
		
		return new ResponseEntity<Movie>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createMovie(@Valid @RequestBody MovieDTO movieDTO){
		
		if(!patt.matcher(movieDTO.getStartRentalDate()).matches())
			throw new MyDateFormatException("Please, insert startRentalDate in format dd-mm-yyyy");
		else if(!patt.matcher(movieDTO.getEndRentalDate()).matches())
			throw new MyDateFormatException("Please, insert endRentalDate in format dd-mm-yyyy");
		
		Movie movie = dto2entity(movieDTO);

		movieService.createMovie(movie);
		
		//SchemaDTO createdSchemaDTO = new SchemaDTO();
		
		return new ResponseEntity<IdDTO>(new IdDTO(movie.getId()), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT) // it is no need to update movie
	public ResponseEntity<?> updateMovie(@PathVariable("id") Integer id, /*@Valid*/ @RequestBody MovieDTO movieDTO){
		
		Movie movieDB = movieService.findMovieById(id);
		MovieDTO movieDBDTO = entity2dto(movieDB);
		// немного неправильно гонять туда-сюда dto2entity и entity2dto??
		Boolean boolStartRental = false;
		Boolean boolEndRental = false;
		
		if((movieDTO.getStartRentalDate() != null) && patt.matcher(movieDTO.getStartRentalDate()).matches())
			boolStartRental = true;
		else
			throw new MyDateFormatException("Please, insert startRentalDate in format dd-mm-yyyy");
		
		if ((movieDTO.getEndRentalDate() != null) && patt.matcher(movieDTO.getEndRentalDate()).matches())
			boolEndRental = true;
		else
			throw new MyDateFormatException("Please, insert endRentalDate in format dd-mm-yyyy");
		
		movieDBDTO.setTitle(movieDTO.getTitle() != null ? movieDTO.getTitle() 
		: movieDBDTO.getTitle());
		movieDBDTO.setAgeBracket(movieDTO.getAgeBracket() != null ? movieDTO.getAgeBracket() 
		: movieDBDTO.getAgeBracket());
		movieDBDTO.setDuration(movieDTO.getDuration() != null ? movieDTO.getDuration() 
		: movieDBDTO.getDuration());
		movieDBDTO.setDescription(movieDTO.getDescription() != null ? movieDTO.getDescription() 
		: movieDBDTO.getDescription());
		
		// если данные даты обновления подходят под формат, то обновляем, если нет - то оставляем предыдущую дату
		movieDBDTO.setStartRentalDate(boolStartRental ? movieDTO.getStartRentalDate() 
		: movieDBDTO.getStartRentalDate());
		
		// если данные даты обновления подходят под формат, то обновляем, если нет - то оставляем предыдущую дату
		movieDBDTO.setEndRentalDate(boolEndRental ? movieDTO.getEndRentalDate() 
		: movieDBDTO.getEndRentalDate());
		
		movieDBDTO.setGenresNames(movieDTO.getGenresNames() != null ? movieDTO.getGenresNames() 
		: movieDBDTO.getGenresNames());
		
		Movie movieNew = dto2entity(movieDBDTO);
		
		movieService.updateMovie(movieNew);
		
		return new ResponseEntity<MovieDTO>(HttpStatus.OK);
	}
	
	private Movie dto2entity(MovieDTO movieDTO) {
		Movie movie = new Movie();
		
		if (movieDTO.getId() != null)
			movie.setId(movieDTO.getId());

		movie.setTitle(movieDTO.getTitle());
		movie.setAgeBracket(movieDTO.getAgeBracket());
		movie.setDuration(movieDTO.getDuration());
		movie.setDescription(movieDTO.getDescription());
		
		try {

			movie.setStartRentalDate(df.parse(movieDTO.getStartRentalDate()));
			movie.setEndRentalDate(df.parse(movieDTO.getEndRentalDate()));
		} catch (Exception e) {
		}
		
		List<Genre> genres = new ArrayList<Genre>();
		
		for(String genreName : movieDTO.getGenresNames())
			genres.add(genreService.findGenreByName(genreName)); // возвращает список
		
		movie.setGenres(genres);

		return movie;
	}

	private MovieDTO entity2dto(Movie movie) {
		
		MovieDTO movieDTO = new MovieDTO();
		
		List<String> genresNames = new ArrayList<String>();

		movieDTO.setId(movie.getId());
		movieDTO.setTitle(movie.getTitle());
		movieDTO.setAgeBracket(movie.getAgeBracket());
		movieDTO.setDuration(movie.getDuration());
		movieDTO.setDescription(movie.getDescription());
		movieDTO.setStartRentalDate(df.format(movie.getStartRentalDate()));
		movieDTO.setEndRentalDate(df.format(movie.getEndRentalDate()));
		
		List<Genre> genreObjects = movie.getGenres();
		for(Genre genre: genreObjects)
		genresNames.add(genre.getName());
		
		movieDTO.setGenresNames(genresNames);

		return movieDTO;
	}
}
