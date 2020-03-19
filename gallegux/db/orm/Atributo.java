package gallegux.db.orm;

import gallegux.db.orm.annotation.Column;
import gallegux.db.orm.annotation.Methods;
import gallegux.db.orm.annotation.Reference;
import gallegux.db.orm.util.Util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;



public class Atributo 
{
	
	// BD //

	/** Nombre de la columna */
	private String columna;
	
	/** Si es o pertenece a la Primary Key de la tabla */
	private boolean primaryKey = false;
	
	/** Si la columna es foreign key */
	private boolean foreignKey = false;
	private String fkTablaColumna = null;
	
	private boolean autoIncrement = false;
	
	// JAVA //
	
	/** Clase del Atributo */
	private Clase clase;
	
	/** Nombre del atributo en la clase Java */
	private String nombre;
	
	/** Tipo del atributo */
	private Class tipo;
	
	/** Referencia FK */
	private String fkClaseBean = null; //
	private String fkAtributoBean = null; //
	private Atributo fkAtributo = null;; 
	
	/** Metodo set */
	private String strSetter = null;
	private Method setter = null;
	
	/** Metodo get */
	private String strGetter = null;
	private Method getter = null;
	
	
	
	
	public Atributo(Field atributo, Clase clase)
	{
		this.clase = clase;
		this.nombre = atributo.getName();
		this.tipo = atributo.getType();
		
		for (Annotation a: atributo.getAnnotations()) {
			if (a instanceof Column) {
				Column c = (Column) a;
				this.columna = c.name();
				this.primaryKey = c.primaryKey();
				this.foreignKey = !Util.esVacio(c.foreignKey());
				if (this.foreignKey) this.fkTablaColumna = c.foreignKey(); 
				this.autoIncrement = c.autoIncrement();
			}
			else if (a instanceof Methods) {
				Methods m = (Methods) a;
				this.strSetter = m.setter();
				this.strGetter = m.getter();
			}
			else if (a instanceof Reference) {
				Reference r = (Reference) a;
				this.fkClaseBean = r.classs();
				this.fkAtributoBean = r.attribute();
			}
		}
	}

	
	public String getNombreColumna() {
		return columna;
	}
	
	
	public String getNombreAtributo() {
		return this.nombre;
	}

	
	public Class getTipo() {
		return tipo;
	}
	
	
	public Clase getClase() {
		return this.clase;
	}
	
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	
	public String getPrimaryKey() {
		return this.fkTablaColumna;
	}
	
	
	public boolean isForeignKey() {
		return this.foreignKey;
	}
	
	
	public Method getGetter()
	throws NoSuchMethodException
	{
		if (this.getter == null) {
			this.getter = this.clase.getBeanClass().getMethod(strGetter);
		}

		return this.getter;
	}
	
	
	public Method getSetter() 
	throws NoSuchMethodException
	{
		if (this.setter == null) {
			this.setter = this.clase.getBeanClass().getMethod(strSetter, this.tipo);
		}
		
		return this.setter;
	}
	
	
	public Atributo getAtributoReferenciado()
	throws ClassNotFoundException, NoSuchMethodException
	{
		if (this.fkAtributo == null) {
			Clase c = Mapeos.getClase(this.fkClaseBean);
			this.fkAtributo = c.getAtributoPorNombre(this.fkAtributoBean);
		}
		return this.fkAtributo;
	}
	

	
	
}
