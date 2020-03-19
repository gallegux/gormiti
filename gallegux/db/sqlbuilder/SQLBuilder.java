package gallegux.db.sqlbuilder;

import gallegux.db.sqlbuilder.condiciones.Condicion;
import gallegux.db.sqlbuilder.condiciones.CondicionExprOpExpr;
import gallegux.db.sqlbuilder.condiciones.O;
import gallegux.db.sqlbuilder.condiciones.Y;

import java.util.ArrayList;
import java.util.List;



public class SQLBuilder 
{
	
	private boolean selectDistinct = false;
	// select
	private List<String> campos = new ArrayList<String>();
	// from
	private List<String> tablas = new ArrayList<String>();
	// where
	private Condicion condicionWhere = null;
	// order by
	private List<String> camposOrderBy = new ArrayList<String>();
	// group by
	private List<String> camposGroupBy = new ArrayList<String>();
	// having
	private Condicion condicionGroup = null;
	
	
	
	public SQLBuilder() {}
	
	
	
	public void setSelectDistinct(boolean selectDistinct) {
		this.selectDistinct = selectDistinct;
	}
	
	
	
	/**
	 * Añade un campo a devolver en la sentencia SQL.
	 * Se puede indicar el alias:  "FEC_NAC AS FECHA_NACIMIENTO"
	 * @param campo
	 * @return
	 */
	public SQLBuilder addFields(String... campos) {
		for (String campo: campos) this.campos.add(campo);
		return this;
	}
	
	
	/**
	 * Añade una tabla a la sentencia SQL. 
	 * Se puede indicar el alias: "PERSONAS P"
	 * @param tabla
	 * @return
	 */
	public SQLBuilder addTables(String... tablas) {
		for (String tabla: tablas) this.tablas.add(tabla);
		return this;
	}
	
	
	
	public SQLBuilder setCondicionWhere(Condicion c) {
		this.condicionWhere = c;
		return this;
	}
	
	
	/**
	 * Puede incluir el modificador de orden ascendente o descendente: "APELLIDOS DESC"
	 * @param campo
	 * @return
	 */
	public SQLBuilder addOrder(String campo) {
		camposOrderBy.add(campo);
		return this;
	}
	

	public SQLBuilder addGroup(String campo) {
		camposGroupBy.add(campo);
		return this;
	}
	

	public SQLBuilder setCondicionGroup(Condicion c) {
		this.condicionGroup = c;
		return this;
	}
	
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		addParteDeStrings(sb, selectDistinct ? "SELECT DISTINCT " : "SELECT ", campos);
		addParteDeStrings(sb, " FROM ", tablas);
		addParteDeCondiciones(sb, " WHERE ", condicionWhere);
		addParteDeStrings(sb, " ORDER BY ", camposOrderBy);
		addParteDeStrings(sb, " GROUP BY ", camposGroupBy);
		addParteDeCondiciones(sb, " HAVING ", condicionGroup);
		
		return sb.toString();
	}
	
	
	/**
	 * Para las partes SELECT, FROM, ORDER BY, GROUP BY
	 * @param sb 
	 * @param parteSQL
	 * @param elementos
	 */
	private void addParteDeStrings(StringBuilder sb, String parteSQL, List<String> elementos)
	{
		if (elementos.size() > 0) {
			sb.append(parteSQL);
			addList(sb, elementos);
		}
	}
	
	
	private void addParteDeCondiciones(StringBuilder sb, String parteSQL, Condicion condicion)
	{
		if (condicion != null) {
			sb.append(parteSQL);
			condicion.addTo(sb);
		}
	}
	
	
	
	/**
	 * Añade al StringBuilder los elementos separados por comas. Por ejemplo:
	 * El SB viene con "ABC DEF", y los elementos son {"HIJ", "KLM", "NOP"}.
	 * SB queda asi: "ABC DEF HIJ,KLM,NOP"
	 * @param sb
	 * @param elementos
	 */
	private void addList(StringBuilder sb, List<String> elementos) {
		for (Object e: elementos) {
			sb.append(e).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
	}
	
	
	/////////////////////////////////////////////////////////////////////////
	
	
	public static void main(String...arg) {
		try {
			SQLBuilder b = new SQLBuilder();
			
			b.addFields("nombre", "apellidos");
			b.addTables("personas");
			b.addOrder("edad");

			O co = new O();
			
			b.setCondicionWhere(co);			
			
			co.add(new CondicionExprOpExpr("ciudad","=","{ciudad1}"));
			co.add(new CondicionExprOpExpr("ciudad","=","{ciudad2}"));
			
			Y cy = new Y();
			
			cy.add(new CondicionExprOpExpr("edad","=","{e1}"));
			cy.add(new CondicionExprOpExpr("estudios","=","{e2}"));
			
			co.add(cy);
			
			System.out.println(b);
			System.out.println("FIN");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
