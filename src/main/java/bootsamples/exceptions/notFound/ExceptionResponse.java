package bootsamples.exceptions.notFound;

import java.util.List;

import lombok.Data;

@Data
public class ExceptionResponse {

	private String errorCode;
	private String errorMessage;
	private List<String> errors;
	
	public ExceptionResponse() {
		// TODO Auto-generated constructor stub
	}

}
