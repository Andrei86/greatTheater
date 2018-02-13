package bootsamples.factories;

import bootsamples.model.Cinema;
import bootsamples.model.CinemaSchema;

/**
 * @author Andrei Shalkevich
 *
 */
public class CinemaFactory {
	
	private String name;
	private String city;
	private String address;
	private Boolean isActive;
	private CinemaSchema cinemaSchema;
	
	public CinemaFactory() {
		
		name = "testCinemaName1";
		city = "Grodno";
		address = "Kalinovskogo, 34";
		isActive = true;
		cinemaSchema = new SchemaFactory().newInstance();
		
	}
	
	public Cinema newInstance() {
		return new Cinema(name, city, address, isActive, cinemaSchema);
	}
	
	public CinemaFactory setFields(String name, String address, Boolean isActive) {
		this.name = name;
		this.address = address;
		this.isActive = isActive;
		return this;
	}

}
