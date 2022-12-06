package com.db.nomina;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class NominaCliente {
	private static boolean running = true;

	public static void main(String[] args) throws UnknownHostException {
		InetAddress ipLocal = InetAddress.getByName("localhost");
		
		try {
			iniciarCliente(9000, ipLocal);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void iniciarCliente(int port, InetAddress ip) throws IOException {
        try {
        	Socket socket = new Socket(ip, port);
    		Scanner reader = new Scanner(System.in);
    		DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            
            Thread readMessageProcess = new Thread( new Runnable() {
	        	@Override
	            public void run(){
	                while(!socket.isClosed()){
	                    try{
	                    	if(input.available() > 0) {
	                    		String message = input.readUTF();
	                    		System.out.println(message);	                    		
	                    	}
	                    }catch(IOException e){
	                        e.printStackTrace();
	                    }
	                }
	            }
	        });
	        Thread sendMessageProcess = new Thread( new Runnable() {
	        	@Override
	            public void run(){
	                while(true){
	                    try{
	                    	String message = reader.nextLine();
	                        output.writeUTF(message);
	                    }catch (IOException e){
	                        e.printStackTrace();
	                    }
	                }
	            }
	        });
	        readMessageProcess.start();
	        sendMessageProcess.start();
        } catch(IOException e) {
        	e.printStackTrace();
        }
	}

}
