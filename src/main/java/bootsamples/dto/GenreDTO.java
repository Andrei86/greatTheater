package bootsamples.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class GenreDTO {
	
	private Integer id;
	@NotNull(message="genre name can't be empty")
	private String name;

}
