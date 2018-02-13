package bootsamples.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Andrei Shalkevich
 *
 */
@Data
@Entity
@Table(name="cinema")
public class Cinema {
	
	public Cinema() {
		super();
	}

	public Cinema(String name, String city, String address, Boolean isActive, CinemaSchema cinemaSchema) {
		super();
		this.name = name;
		this.city = city;
		this.address = address;
		this.isActive = isActive;
		this.cinemaSchema = cinemaSchema;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String city;
	private String address;
	private Boolean isActive;
	
	@ManyToOne//(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private CinemaSchema cinemaSchema;
	
	
}
