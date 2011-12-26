package controllers;

import java.util.List;

import models.Hotel;
import play.db.jpa.JPA;
import play.mvc.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dto.HotelsDTO;
import dto.ResponseDTO;

public class Hotels extends Controller {
	
	protected static List<Hotel> getHotels(){
		Gson gson = new Gson();
		HotelsDTO dto = gson.fromJson(params.all().get("body")[0], HotelsDTO.class);
		
		return dto!=null ? dto.data : null;
	}
	
	public static void list(int limit, int start) {
		List<Hotel> hotels = Hotel.all().fetch();
		renderJSON(new ResponseDTO(true, "", hotels));
	}

	public static void create() {
		List<Hotel> hotels = getHotels();
		
		if (hotels != null) {
			for (Hotel hotel : hotels) {
				hotel.save();
			}
			
			renderJSON(new ResponseDTO(true, "The hotel(s) was create successfully", hotels));
		}
	}

	public static void update() {
		List<Hotel> hotels = getHotels();
		
		if (hotels != null) {
			for (Hotel h: hotels) {
				JPA.em().merge(h).save();
			}
			renderJSON(new ResponseDTO(true, "The hotel(s) was updated successfully", hotels));
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
			renderJSON(new ResponseDTO(true, "The hotel was deleted successfully", null));
		}
	}
}
