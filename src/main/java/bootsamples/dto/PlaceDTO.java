package bootsamples.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

//в чистом виде места я создавать по отдельности не буду!
@Data
public class PlaceDTO {

	public PlaceDTO() {
		// TODO Auto-generated constructor stub
	}
	
	private Integer id; // необязательный атрибут
	@NotNull(message = "Place must necessarily have seance id")
	private Integer seanceId;
	private String cinema;
	private String movie;
	private String seanceDate;
	@NotNull(message = "Place must necessarily have number of row")
	private Integer row;
	@NotNull(message = "Place must necessarily have number")
	private Integer place; // rename to num
	@NotNull(message = "Place must necessarily have status")
	private String status;
	@NotNull(message = "Place must necessarily have category")
	private String category;
	private Double cost;
	
}
