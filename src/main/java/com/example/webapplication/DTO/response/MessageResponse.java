package com.example.webapplication.DTO.response;

import lombok.Data;

import java.util.Date;
@Data
public class MessageResponse {
	private int status;
	private Date timestamp;
	private String message;

	public MessageResponse(int status,String message) {
	    this.status=status;
		this.message = message;
		this.timestamp=new Date();
	  }
}
