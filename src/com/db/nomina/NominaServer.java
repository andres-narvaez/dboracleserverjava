package com.db.nomina;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class NominaServer {
	private static boolean running = true;
	static DataBaseController controller;
	static DataBaseConnector connector;

	public static void main(String[] args) throws UnknownHostException {
		InetAddress ipLocal = InetAddress.getByName("localhost");
		String dbUrl = "jdbc:oracle:thin:@localhost:1521:xe";
		String userName = "POLI01";
		String password = "admin123";
		connector = new DataBaseConnector(dbUrl, userName, password);
		controller = new DataBaseController(connector);
		
		try {
			iniciarServer(9000, ipLocal);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void iniciarServer(int port, InetAddress ip) throws IOException {
		ServerSocket server = new ServerSocket(port, 50, ip);
		Socket socket;
		System.out.println(server);
		
		while(running) {
		 socket = server.accept();
		 DataInputStream input = new DataInputStream(socket.getInputStream());
         DataOutputStream output = new DataOutputStream(socket.getOutputStream());
         NominaHandler user = new NominaHandler(socket, input, output, controller);
         Thread thread = new Thread(user);
         System.out.println("----------------------------------------------");
         System.out.println("User is connected.");
         System.out.println("----------------------------------------------");
         thread.start();
		}
	}
}
