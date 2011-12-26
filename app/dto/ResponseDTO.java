package dto;

import java.util.List;

import models.Hotel;

import com.google.gson.Gson;

import controllers.Hotels;

public class ResponseDTO {

	public boolean success;
	public String message;
	public List<Hotel> data;
	
	public ResponseDTO(boolean success, String message, List<Hotel> data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}
	
	public String toJson(){
		return new Gson().toJson(this);
	}
	
}
