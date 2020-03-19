package gallegux.db.orm.annotation;

import java.lang.annotation.*; 

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE) 

public @interface Table 
{
	
	/**
	 * Nombre de la tabla
	 * @return
	 */
	String name();
	
}



 