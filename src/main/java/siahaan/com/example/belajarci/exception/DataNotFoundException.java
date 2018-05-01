package siahaan.com.example.belajarci.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException {

	public DataNotFoundException(String message) {
		super(message);
	}

}
