package gallegux.db.orm.gen;



import gallegux.db.orm.util.Util;


public class ColumnaAtributo 
{
	
	private final String COMILLAS = "\"";
	
	// bd
	public String columna;
	public String tipoSql;
	public boolean pk = false;
	public boolean nullable = true;
	public String fk = null;
	public String valorDefecto;
	public boolean autoincrement = false;
	// java
	public String atributo;
	public String tipoJava;
	public String sufijoGetSet;
	public String paqueteClaseFK;
	public String atributoFK;

	
	public TablaClase tablaClase;
	
	
	public ColumnaAtributo(TablaClase tablaClase, String...fila)
	{
		this.tablaClase = tablaClase;
		
		columna = fila[0].trim();
		tipoSql = fila[1].trim();
		pk = fila[2].trim().toLowerCase().equals("x");
		nullable = fila[3].trim().toLowerCase().equals("x");
		fk = fila[4].trim();
		valorDefecto = fila[5].trim();
		autoincrement = fila[6].trim().toLowerCase().equals("x");
		atributo = fila[7].trim();
		tipoJava = fila[8].trim();
		sufijoGetSet = fila[9].trim();
	}
	
	
	public String getGetter()
	{
		return "get" + ( 
			(Util.esVacio(sufijoGetSet))
			? crearSufijoMetodo(this.atributo)
			: this.sufijoGetSet 
			);
	}
	
	
	public String getSetter()
	{
		return "set" + (
			(Util.esVacio(sufijoGetSet))
			? crearSufijoMetodo(this.atributo)
			: this.sufijoGetSet
			);
	}
	

	
	
	private String crearSufijoMetodo(String s) {
		return s.substring(0,1).toUpperCase() + s.substring(1);
	}
	
	
	public String getColumnAnnotation()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("@Column(");

		append(sb, "name", COMILLAS + this.columna + COMILLAS);
		sb.append(", ");
		append(sb, "primaryKey", this.pk);
		sb.append(", ");
		append(sb, "autoIncrement", this.autoincrement);

		if (!Util.esVacio(this.fk)) {
			sb.append(", ");
			append(sb, "foreignKey", COMILLAS + this.fk + COMILLAS);
		}
		
		sb.append(")");
		
		return sb.toString();
	}
	
	
	
	public String getMethodsAnnotation()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("@Methods(");

		append(sb, "setter", COMILLAS + getSetter() + COMILLAS);
		sb.append(", ");
		append(sb, "getter", COMILLAS + getGetter() + COMILLAS);

		sb.append(")");
		
		return sb.toString();
		
	}
	
	
	
	public String getReferenceAnnotation()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("@Reference(");

		append(sb, "classs", COMILLAS + this.paqueteClaseFK + COMILLAS);
		sb.append(", ");
		append(sb, "attribute", COMILLAS + this.atributoFK + COMILLAS);

		sb.append(")");
		
		return sb.toString();
		
	}
	
	
	public String getTablaPuntoColumna()
	{
		return this.tablaClase.tabla + "." + this.columna;
	}
	
	
	private void append(StringBuilder sb, String atributo, Object valor)
	{
		sb.append(atributo);
		sb.append('=');
		sb.append(valor);
	}
	
	
	private String getValorAnotacion(String valor)
	{
		if (valor == null) {
			return "null";
		}
		else if (Util.estaEn(this.tipoJava, "Integer", "Long", "Boolean")) {
			return valor;
		}
		else {
			return COMILLAS + valor + COMILLAS;
		}
	}
	
	
}
