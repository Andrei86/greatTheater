package bootsamples.factories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bootsamples.model.Genre;
import bootsamples.model.Movie;


/**
 * @author Andrei Shalkevich
 *
 */
public class MovieFactory {
	
	private String title;
	private String ageBracket;
	private Integer duration;
	private String description;
	private Date startRentalDate;
	private Date endRentalDate;
	
	private List<Genre> genres;

	public MovieFactory() {
		
		title = "testMovieTitle1";
		ageBracket = "16+";
		duration = 129;
		description  = "good movie1";
		startRentalDate = new Date();
		endRentalDate = new Date(startRentalDate.getTime() + 604800000);
		
		genres = new ArrayList<>();
		genres.add(new GenreFactory().newInstance());
		
		
	}
	
	public Movie newInstance() {
		return new Movie(title, ageBracket, duration, description, startRentalDate, 
				endRentalDate, genres);
	}

	public MovieFactory setFields(String title, String ageBracket, 
			Integer duration, String description) {
		
		this.title = title;
		this.ageBracket = ageBracket;
		this.duration = duration;
		this.description = description;
		return this;
		
	}
}
