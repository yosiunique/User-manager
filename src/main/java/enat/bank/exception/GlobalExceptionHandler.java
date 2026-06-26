package enat.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeExceptions(RuntimeException ex) {

        ApiErrorResponse response = new ApiErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());

        if (ex instanceof UserAlreadyExistException) {
            response.setError("User error");

        }
        else if (ex instanceof SavingAndLoanRepaymentSaveFileException) {
            response.setError("Saving Error");
        } else if(ex instanceof  SavingAndLoanRepaymentsNotFoundException){
            response.setError("Saving Error");
        }
        else if (ex instanceof  RolesNotFoundException) {
            response.setError("Role Error");
        } else {
            response.setError("Unhandled Exception is occur !");
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}