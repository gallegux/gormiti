package gallegux.db.orm.gen;


import gallegux.db.orm.gen.ColumnaAtributo;
import gallegux.db.orm.gen.TablaClase;
import gallegux.db.orm.util.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;


public class GenerarBeans 
{

	
	String ficheroCSV;
	String directorio;
	
	
	List<TablaClase> entidades = new ArrayList<>();
	//HashMap<Par, List<Par>> map_rel_tablatabla = new HashMap<>();
	//HashMap<Par, ColumnaAtributo> map_tablaColumna_objAtributo = new HashMap<>();
	HashMap<String, ColumnaAtributo> map_tablaColumna_objAtributo = new HashMap<String, ColumnaAtributo>();
	
	
	
	private static String leerFichero(String fichero)
	{
		StringBuilder s = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(fichero));
			
			String linea;
			while ( (linea = br.readLine()) != null ) {
				s.append(linea);
				s.append('\n');
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return s.toString();
	}
	

	private static String escribirFichero(String fichero, String texto)
	{
		StringBuilder s = new StringBuilder();

		try {
			FileWriter fw = new FileWriter(fichero);
			fw.write(texto);
			fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return s.toString();
	}
	


	
	
	public GenerarBeans(String ficheroCSV, String directorio)
	{
		this. ficheroCSV = ficheroCSV;
		this. directorio = directorio;
	}
	
	
	
	
	private TablaClase getEntidad_tabla(String tabla, List<TablaClase> entidades)
	{
		TablaClase tablaClase = null;
		Iterator<TablaClase> i = entidades.iterator();
		
		while (i.hasNext()) {
			tablaClase = i.next();
			if (tabla.equals(tablaClase.tabla)) return tablaClase;
		}
		return tablaClase;
	}
	
	
	private TablaClase getEntidad_clase(String clase, List<TablaClase> entidades)
	{
		TablaClase tablaClase = null;
		Iterator<TablaClase> i = entidades.iterator();
		
		while (i.hasNext()) {
			tablaClase = i.next();
			if (clase.equals(tablaClase.tabla)) return tablaClase;
		}
		return tablaClase;
	}
	
	
	
	public void generarBeans() throws Exception
	{
		TablaClase tablaClase;
		Iterator<TablaClase> ie = entidades.iterator();
		StringBuilder sb = new StringBuilder();
		
		while (ie.hasNext()) {
			tablaClase = ie.next();
			
			BeanCreator bc = new BeanCreator(tablaClase, map_tablaColumna_objAtributo);
			escribirFichero(this.directorio + "/" + tablaClase.clase + ".java", bc.toString());
		}
		
	}
	
	
	
	
	public void generar() throws Exception
	{
		Reader r = new FileReader(this.ficheroCSV);
		CSVReader cr = new CSVReader(r);
		Iterator<String[]> t = cr.iterator(); 
		int n = 0;
		TablaClase tablaClase = null;
		String leer = "";
		
		
		while (t.hasNext()) {
			String[] fila = t.next();
			n++;
			//System.out.print(n+ "  ");
			//Util.print(fila);
			
			if (fila[0].startsWith("$")) {
			}
			else if (fila[0].trim().equals("")) {
			}
			else if ("#tabla-clase".equals(fila[0])) {
				leer = "tablaClase";
			}
			else if ("#columnas-atributos".equals(fila[0])) {
				leer = "columnaAtributo";
			}
			else if (leer.equals("tablaClase")) {
				tablaClase = new TablaClase(fila);
				entidades.add(tablaClase);
			}
			else if (leer.equals("columnaAtributo")) {
				ColumnaAtributo a = new ColumnaAtributo(tablaClase, fila);
				tablaClase.columnaAtributos.add(a);
				String clave = tablaClase.tabla + "." + a.columna;
				System.out.println(clave + " --> " + a.fk);
				this.map_tablaColumna_objAtributo.put( clave , a );
				System.out.println(clave+" "+a);		
						
//				if (!Util.esVacio(a.fk)) {
//					String[] split = Util.split(a.fk);
//					getRelTablaTabla(tablaClase.tabla, split[0]).add( new Par(a.atributo, split[1]) );
//				}
			}
		}
		System.out.println("columnas=" + this.map_tablaColumna_objAtributo.size());
		
		
		//Iterator<TablaClase> it = this.entidades.iterator();
		ColumnaAtributo referenciado;
		//String[] split;
		
		for (TablaClase tc: this.entidades) {
			for (ColumnaAtributo ca: tc.columnaAtributos) {
				if (!Util.esVacio(ca.fk)) {
					referenciado = this.map_tablaColumna_objAtributo.get(ca.fk);
					ca.paqueteClaseFK = referenciado.tablaClase.paqueteClase;
					ca.atributoFK = referenciado.atributo;
				}
			}
		}
		
		
		generarBeans();


	}
	
	
//	private String claveTablaColumna(String...x) 
//	{
//		return x[0] + "." + x[1];
//	}
	

	
	
	
//	private List<Par> getRelTablaTabla(String tabla1, String tabla2)
//	{
//		List<Par> v = null;
//		Par k = new Par(tabla1, tabla2);
//		
//		if (map_rel_tablatabla.get(k) == null) {
//			v = new ArrayList<>();
//			map_rel_tablatabla.put(k, v);
//		}
//		return v;
//	}
	
	
	
	public static void main(String...arg)
	{
		try {
			

			System.out.println("Argumentos:");
			System.out.println("- Fichero CSV");
			System.out.println("- Directorio de salida");
			
			String ficheroCSV = arg[0];
			String directorio = arg[1];
			
			GenerarBeans g = new GenerarBeans(ficheroCSV, directorio);
			g.generar();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}

