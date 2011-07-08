package minebot;

import java.io.*;

import minebot.net.Session;

public class Minebot
{
	private static Bot player;
	private static Session session;

	public static void main(String args[]) throws Exception {
		
		try {
			Config.Load("config.txt");
		} catch (IOException e) {
			System.out.println("config.txt missing");
		}
		
		session = new Session(Config.username, Config.password);
		session.login();
		session.connect(Config.host, Config.port);
		
		player = new Bot(session);
		session.begin(player);
	}
}
