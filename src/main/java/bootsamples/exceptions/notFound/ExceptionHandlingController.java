package bootsamples.exceptions.notFound;

import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import bootsamples.exceptions.InappropirateStatusException;
import bootsamples.exceptions.InsufficientRightsException;
import bootsamples.exceptions.IntegrityViolationException;
import bootsamples.exceptions.LatenessCancelException;
import bootsamples.exceptions.dataIntegrityViolation.MyConstraintException;
import bootsamples.exceptions.dateException.MyDateFormatException;
import bootsamples.exceptions.duplicate.DuplicateEntityException;
import bootsamples.exceptions.noEnum.NoEnumValueException;
import bootsamples.util.ValidationUtil;

@ControllerAdvice
public class ExceptionHandlingController {

	public ExceptionHandlingController() {
		// TODO Auto-generated constructor stub
	}
	
	@ExceptionHandler(MyResourceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> resourceNotFound(MyResourceNotFoundException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Not Found");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex)
	{
		BindingResult result = ex.getBindingResult();
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Validation Error");
		response.setErrorMessage("Invalid or missing inputs");
		response.setErrors(ValidationUtil.fromBindingErrors(result));
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DuplicateEntityException.class)
	public ResponseEntity<ExceptionResponse> duplicateInput(DuplicateEntityException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Duplicate Input");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MyConstraintException.class)
	public ResponseEntity<ExceptionResponse> constraintValidationInput(MyConstraintException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Constraint Validation Input");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MyDateFormatException.class)
	public ResponseEntity<ExceptionResponse> incorrectDateFormat(MyDateFormatException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Incorrect date format");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoEnumValueException.class)
	public ResponseEntity<ExceptionResponse> noEnumValueInput(NoEnumValueException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("No enum value input");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InappropirateStatusException.class)
	public ResponseEntity<ExceptionResponse> noValidStatusInput(InappropirateStatusException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Inappropirate status");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(LatenessCancelException.class) // можно убирать, т.к. это определяется статусом
	public ResponseEntity<ExceptionResponse> tooLateCancelPlaceInput(LatenessCancelException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("It is too late for booking cancelling");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InsufficientRightsException.class)
	public ResponseEntity<ExceptionResponse> insufficientRightsUserInput(InsufficientRightsException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Insufficient rights user input");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(IntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> inconsistentAttemptToDelete(IntegrityViolationException ex)
	{
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("Data integrity violation exception");
		response.setErrorMessage(ex.getMessage());
		
		return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
	}
}
