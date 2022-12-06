package com.db.nomina;

import java.io.*;
import java.net.*;
import java.util.*;

public class NominaHandler implements Runnable {
	Boolean isConnected;
	DataInputStream messageInput;
	DataOutputStream messageOutput;
	Socket socket;
	static DataBaseController controller;

	public NominaHandler(Socket userSocket, DataInputStream input, DataOutputStream output, DataBaseController cntrlr) {
		isConnected = true;
		messageInput = input;
		messageOutput = output;
		controller = cntrlr;
		
		printInUserConsole("Estas en línea con el servicio de nomina");
	}

	@Override
	public void run() {
		while(isConnected) {
			try{
				String message = messageInput.readUTF();
				System.out.println(message);
				processMessage(message);
	        }catch(IOException e){
	            e.printStackTrace();
	        }
		}
		
		try{
            messageInput.close();
            messageOutput.close();
        }catch(IOException e){
            e.printStackTrace();
        }
		
	}
	
	private void processMessage(String mensaje) {
		String operacion = this.identificarOperacion(mensaje);
		String tabla = this.nombreTabla(mensaje);
		String msg;
		switch (operacion) {
			case "insert":
				msg = insertarRegistro(tabla, mensaje);
				printInUserConsole(msg);
				break;
			case "select":
				ArrayList<String> msgs = consultarTabla(tabla, mensaje);	
				for(String texto : msgs) {
					printInUserConsole(texto);
				}		
				break;
			case "delete":
				msg = eliminarRegistro(tabla, mensaje);
				printInUserConsole(msg);
				break;
			case "update":
				msg = actualizarRegistro(tabla, mensaje);
				printInUserConsole(msg);
				break;
			default:
				printInUserConsole("Operación inválida");
				break;
		}	
	}
	
	private String actualizarRegistro(String tabla, String mensaje) {
		ArrayList<String> arregloVlrs = valoresArreglo(mensaje);
		switch(tabla) {
			case "empleados":
				return controller.actualizarEmpleado(arregloVlrs);
			default:
				return "Actualización inválida";
		}
	}
	
	private String eliminarRegistro(String tabla, String mensaje) {
		String valores = this.valores(mensaje);
		
		switch (tabla) {
			case "empleados":
				return controller.retirarEmpleado(valores);
			default:
				return "No se puede eliminar registros en la tabla";
		}
	}
	
	private ArrayList<String> consultarTabla(String tabla, String mensaje) {
		String valores = this.valores(mensaje);
		
		switch(tabla) {
			case "empleado":
				return controller.consultarEmpleado(valores);
			default:
				return controller.consultarTabla(tabla);
		}
		
	}
	
	private String insertarRegistro(String tabla, String mensaje) {
		String valores = this.valores(mensaje);
		ArrayList<String> arregloVlrs = valoresArreglo(mensaje);

		switch (tabla) {
			case "paises":
				return controller.insertarPais(valores);
			case "ciudades":
				return controller.insertarCiudad(arregloVlrs);
			case "departamentos":
				return controller.insertarDepartamento(valores);
			case "localizaciones":
				return controller.insertarLocalizacion(arregloVlrs);
			case "cargos":
				return controller.insertarCargo(arregloVlrs);
			case "empleados":
				return controller.insertarEmpleado(arregloVlrs);
			default:
				return "Tabla no encontrada";
		}
	}
	
	private ArrayList<String> mensajeArreglo(String message) {
		ArrayList<String> msg = new ArrayList<String>();
		StringTokenizer token = new StringTokenizer(message,"|");
		
		while(token.hasMoreTokens()) {
			msg.add(token.nextToken());
		}
		
		return msg;
	}
	
	private String identificarOperacion(String message) {
		ArrayList<String> msg = mensajeArreglo(message);
		
		return msg.get(0);	
	}
	
	private String nombreTabla(String message) {
		ArrayList<String> msg = mensajeArreglo(message);
		
		return msg.get(1);
	}
	
	private ArrayList<String> valoresArreglo(String message) {
		ArrayList<String> msg = mensajeArreglo(message);
		
		msg.remove(0);
		msg.remove(0);
		
		return msg;
	}
	
	private String valores(String message) {
		ArrayList<String> msg = valoresArreglo(message);
		StringBuilder str = new StringBuilder("");
		
		  for (String value : msg) {
			  String ultimo = msg.get(msg.size() - 1);
			  int esUltimo = ultimo.compareTo(value);
			  
			  if(esUltimo == 0) {
				  str.append(value);
			  } else {
				  str.append(value).append(",");
			  }  
		  }
		  
		  return str.toString();
	}
	
	public void printInUserConsole(String message) {
		try {
			messageOutput.writeUTF(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
