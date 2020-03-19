package gallegux.db.orm.annotation;

import java.lang.annotation.*; 

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) 

public @interface Column 
{
	
	/**
	 * El nombre de la columna
	 * @return
	 */
	String name();
	
	/**
	 * si es PK o forma parte de la PK
	 * @return
	 */
	boolean primaryKey() default false;
	
	/**
	 * Si puede ser nulo
	 * @return
	 */
	boolean nullable() default true;
	
	/**
	 * Si puede ser nulo
	 * @return
	 */
	boolean autoIncrement() default false;
	
	/**
	 * Clase y columnaAtributos referenciados por esta atributo/columna.
	 * Se especifica con el nombre completo de la clase (que incluye el paquete), el caracter /
	 * y el nombre del atributo.
	 * com.foo.beans.Persona/nombre
	 * @return
	 */
	String foreignKey() default "";
	
}



 