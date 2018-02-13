package bootsamples.exceptions.duplicate;

public class DuplicateEntityException extends RuntimeException{

	private String message; 
	
	public DuplicateEntityException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}

}
