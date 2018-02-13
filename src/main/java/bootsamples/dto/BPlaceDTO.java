package bootsamples.dto;

import java.util.List;

import lombok.Data;

@Data
public class BPlaceDTO {
	
	List<Integer> placesId;
	Integer customerId;

	public BPlaceDTO() {
		// TODO Auto-generated constructor stub
	}

}
