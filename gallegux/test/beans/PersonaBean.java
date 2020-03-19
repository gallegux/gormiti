
package gallegux.test.beans;


import gallegux.db.orm.annotation.Table;
import gallegux.db.orm.annotation.Column;
import gallegux.db.orm.annotation.Methods;
import gallegux.db.orm.annotation.Reference;

/** clase generada */

@Table(name="personas")
public class PersonaBean
{

@Column(name="dni", primaryKey=true, autoIncrement=false)
@Methods(setter="setDNI", getter="getDNI")
public String dni = null;

public void setDNI(String x) { this.dni = x; }

public String getDNI() { return this.dni; }



@Column(name="nombre", primaryKey=false, autoIncrement=false)
@Methods(setter="setNombre", getter="getNombre")
public String nombre = null;

public void setNombre(String x) { this.nombre = x; }

public String getNombre() { return this.nombre; }



@Column(name="apellidos", primaryKey=false, autoIncrement=false)
@Methods(setter="setApellidos", getter="getApellidos")
public String apellidos = null;

public void setApellidos(String x) { this.apellidos = x; }

public String getApellidos() { return this.apellidos; }





public PersonaBean() {}

public PersonaBean(String dni) {
	this.dni = dni;
}


}
