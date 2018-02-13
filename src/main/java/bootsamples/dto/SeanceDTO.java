package bootsamples.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SeanceDTO {

	private Integer id;
	@NotNull(message="seance must have a cinema")
	private String cinemaName;
	@NotNull(message="seance must have a movie")
	private String movieTitle;
	@NotNull(message="seance must have a date and time")
	private String date; // в чем плюс работы со строкой сдесь?
	//private String time; // ??
	
}
