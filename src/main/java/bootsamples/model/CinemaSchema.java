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
@Table(name="cinema_schema")
public class CinemaSchema {
	
	public CinemaSchema() {
	super();
	}
	
	public CinemaSchema(Integer rowsNumber, Integer placesNumber, String name) {
		super();
		this.rowsNumber = rowsNumber;
		this.placesNumber = placesNumber;
		this.name = name;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private Integer rowsNumber;
	private Integer placesNumber;
	private String name;
	
}
