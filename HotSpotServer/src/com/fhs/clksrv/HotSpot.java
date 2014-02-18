package com.fhs.clksrv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fhs.clksrv.Clicker.Button;

public class HotSpot {
	public HotSpot() {
		this.name = "default";
		this.x = 0;
		this.y = 0;
		this.button = Clicker.Button.LEFT;
		this.clicks = 1;
	}
	String name;
	int x, y;
	Clicker.Button button;
	int clicks;
	
	protected static java.util.Map<String, HotSpot> loadHotSpots(String location) throws IOException {
		Path path = FileSystems.getDefault().getPath(location);
		byte[] b = Files.readAllBytes(path);
        String s = new String(b, Charset.forName("UTF-8"));
        JSONObject json = new JSONObject(s);
        JSONArray hss = json.getJSONArray("hotspots");
        int len = hss.length();
        java.util.Map<String, HotSpot> spots = new java.util.HashMap<>();
        for (int i = 0; i < len; i++) {
        	JSONObject obj = (JSONObject) hss.get(i);
        	HotSpot hs = new HotSpot();
        	hs.name = obj.getString("name");
        	hs.x = obj.getInt("x");
        	hs.y = obj.getInt("y");
        	hs.button = Button.valueOf(obj.getString("button").toUpperCase());
        	hs.clicks = obj.getInt("clicks");
        	spots.put(hs.name, hs);
        }
        return spots;
    }
	
}