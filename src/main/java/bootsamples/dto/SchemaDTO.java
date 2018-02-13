package bootsamples.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SchemaDTO {

	public SchemaDTO() {
		// TODO Auto-generated constructor stub
	}
	private Integer id;
	@NotNull(message = "number of rows can not be empty")
	@Min(message = "number of rows must be more than 6", value = 6)
	private Integer rowsNumber;
	@NotNull(message = "number of places can not be empty")
	@Min(message = "number of places must be more than 10", value = 10)
	private Integer placesNumber;
	@NotNull(message = "schema name can not be empty")
	private String name;

}
