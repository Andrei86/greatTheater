package bootsamples.exceptions;

public class MyBuisnessException extends Exception{
	
	String message;

	public MyBuisnessException(String mess) {
		
		this.message = mess;
	}

}
