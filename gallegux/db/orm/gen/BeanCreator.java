package gallegux.db.orm.gen;


import gallegux.db.orm.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;


public class BeanCreator 
{
	
	private final static String plantilla_clase = 
			"\npackage $paquete;\n\n\n" +
			"import gallegux.db.orm.annotation.Table;\n" +
			"import gallegux.db.orm.annotation.Column;\n" +
			"import gallegux.db.orm.annotation.Methods;\n" +
			"import gallegux.db.orm.annotation.Reference;\n" +
			"import gallegux.db.orm.IBean;\n" +
			"\n/** clase generada */\n\n" +
			"@Table(name=\"$tabla\")\n" +
			"public class $clase implements IBean\n" +
			"{\n\n" +
			"$atributos_set_get\n\n" +
			"$setters_fk\n\n" +
			"$constructor_vacio\n\n" + 
			"$constructor_pk" +
			"\n\n}\n";

	private final static String plantilla_constructor_vacio = "public $clase() {}";
	
	private final static String plantilla_atributo =
			"$anotacion_columna\n" +
			"$anotacion_metodos\n" +
			"$anotacion_referencia\n" + 
			"public $tipo $atributo = $valor_inicial;";
	
	private final static String plantilla_metodo_set = "public void $metodoSet($tipo x) { this.$atributo = x; }";
			
	private final static String plantilla_metodo_get = "public $tipo $metodoGet() { return this.$atributo; }";
			
	
	private TablaClase tablaClase;
	private HashMap<String, ColumnaAtributo> map_tablaColumna_objAtributo;
	
	
	public BeanCreator(TablaClase tablaClase, 
			HashMap<String, ColumnaAtributo> map_tablaColumna_objAtributo)
	{
		System.out.println("Creando " + tablaClase.clase);
		this.tablaClase = tablaClase;
		this.map_tablaColumna_objAtributo = map_tablaColumna_objAtributo;
	}
	
	
	
	private String crearAtributosYMetodos()
	{
		Iterator<ColumnaAtributo> i = this.tablaClase.columnaAtributos.iterator();
		ColumnaAtributo ca;
		StringBuilder sb = new StringBuilder();
		
		while (i.hasNext()) {
			ca = i.next();
			sb.append(crearDeclaracionAtributo(ca));
			sb.append("\n\n");
			sb.append(crearMetodoSet(ca));
			sb.append("\n\n");
			sb.append(crearMetodoGet(ca));
			sb.append("\n\n\n\n");
		}
		
		return sb.toString();
	}
	
	
	
	private String crearDeclaracionAtributo(ColumnaAtributo a)
	{
		String anotColumna = a.getColumnAnnotation();
		String anotMetodos = a.getMethodsAnnotation();
		String anotReferencia =
				(Util.esVacio(a.fk)) ? "" : (a.getReferenceAnnotation() + "\n");
		
		return plantilla_atributo
				.replace("$anotacion_columna", anotColumna)
				.replace("$anotacion_metodos", anotMetodos)
				.replace("$anotacion_referencia", anotReferencia)
				.replace("$tipo", a.tipoJava)
				.replace("$atributo", a.atributo)
				.replace("$valor_inicial", getValorInicial(a));
	}
	
	


	
	
	private String crearMetodoSet(ColumnaAtributo a) 
	{
		return plantilla_metodo_set.toString()
				.replace("$metodoSet", a.getSetter())
				.replace("$tipo", a.tipoJava)
				.replace("$atributo", a.atributo);
	}
	

	private String crearMetodoGet(ColumnaAtributo a) 
	{
		return plantilla_metodo_get
				.replace("$metodoGet", a.getGetter())
				.replace("$tipo", a.tipoJava)
				.replace("$atributo", a.atributo);
	}
	
	
	private String crearContructorVacio()
	{
		return plantilla_constructor_vacio.replace("$clase", this.tablaClase.clase);
	}

	
	private String crearContructorPK()
	{
		List<ColumnaAtributo> atributosPK = new ArrayList<>();
		ColumnaAtributo a;
		
		for (ColumnaAtributo ca: this.tablaClase.columnaAtributos) {
			if (ca.pk) atributosPK.add(ca);
		}

		
		StringBuffer s = new StringBuffer();
		s.append("public ");
		s.append(this.tablaClase.clase);
		s.append('(');
		
		for (int n = 0; n < atributosPK.size(); n++) {
			if (n > 0) s.append(", ");
			a = atributosPK.get(n);
			s.append(a.tipoJava);
			s.append(" ");
			s.append(a.atributo);
		}
		
		s.append(") {\n");
		
		for (int n = 0; n < atributosPK.size(); n++) {
			a = atributosPK.get(n);
			s.append("\tthis.");
			s.append(a.atributo);
			s.append(" = ");
			s.append(a.atributo);
			s.append(";\n");
		}
		
		s.append("}\n");
		
		return s.toString();
	}
	
	
	
	private String crearSettersFKsCompuestas()
	{
		String fkTabla, fkColumna, tablaRefenrenciada;
		String[] split;
		ColumnaAtributo caFK, caPK ;
		HashMap<String, List<ColumnaAtributo>> fks = new HashMap<>();
		List<ColumnaAtributo> listColumnasFK = null;
		StringBuilder setters = new StringBuilder();
		
		for (ColumnaAtributo c: this.tablaClase.columnaAtributos) {
			if ( !Util.esVacio(c.fk) ) {
				split = Util.split(c.fk);
				fkTabla = split[0];
				fkColumna = split[1];
				
				listColumnasFK = fks.get(fkTabla);
				if (listColumnasFK == null) {
					listColumnasFK = new ArrayList<>();
					fks.put(fkTabla, listColumnasFK);
				}
				// en esta lista guardamos las columnas que son fk con formato tabla.campo
				listColumnasFK.add(c);
				System.out.println("+ " + c);
			}
		}
		
		// eliminamos las entradas a las tablas que tienen menos de 2 columnas que son fk
		for (String tabla: fks.keySet()) {
			listColumnasFK = fks.get(tabla);
			if (listColumnasFK.size() < 2) fks.remove(tabla);
			else System.out.println(tabla+" "+listColumnasFK.size());
		}

		
		for (String tabla: fks.keySet()) {
			System.out.println("tabla: " + tabla);
			listColumnasFK = fks.get(tabla);
			TablaClase tablaClaseReferenciada = listColumnasFK.get(0).tablaClase;
//			System.out.println("tabla referenciada: " + listColumnasFK.get(0));
//			caFK = this.map_tablaColumna_objAtributo.get(listColumnasFK.get(0).getTablaPuntoColumna());
//			tablaRefenrenciada = caFK.tablaClase.clase;
			
			// construccion por atributos
			
			setters.append("public void set");
			setters.append(Util.capitaliza(tabla));
			setters.append("(");
			
			int n = 0;
			for (ColumnaAtributo columnaFK: listColumnasFK) {
				if (n++ > 0) setters.append(", ");
				System.out.println("columnaFK " + columnaFK + columnaFK.getTablaPuntoColumna());
				caFK = this.map_tablaColumna_objAtributo.get(columnaFK.getTablaPuntoColumna());
				setters.append(caFK.tipoJava);
				setters.append(" ");
				setters.append(caFK.atributo);
			}
			setters.append(") {\n");

			for (ColumnaAtributo columnaFK: listColumnasFK) {
				caFK = this.map_tablaColumna_objAtributo.get(columnaFK.getTablaPuntoColumna());
				setters.append("\tthis.");
				setters.append(caFK.atributo);
				setters.append(" = ");
				setters.append(caFK.atributo);
				setters.append(";\n");
			}
			setters.append("}\n\n\n");
			
			// construccion por objeto
			
			setters.append("public void set");
			setters.append(tablaClaseReferenciada.clase);
			setters.append("(");
			setters.append(tablaClaseReferenciada.clase);
			setters.append(" x) {\n");
			
			for (ColumnaAtributo columnaFK: listColumnasFK) {
				caFK = this.map_tablaColumna_objAtributo.get(columnaFK.getTablaPuntoColumna());
				System.out.print(columnaFK+"-->"+caFK.fk);
				caPK = this.map_tablaColumna_objAtributo.get(caFK.fk);
				setters.append("\tthis.");
				setters.append(caFK.atributo);
				setters.append(" = x.");
				setters.append(caPK.getGetter());
				setters.append("();\n");
			}
			setters.append("}\n\n");
		}
		
		return setters.toString();
	}

	
	
//	/**
//	 * obtiene las columnas que forman parte de la pk de una tabla
//	 * tiene sentido cuando la pk está formada por mas de una columnas
//	 */
//	public List<ColumnaAtributo> getColumnasPK(String tabla)
//	{
//		List<ColumnaAtributo> pk = new ArrayList<>();
//		
//		for (TablaClase tc: this.entidades) {
//			for (ColumnaAtributo ca: tc.columnaAtributos) {
//				if (ca.pk) pk.add(ca);
//			}
//		}
//		
//		return pk;
//	}
	
	
	
	public String toString()
	{
		return plantilla_clase
				.replace("$paquete", this.tablaClase.paquete)
				.replace("$tabla", this.tablaClase.tabla)
				.replace("$clase", this.tablaClase.clase)
				.replace("$atributos_set_get", crearAtributosYMetodos())
				.replace("$constructor_vacio", crearContructorVacio())
				.replace("$constructor_pk", crearContructorPK())
				.replace("$setters_fk", crearSettersFKsCompuestas());
	}
	
	
	
	private String getValorInicial(ColumnaAtributo ca)
	{
		if (Util.esVacio(ca.valorDefecto)) {
			return "null";
		}
		else if (Util.estaEn(ca.tipoJava, "Integer", "Long", "Double", "Float", "Boolean")) {
			return ca.valorDefecto;
		}
		else {
			return "\"" + ca.valorDefecto + "\"";
		}
	}
	
	
	
	
}
