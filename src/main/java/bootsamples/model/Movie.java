package bootsamples.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;


/**
 * @author Andrei Shalkevich
 *
 */
@Entity
@Data
@Table(name="movie")
public class Movie {
	
	public Movie() {
		super();
	}

	public Movie(String title, String ageBracket, Integer duration, String description, Date startRentalDate,
			Date endRentalDate, List<Genre> genres) {
		super();
		this.title = title;
		this.ageBracket = ageBracket;
		this.duration = duration;
		this.description = description;
		this.startRentalDate = startRentalDate;
		this.endRentalDate = endRentalDate;
		this.genres = genres;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String ageBracket;
	private Integer duration;
	private String description;
	private Date startRentalDate;
	private Date endRentalDate;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="movie_genre", joinColumns=@JoinColumn(name="movie_id"), 
			inverseJoinColumns=@JoinColumn(name="genre_id"))
	private List<Genre> genres;

}
		