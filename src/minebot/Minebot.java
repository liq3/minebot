package minebot;

import java.io.*;


import minebot.bot.Bot;
import minebot.net.Packets;
import minebot.net.Session;

public final class Minebot
{
	private static Bot bot;
	private static Session session;

	public static void main(String args[]) throws IOException, InterruptedException {
		
		try {
			Config.Load("config.txt");
		} catch (IOException e) {
			System.out.println("config.txt missing");
			System.exit(0);
		}
		
		session = new Session(Config.username, Config.password);
		session.login();
		session.connect(Config.host, Config.port);

		bot = new Bot(session);
		session.begin();
	}
}