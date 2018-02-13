package bootsamples.dto;

import lombok.Data;

@Data
public class IdDTO {

	private Integer id;
	
	public IdDTO(Integer id) {
		
		this.id = id;
	}

}
