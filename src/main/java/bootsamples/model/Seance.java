package bootsamples.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;


/**
 * @author Andrei Shalkevich
 *
 */
@Entity
@Data
@Table(name="seance")
public class Seance {
	
	public Seance() {
		super();
	}
	
	public Seance(Cinema cinema, Movie movie, Date date) {
		super();
		this.cinema = cinema;
		this.movie = movie;
		this.date = date;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@OneToOne
	@JoinColumn(name="cinema_id")
	private Cinema cinema;
	@OneToOne
	@JoinColumn(name="movie_id")
	private Movie movie;
	private Date date; // запихиваем правильную дату со временем

}
