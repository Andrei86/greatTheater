package bootsamples.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import bootsamples.additional.UserRole;
import lombok.Data;

/**
 * @author Andrei Shalkevich
 *
 */
@Data
@Entity
@Table(name = "customer")
public class Customer {
	
	public Customer() {
		super();
	}

	public Customer(String login, String password, String firstName, String lastName, String email, UserRole userRole) {
		super();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userRole = userRole;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String login;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "user_role")
	private UserRole userRole;
	
}
