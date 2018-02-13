package bootsamples.exceptions.dateException;

public class MyDateFormatException extends RuntimeException{
	
	private String message;

	public MyDateFormatException(String message) {
		super(message);
	}

}
