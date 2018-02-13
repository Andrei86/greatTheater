package bootsamples.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ValidationUtil {

	public ValidationUtil() {
		// TODO Auto-generated constructor stub
	}

	public static List<String> fromBindingErrors(Errors errors){
		List<String> validErrors = new ArrayList<>();
		
		for(ObjectError objectError : errors.getAllErrors()){
			validErrors.add(objectError.getDefaultMessage());
		}
		
		return validErrors;
	}
}
