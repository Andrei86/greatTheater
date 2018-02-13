package bootsamples.exceptions.dataIntegrityViolation;

// этой ошибки мож не быть, т.к. при поиске схемы выдаст, что таковой нет -))
public class MyConstraintException extends RuntimeException{
	
	private String resourceName; // is need it field?

	public MyConstraintException(String message, String resourceName) {
		// TODO Auto-generated constructor stub
		
		super(message);
		this.resourceName = resourceName;
	}

}
