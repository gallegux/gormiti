
package gallegux.test.beans;


import gallegux.db.orm.annotation.Table;
import gallegux.db.orm.annotation.Column;
import gallegux.db.orm.annotation.Methods;
import gallegux.db.orm.annotation.Reference;

/** clase generada */

@Table(name="coches")
public class CocheBean
{

@Column(name="matricula", primaryKey=true, autoIncrement=false)
@Methods(setter="setMatricula", getter="getMatricula")
public String matricula = null;

public void setMatricula(String x) { this.matricula = x; }

public String getMatricula() { return this.matricula; }



@Column(name="propietario", primaryKey=false, autoIncrement=false, foreignKey="personas.dni")
@Methods(setter="setPropietario", getter="getPropietario")
@Reference(classs="gallegux.test.beans.PersonaBean", attribute="dni")
public String propietario = null;

public void setPropietario(String x) { this.propietario = x; }

public String getPropietario() { return this.propietario; }



@Column(name="marca", primaryKey=false, autoIncrement=false)
@Methods(setter="setMarca", getter="getMarca")
public String marca = null;

public void setMarca(String x) { this.marca = x; }

public String getMarca() { return this.marca; }



@Column(name="modelo", primaryKey=false, autoIncrement=false)
@Methods(setter="setModelo", getter="getModelo")
public String modelo = null;

public void setModelo(String x) { this.modelo = x; }

public String getModelo() { return this.modelo; }





public CocheBean() {}

public CocheBean(String matricula) {
	this.matricula = matricula;
}


}
