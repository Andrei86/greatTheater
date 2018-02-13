package bootsamples.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CustomerLoginDTO {
	
	@NotNull(message = "insert login")
	private String login;
	@NotNull(message = "insert password")
	private String password;

}
