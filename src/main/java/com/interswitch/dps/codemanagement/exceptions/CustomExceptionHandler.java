package com.interswitch.dps.codemanagement.exceptions;

import com.interswitch.dps.codemanagement.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.interswitch.dps.codemanagement.response.CustomResponse.CustomResponseBuilder;

import java.util.Date;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // server error 500-internal server error

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CustomResponse<?>> handleAllExceptions(Exception ex, WebRequest request) {

        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("500").withMessage(ex.getMessage()).withTimestamp(new Date())
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomResponse<?>> handleResponseStatusExceptions(ResponseStatusException ex, WebRequest request) {
        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode(String.valueOf(ex.getStatus().value())).withMessage(ex.getMessage()).withTimestamp(new Date())
                .withStatus(ex.getStatus()).build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(RestClientException.class)
    public final ResponseEntity<CustomResponse<?>> handleRestClientExceptions(Exception ex, WebRequest request) {

        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("500").withMessage(ex.getMessage()).withTimestamp(new Date())
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    // error 400 - bad request error
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<CustomResponse<?>> handleBadRequestException(BadRequestException ex,
                                                                             WebRequest request) {
        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("400").withMessage(ex.getMessage()).withTimestamp(new Date())
                .withStatus(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, response.getStatus());

    }

    // error 404 - record not found
    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<CustomResponse<?>> handleRecordNotFoundException(RecordNotFoundException ex,
                                                                                 WebRequest request) {
        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("404").withMessage(ex.getMessage()).withTimestamp(new Date()).withStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, response.getStatus());

    }

    // forbidden 403
    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<CustomResponse<?>> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("403").withMessage(ex.getMessage()).withTimestamp(new Date()).withStatus(HttpStatus.FORBIDDEN)
                .build();
        return new ResponseEntity<>(response, response.getStatus());

    }

    // unauthorized-401
//    @ExceptionHandler(InvalidJwtAuthenticationException.class)
//    public final ResponseEntity<CustomResponse<?>> invalidJwtAuthentication(InvalidJwtAuthenticationException ex,
//                                                                            WebRequest request) {
//        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
//                .withCode("401").withMessage(ex.getMessage()).withTimestamp(new Date())
//                .withStatus(HttpStatus.UNAUTHORIZED).build();
//        return new ResponseEntity<>(response, response.getStatus());
//    }

    // unauthorized-401
    @ExceptionHandler(UnAuthorizedException.class)
    public final ResponseEntity<CustomResponse<?>> handleUnAuthorizedException(UnAuthorizedException ex,
                                                                               WebRequest request) {
        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("401").withMessage(ex.getMessage()).withTimestamp(new Date())
                .withStatus(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(GracefulFailureException.class)
    public final ResponseEntity<CustomResponse<?>> handleGracefulFailureException(GracefulFailureException ex, WebRequest request) {

        CustomResponse<?> response = new CustomResponseBuilder<>()
                .withDetail(request.getDescription(false))
                .withCode("200")
                .withMessage(ex.getMessage())
                .withData(ex.getData())
                .withTimestamp(new Date())
                .withStatus(HttpStatus.OK).build();

        return new ResponseEntity<>(response, response.getStatus());
    }


    @ExceptionHandler(ConflictException.class)
    public final ResponseEntity<CustomResponse<?>> handleConflictException(ConflictException ex, WebRequest request) {
        CustomResponse<?> response = new
                CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("409")
                .withMessage(ex.getMessage())
                .withData(ex.getData())
                .withTimestamp(new Date())
                .withStatus(HttpStatus.CONFLICT).build();
        return new ResponseEntity<>(response, response.getStatus());
    }


    @ExceptionHandler(ServiceUnavailableException.class)
    public final ResponseEntity<CustomResponse<?>> handleServiceUnavailableException(ServiceUnavailableException ex,
                                                                                     WebRequest request) {
        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("503").withMessage(ex.getMessage()).withTimestamp(new Date())
                .withStatus(HttpStatus.SERVICE_UNAVAILABLE).build();
        return new ResponseEntity<>(response, response.getStatus());
    }
    
    @ExceptionHandler(FailedDependencyException.class)
    public final ResponseEntity<CustomResponse<?>> handleFailedDependencyException(FailedDependencyException ex,
                                                                                     WebRequest request) {
        CustomResponse<?> response = new CustomResponseBuilder<>().withDetail(request.getDescription(false))
                .withCode("424")
                .withMessage(ex.getMessage())
                .withTimestamp(new Date())
                .withStatus(HttpStatus.FAILED_DEPENDENCY)
                .build();
        return new ResponseEntity<>(response, response.getStatus());
    }
    
    
    @ExceptionHandler(HttpClientErrorException.class)
	public final ResponseEntity<CustomResponse<?>> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {

		String errorPayload = ex.getResponseBodyAsString();
		
		System.out.println("ERROR PAYLOAD===>"+errorPayload);
	
		CustomResponse<?> response = new CustomResponseBuilder<>()
				.withDetail(request.getDescription(false))
				.withCode(""+ex.getStatusCode().value())
				.withMessage(ex.getMessage())
				.withTimestamp(new Date())
				.withData(errorPayload)
				.withStatus(ex.getStatusCode())
				.build();
		
		return new ResponseEntity<>(response, response.getStatus());
	}
	
	@ExceptionHandler(HttpServerErrorException.class)
	public final ResponseEntity<CustomResponse<?>> handleHttpServerErrorException(HttpServerErrorException ex, WebRequest request) {

		String errorPayload = ((HttpServerErrorException) ex).getResponseBodyAsString();
		
		CustomResponse<?> response = new CustomResponseBuilder<>()
				.withDetail(request.getDescription(false))
				.withCode(""+ex.getStatusCode().value())
				.withMessage(ex.getMessage())
				.withData(errorPayload)
				.withTimestamp(new Date())
				.withStatus(ex.getStatusCode())
				.build();
		
		return new ResponseEntity<>(response, response.getStatus());
	}


}
