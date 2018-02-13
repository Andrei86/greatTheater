package bootsamples.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetBookingDTO {

	
	public GetBookingDTO() {
		// TODO Auto-generated constructor stub
	}

	private Integer id;
	private String customerLogin; // для юзера может не быть, но для админа надо
	private List<PlaceDTO> placeDTO;
	private String bookingDate;
	
}
