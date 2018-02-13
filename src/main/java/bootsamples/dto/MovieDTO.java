package bootsamples.dto;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import bootsamples.model.Genre;
import lombok.Data;

@Data
public class MovieDTO {

	public MovieDTO() {
		// TODO Auto-generated constructor stub
	}
	
	private Integer id;
	@NotNull(message = "movie must have a title")
	private String title;
	@NotNull(message = "age bracket of movie can't be empty")
	private String ageBracket;
	@NotNull(message = "movie's duration field can't be empty")
	private Integer duration;
	@NotNull(message = "duration of movie can't be empty")
	private String description;
	@NotNull(message = "start rental date of movie can't be empty")
	private String startRentalDate;
	@NotNull(message = "end rental date of movie can't be empty")
	private String endRentalDate;
	@NotNull(message = "movie must have at least one genre")
	private List<String> genresNames;

}
