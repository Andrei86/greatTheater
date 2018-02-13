package bootsamples.factories;

import bootsamples.model.CinemaSchema;


/**
 * @author Andrei Shalkevich
 *
 */
public class SchemaFactory {
	
	private Integer rowsNumber;
	private Integer placesNumber;
	private String name;
	
	public SchemaFactory() {
		rowsNumber = 10;
		placesNumber = 20;
		name = "testSchemaName1";
	}

	public CinemaSchema newInstance() {
		
		return new CinemaSchema(rowsNumber, placesNumber, name);
	}
	
	
	public SchemaFactory setName(String name){
		this.name = name;
		return this;
	}

}
