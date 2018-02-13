package bootsamples.dto;

import java.util.List;

import lombok.Data;

@Data
public class PostBookingDTO {

	public PostBookingDTO() {
		// TODO Auto-generated constructor stub
	}
	
	//private Integer id;
	private Integer customerId;
	private List<Integer> placeIds;

}
