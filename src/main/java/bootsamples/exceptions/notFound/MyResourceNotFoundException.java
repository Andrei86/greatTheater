package bootsamples.exceptions.notFound;

import lombok.Data;

@Data
public class MyResourceNotFoundException extends RuntimeException {

	//private Integer resourceId;
	
	public MyResourceNotFoundException(/*Integer resourceId, */String message) {

		super(message);
		//this.resourceId = resourceId;
	}

}
