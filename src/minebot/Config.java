package minebot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
	public static String username = "";
	public static String password = "";
	
	public static String host = "localhost";
	public static int port = 25565;
	
	public static String master = "";
	
	public static void Load(String filename) throws IOException {
		BufferedReader file = new BufferedReader(new FileReader(filename));

		String line;
		while ((line = file.readLine()) != null) {
			String[] pair = line.split("=");
			String key = pair[0].trim().toLowerCase();
			String value = pair[1].trim();
			
			if (key.equals("username")) {
				username = value;
			} else if (key.equals("password")) {
				password = value;
			} else if (key.equals("host")) {
				host = value;
			} else if(key.equals("port")) {
				port = Integer.parseInt(value);
			} else if (key.equals("master")) {
				master = value;
			}
		}
	}
}