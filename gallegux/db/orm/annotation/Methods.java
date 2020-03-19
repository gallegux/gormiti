package gallegux.db.orm.annotation;

import java.lang.annotation.*; 

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) 

public @interface Methods 
{
	
	/**
	 * metodo setter del atributo
	 * @return
	 */
	String setter() default "";
	
	/**
	 * metodo getter del atributo
	 * @return
	 */
	String getter() default "";
	
	
}



 