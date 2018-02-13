package bootsamples.exceptions;

public class MyException extends Exception{
	
	private final static String realMsg = "There is no entity with such id to delete";
	
	public MyException() {

		super(realMsg);
		
	}

}
