package bootsamples.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CinemaDTO {

	public CinemaDTO() {
		// TODO Auto-generated constructor stub
	}
	
	private Integer id;
	@NotNull(message = "cinema name can not be empty")
	private String name;
	@NotNull(message = "city of the cinema can not be empty")
	private String city;
	@NotNull(message = "address can not be empty")
	private String address;
	@NotNull(message = "isActive status can not be empty")
	private Boolean isActive;
	@NotNull(message = "schema name can not be empty")
	private String schemaName;
	
}
