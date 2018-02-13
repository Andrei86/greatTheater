package bootsamples.factories;

import bootsamples.model.Genre;


/**
 * @author Andrei Shalkevich
 *
 */
public class GenreFactory {
	
	private String name;

	public GenreFactory() {

		name = "testGenreName1";
	}
	
	public Genre newInstance() {
		
		return new Genre(name);
	}
	
	public GenreFactory setName(String name){
		this.name = name;
		return this;
	}

}
