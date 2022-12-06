package com.db.nomina;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataBaseController {
	static DataBaseConnector connector;
	public DataBaseController(DataBaseConnector dbConnector) {
		connector = dbConnector;
	}
	
	private boolean insertar(String tableName, String columns, String values) {
		String query = String.format("INSERT INTO %s(%s) VALUES(%s)", tableName, columns, values);
		try {
			System.out.println("Query insert ejecutado: " + query);
			connector.executeUpdate(query, tableName);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private ArrayList<String> entradasDeTabla(String tabla, String[] columnas) {
		String query = String.format(
				"SELECT * from %s", tabla);
		ArrayList<String> entradas = new ArrayList<String>();
		
		try {
			ResultSet result = connector.executeQuery(query);
			StringBuilder header = new StringBuilder("");
			System.out.println("Query ejecutado: " + query);
			for (String columna : columnas) {
				header.append(columna + " | " );			
			}
			entradas.add(header.toString());
			while (result.next()) {
				StringBuilder entrada = new StringBuilder("");
				for (String column : columnas) {
					String value = result.getString(column);
					entrada.append(value + " | ");
				}
				entradas.add(entrada.toString());
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return entradas;
	}
	
	private ArrayList<String> entradasDeEmpleado(String emplId) {
		String query = String.format(
				"SELECT * from empleados WHERE empl_Id=%s", emplId);
		ArrayList<String> entradas = new ArrayList<String>();
		
		try {
			ResultSet result = connector.executeQuery(query);
			StringBuilder header = new StringBuilder("");
			System.out.println("Query ejecutado: " + query);
			for (String columna : DataBaseColumns.getColumns("empleados")) {
				header.append(columna + " | " );			
			}
			entradas.add(header.toString());
			while (result.next()) {
				StringBuilder entrada = new StringBuilder("");
				for (String column : DataBaseColumns.getColumns("empleados")) {
					String value = result.getString(column);
					entrada.append(value + " | ");
				}
				entradas.add(entrada.toString());
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entradas;
	} 
	
	public int paisId(String pais) {
		String query = String.format(
				"SELECT pais_id from paises WHERE pais_nombre='%s'", pais);
		try {
			ResultSet result = connector.executeQuery(query);
			result.next();
			int id = result.getInt("pais_id");
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int ciudadId(String ciudad) {
		String query = String.format(
				"SELECT ciud_id from ciudades WHERE ciud_nombre='%s'", ciudad);
		try {
			ResultSet result = connector.executeQuery(query);
			result.next();
			int id = result.getInt("ciud_id");
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int cargoId(String cargo) {
		String query = String.format(
				"SELECT cargo_id from cargos WHERE cargo_nombre='%s'", cargo);
		try {
			ResultSet result = connector.executeQuery(query);
			result.next();
			int id = result.getInt("cargo_id");
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int empleadoCargoId(String id) {
		String query = String.format(
				"SELECT cargo_id from empleados WHERE empl_id='%s'", id);
		try {
			ResultSet result = connector.executeQuery(query);
			result.next();
			int response = result.getInt("cargo_id");
			return response;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int empleadoDptoId(String id) {
		String query = String.format(
				"SELECT dpto_id from empleados WHERE empl_id='%s'", id);
		try {
			ResultSet result = connector.executeQuery(query);
			result.next();
			int response = result.getInt("dpto_id");
			return response;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int dptoId(String dpto) {
		String query = String.format(
				"SELECT dpto_id from departamentos WHERE dpto_nombre='%s'", dpto);
		try {
			ResultSet result = connector.executeQuery(query);
			result.next();
			int id = result.getInt("dpto_id");
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String insertarPais(String pais) {
		boolean resultado = insertar("paises", "pais_nombre", formatoTexto(pais));
		
		return resultado ? "El pais fue insertado" : "Hubo un error";
	}
	
	public String insertarCiudad(ArrayList<String> valores) {
		int paisId = paisId(valores.get(1));
		String ciudad = valores.get(0);
		boolean resultado = insertar("ciudades", "ciud_nombre, pais_ID", formatoTexto(ciudad) + "," + paisId);
		return resultado ? "La Ciudad fue insertada" : "Hubo un error";
	}
	
	public String insertarDepartamento(String dpto) {
		boolean resultado = insertar("departamentos", "dpto_nombre", formatoTexto(dpto));
		return resultado ? "El departamento fue insertado" : "Hubo un error";
	}
	
	public String insertarLocalizacion(ArrayList<String> valores) {
		String nombre = valores.get(0);
		int ciudadId = ciudadId(valores.get(1));
		boolean resultado = insertar("localizaciones", "localiz_direccion, ciud_ID", formatoTexto(nombre) + "," + ciudadId);
		return resultado ? "La localizacion fue insertada" : "Hubo un error";
	}
	
	public String insertarCargo(ArrayList<String> valores) {
		String cargo = valores.get(0);
		String min = valores.get(1);
		String max = valores.get(2);
		boolean resultado = insertar("cargos", "cargo_nombre, cargo_sueldo_minimo, cargo_sueldo_maximo", formatoTexto(cargo) + "," + min + "," + max);
		return resultado ? "El cargo fue insertado" : "Hubo un error";
	}
	
	public String insertarEmpleado(ArrayList<String> valores) {
		String nombre = formatoTexto(valores.get(0));
		String apellido = formatoTexto(valores.get(1));
		String correo = formatoTexto(valores.get(2));
		String sueldo = valores.get(3);
		int cargoId = cargoId(valores.get(4));
		int dptoId = dptoId(valores.get(5));
		String nacimiento = "DATE " + formatoTexto(valores.get(6));
		String jefeId = valores.get(7);
		String empleado = nombre + ", " + apellido + ", " + correo + ", " + sueldo + ", " + cargoId + ", " + dptoId + ", " + nacimiento + ", " + jefeId;
		String campos = "empl_primer_nombre, empl_segundo_nombre, empl_email, empl_sueldo, cargo_ID, dpto_ID, empl_fecha_nac, gerente_ID";
		boolean resultado = insertar("empleados", campos, empleado);
		return resultado ? "El empleado fue insertado" : "Hubo un error";
	}
	
	private boolean insertarHistorico(int cargoId, int dptoId, String fechaRetiro, String emplId) {
		String historico = fechaRetiro + ", " + cargoId + ", " + dptoId + ", " + emplId;
		return insertar("historico", "empl_his_fecha_retiro, cargo_id, dpto_id, empl_id", historico);
	}
	
	private String formatoTexto(String texto) {
		return "'" + texto + "'";
	}
	
	public ArrayList<String> consultarTabla(String tabla) {
		return entradasDeTabla(tabla, DataBaseColumns.getColumns(tabla));
	}
	
	public ArrayList<String> consultarEmpleado(String emplId) {
		return entradasDeEmpleado(emplId);
	}
	
	private String fechaHoy() {
		LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateObj.format(formatter);
	}
	
	private boolean eliminarRegistro(String tabla, String columna, String valor) {
		String query = String.format(
				"DELETE from %s WHERE %s=%s", tabla, columna, valor);
		
		try {
			System.out.println("Query delete ejecutado: " + query);
			connector.executeUpdate(query, tabla);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String retirarEmpleado(String emplId) {
		// insertar historico
		int cargoId = empleadoCargoId(emplId);
		int dptoId = empleadoDptoId(emplId);
		String fechaRetiro = "DATE " + formatoTexto(fechaHoy());
		boolean resultado = insertarHistorico(cargoId, dptoId, fechaRetiro, emplId);
		// eliminar empleado
		boolean retirado = ejecutarActualizarRetiro(emplId);
		return (resultado & retirado) ? "El empleado fue retirado" : "Hubo un error";
	}
	
	private boolean ejecutarActualizarSalario(String emplId, String sueldo) {
		String query = String.format(
				"UPDATE empleados SET empl_sueldo = %s WHERE empl_id=%s", sueldo, emplId);
		
		try {
			System.out.println("Query delete ejecutado: " + query);
			connector.executeUpdate(query, "empleados");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean ejecutarActualizarRetiro(String emplId) {
		String query = String.format(
				"UPDATE empleados SET estado = 'RETIRADO' WHERE empl_id=%s", emplId);
		
		try {
			System.out.println("Query delete ejecutado: " + query);
			connector.executeUpdate(query, "empleados");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String actualizarEmpleado(ArrayList<String> valores) {
		String propiedad = valores.get(0);
		String valor = valores.get(1);
		String emplId = valores.get(2);
		
		switch(propiedad) {
			case "salario":
				return actualizarSueldo(emplId, valor);
			case "departamento":
				return actualizarDpto(emplId, valor);
			default:
				return "La propiedad no se puede actualizar";
		}
	} 
	
	private String actualizarSueldo(String emplId, String sueldo) {
		boolean actualizado = ejecutarActualizarSalario(emplId, sueldo);
		
		return actualizado ? "El sueldo fue actualizado" : "Hubo un error";
	}
	
	private boolean ejecutarActualizarDptoEmpleado(String emplId, int dpt) {
		String query = String.format(
				"UPDATE empleados SET dpto_id = %s WHERE empl_id=%s", dpt, emplId);
		
		try {
			System.out.println("Query delete ejecutado: " + query);
			connector.executeUpdate(query, "empleados");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String actualizarDpto(String emplId, String dpto) {
		int dptoId = dptoId(dpto);
		boolean actualizado = ejecutarActualizarDptoEmpleado(emplId, dptoId);
		
		return actualizado ? "El departamento del empleado fue actualizado" : "Hubo un error";
	}
	

}
