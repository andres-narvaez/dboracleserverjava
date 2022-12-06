package com.db.nomina;

import java.util.HashMap;

public final class DataBaseColumns {
	final static String[] paises = {"pais_id", "pais_nombre"};
	final static String[] ciudades = {"ciud_id", "ciud_nombre", "pais_id"};
	final static String[] departamentos = {"dpto_id", "dpto_nombre"};
	final static String[] cargos = {
			"cargo_id", 
			"cargo_nombre", 
			"cargo_sueldo_minimo", 
			"cargo_sueldo_maximo"
		};
	final static String[] localizaciones = {"localiz_id", "localiz_direccion", "ciud_id"};
	final static String[] empleados = {
			"empl_id", 
			"empl_primer_nombre", 
			"empl_segundo_nombre", 
			"empl_email",
			"empl_fecha_nac",
			"empl_sueldo",
			"empl_comision",
			"cargo_id",
			"dpto_id",
			"gerente_id",
			"estado"
		};
	final static String[] historico = {
			"empl_his_id", 
			"empl_his_fecha_retiro", 
			"cargo_id", 
			"dpto_id", 
			"empl_id"
		};
	
	public static void main(String[] args) {
	}
	
	public static String[] getColumns(String tabla) {
		HashMap<String, String[]> modelo = new HashMap<String, String[]>();
		
		modelo.put("paises", paises);
		modelo.put("ciudades", ciudades);
		modelo.put("departamentos", departamentos);
		modelo.put("localizaciones", localizaciones);
		modelo.put("empleados", empleados);
		modelo.put("historico", historico);
		modelo.put("cargos", cargos);
		
		return modelo.get(tabla);
	}

}
