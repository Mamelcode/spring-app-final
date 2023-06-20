package org.edupoll.dto.error;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ErrorResponse {
	String message;
	
	Timestamp timestamp;
	
	public ErrorResponse(String message, Long timestamp) {
		super();
		this.message = message;
		this.timestamp = new Timestamp(timestamp);
	}
	
	public String getMessage() {
		return message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
	
}
