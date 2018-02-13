package bootsamples.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CategoryCostDTO {
	
	private Integer id;
	@NotNull(message="category must not to be null")
	private String category;
	@NotNull(message="cost must not to be null")
	private Double cost;

	public CategoryCostDTO() {
		// TODO Auto-generated constructor stub
	}

}
