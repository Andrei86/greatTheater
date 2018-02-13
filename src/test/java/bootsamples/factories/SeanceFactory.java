package bootsamples.factories;

import java.util.Date;

import bootsamples.model.Cinema;
import bootsamples.model.Movie;
import bootsamples.model.Seance;

/**
 * @author Andrei Shalkevich
 *
 */
public class SeanceFactory {
	
	private Cinema cinema;
	private Movie movie;
	private Date date;
	
	public SeanceFactory() {
		
		cinema = new CinemaFactory().newInstance();
		movie = new MovieFactory().newInstance();
		date  = new Date(new Date().getTime() + 7200000); // because of the time limit
		
	}
	
	public Seance newInstance() {
		
		return new Seance(cinema, movie, date);
	}
	
	public SeanceFactory setFields(Cinema cinema, Movie movie) {
		this.cinema = cinema;
		this.movie = movie;
		return this;
	}

}
