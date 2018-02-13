package bootsamples.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import lombok.Data;

@Data
public class CustomerInDTO {

	private Integer id;
	@NotNull(message = "login can't be null")
	private String login;
	@NotNull(message = "password can't be null")
	private String password; // придумать чтобы не было в dto - отдельный dto
	@NotNull(message = "first name can't be null")
	@Size(min=2)
	private String firstName;
	@NotNull(message = "last name can't be null")
	@Size(min=2)
	private String lastName;
	@NotNull(message = "email can't be null")
	@Email(message = "There is not exist such e-mail address")
	private String email;
	
}
