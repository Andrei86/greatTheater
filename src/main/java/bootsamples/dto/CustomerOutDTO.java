package bootsamples.dto;

import lombok.Data;

@Data
public class CustomerOutDTO {
	
	private Integer id;
	private String login;
	private String firstName;
	private String lastName;
	private String eMail;
	
	public CustomerOutDTO() {
		// TODO Auto-generated constructor stub
	}

}
