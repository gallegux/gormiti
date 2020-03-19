package gallegux.db.orm.annotation;

import java.lang.annotation.*; 

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) 

public @interface Reference 
{
	
	/**
	 * metodo setter del atributo
	 * @return
	 */
	String classs() default "";
	
	/**
	 * metodo getter del atributo
	 * @return
	 */
	String attribute() default "";
	
	
}



 