package gallegux.db.orm;



import gallegux.db.orm.annotation.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * Contiene los datos de la tabla y de la clase
 * 
 * @author cucr020
 *
 */
public class Clase 
{
	
	private Class claseBean = null;
	private String tabla = null;
	private List<Atributo> columnas = new ArrayList<Atributo>();
	private List<Atributo> columnasPK = null;
	private String selectFrom = null;
	private String selectSql = null;
	private String insertSql = null;
	private String updateSql = null;
	private String deleteSql = null;
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	
	public Clase(Class claseBean)
	throws NoSuchMethodException, ClassNotFoundException
	{
		this.claseBean = claseBean;
		
		Atributo atributo;
		Annotation[] annotations = this.claseBean.getAnnotations();
		
		// sacamos el tabla de la tabla
		for (Annotation a: annotations) {
			try {
				this.tabla = ((Table) a).name();
			} 
			catch (Exception e) {}
		}
		
		// sacamos los campos de la tabla
		Field[] atributos = this.claseBean.getDeclaredFields();
		for (Field f: atributos) {
			atributo = new Atributo(f, this);
			columnas.add(atributo);
		}
	}
	
	
	
	public Class getBeanClass() {
		return this.claseBean;
	}
	
	
	public String getTabla() {
		return this.tabla;
	}
	
	
	
	public List<Atributo> getAtributos() {
		return this.columnas;
	}
	
	
	
	public Atributo getAtributoPorNombre(String nombreAtributo)
	{
		for (Atributo a: this.columnas) {
			if (a.getNombreAtributo().equals(nombreAtributo)) return a;
		}
		return null;
	}
	
	
	public Atributo getAtributoPorColumna(String nombreColumna)
	{
		for (Atributo a: this.columnas) {
			if (a.getNombreColumna().equals(nombreColumna)) return a;
		}
		return null;
	}

	
	
	protected String getSelectFrom()
	{
		if (selectFrom == null) {
			StringBuilder sbSql = new StringBuilder("select ");
			int campos = 0;
			
			for (Atributo dc: columnas) {
				if (campos++ > 0) sbSql.append(',');
				sbSql.append(dc.getNombreColumna());
			}
			sbSql.append(" from ").append(this.tabla);
			
			selectFrom = sbSql.toString();
			log.fine(selectFrom);
		}
		return selectFrom;
	}
	
	
	
	public String getSelectByPK()
	{
		if (selectSql == null) {
			StringBuilder sbSql = new StringBuilder(getSelectFrom()).append(" where ");
			int pks = 0;
			
			for (Atributo dc: getAtributosPK()) {
				if (pks++ > 0) sbSql.append(" and ");
				sbSql.append(dc.getNombreColumna()).append("=?");
			}
			selectSql = sbSql.toString();
			log.fine(selectSql);
		}
		return selectSql;
	}
	
	
	
	public List<Atributo> getAtributosPK() 
	{
		if (this.columnasPK == null) {
			this.columnasPK = new ArrayList<Atributo>();
			
			for (Atributo columna: columnas) {
				if (columna.isPrimaryKey()) {
					this.columnasPK.add(columna);
				}
			}
		}
		
		return this.columnasPK;
	}
	
	
	
	public String getInsert() 
	{
		if (insertSql == null) {
			StringBuilder sbSql = new StringBuilder("insert into ");
			StringBuilder sbCampos = new StringBuilder(" (");
			StringBuilder sbValores = new StringBuilder();
			int numCampos = 0;
			
			for (Atributo dc: columnas) {
				if (numCampos++ > 0) {
					sbCampos.append(',');
					sbValores.append(',');
				}
				sbCampos.append(dc.getNombreColumna());
				sbValores.append('?');
			}
			
			sbSql.append(this.tabla).append(sbCampos).append(") values (").append(sbValores).append(')');

			insertSql = sbSql.toString();
			log.fine(insertSql);
		}
		return insertSql;
	}
	
	
	
	public String getUpdate() 
	{
		if (updateSql == null) {
			StringBuilder sbSql = new StringBuilder("update ").append(this.tabla).append(" set ");
			StringBuilder sbWhere = new StringBuilder();
			
			int numCampos = 0;
			int numCondic = 0;
			
			for (Atributo columna: columnas) {
				if (numCampos++ > 0) sbSql.append(',');
				sbSql.append(columna.getNombreColumna()).append("=?");
				if (columna.isPrimaryKey()) {
					if (numCondic++ > 0) sbWhere.append(" and ");
					sbWhere.append(columna.getNombreColumna()).append("=?");
				}
			}
			
			sbSql.append(" where ").append(sbWhere);
			
			updateSql = sbSql.toString();
			log.fine(updateSql);
		}
		return updateSql;
	}
	
	
	
	public String getDelete() {
		if (deleteSql == null) {
			StringBuilder sbSql = new StringBuilder("delete from ").append(this.tabla).append(" where ");
			
			int numCondic = 0;
			
			for (Atributo columna: getAtributosPK()) {
				if (numCondic++ > 0) sbSql.append(" and ");
				sbSql.append(columna.getNombreColumna()).append("=?");
			}

			deleteSql = sbSql.toString();
			log.fine(deleteSql);
		}
		return deleteSql;
	}
	
	

}
