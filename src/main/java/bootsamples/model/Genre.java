package bootsamples.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


/**
 * @author Andrei Shalkevich
 *
 */
@Entity
@Data
@Table(name="genre")
public class Genre {

	public Genre() {
		super();
	}
	
	public Genre(String name) {
		super();
		this.name = name;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;

}
