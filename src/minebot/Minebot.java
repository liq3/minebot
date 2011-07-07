package minebot;

import java.io.*;

public class Minebot
{
	private static Player player;
	private static Session session;

	public static void main(String args[]) throws Exception {
		BufferedReader fileInput = null;
		String username = null;
		String password = null;
		try {
			fileInput = new BufferedReader(new FileReader("login.txt"));
			username = fileInput.readLine();
			password = fileInput.readLine();
		} finally {
			if (fileInput != null) {
				fileInput.close();
			}
		}
		
		if (username == null || password == null) {
			System.out.println("Invalid login.txt");
			System.exit(0);
		}
		
		session = new Session(username, password);
		session.login();
		//session.connect("localhost", 25565);
		
		// player = new Player(session);
		// session.begin(player);
	}
}
