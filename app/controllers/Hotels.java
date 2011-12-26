package controllers;

import java.util.List;

import models.Hotel;
import play.mvc.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import dto.HotelsDTO;

public class Hotels extends Controller {
	
	protected static List<Hotel> getHotels(){
		Gson gson = new Gson();
		HotelsDTO dto = gson.fromJson(params.all().get("body")[0], HotelsDTO.class);
		
		return dto!=null ? dto.hotels : null;
	}
	
	public static void list(int limit, int start) {
		List<Hotel> hotels = Hotel.all().fetch();

		Gson gson = new Gson();
		String data = gson.toJson(hotels);
		
		renderJSON("{data:" + data + "}");		
	}

	public static void create() {
		List<Hotel> hotels = getHotels();
		
		if (hotels != null) {
			for (Hotel hotel : hotels) {
				hotel.save();
			}

			Gson gson = new Gson();
			String data = gson.toJson(hotels);
			
			renderJSON("{'success':true,'message':'The hotel(s) was create successfully', 'data':" + data + "}");
		}
	}

	public static void update() {
		List<Hotel> hotels = getHotels();
		
		if (hotels != null) {
			for (Hotel h: hotels) {
				Hotel hotel = Hotel.findById(h.getId());
				
				hotel.fillFrom(h);
				hotel.save();								
			}

			Gson gson = new Gson();
			String data = gson.toJson(hotels);
			
			renderJSON("{'success':true,'message':'The hotel(s) was updated successfully', 'data':" + data + "}");
		}
	}

	public static void delete() {
		JsonParser parser = new JsonParser();
		JsonElement data = parser.parse(params.all().get("body")[0]);
		JsonArray ids = data.getAsJsonObject().get("data").getAsJsonArray();

		if(data!= null){
			for (int i = 0; i < ids.size(); i++) {
				Hotel.delete("from Hotel where id=?", ids.get(i).getAsLong());
			}
			
			renderJSON("{'success':true,'message':'The hotel was deleted successfully'}");
		}
	}
}
