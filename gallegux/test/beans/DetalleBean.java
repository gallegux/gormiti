
package gallegux.test.beans;


import gallegux.db.orm.annotation.Table;
import gallegux.db.orm.annotation.Column;
import gallegux.db.orm.annotation.Methods;
import gallegux.db.orm.annotation.Reference;

/** clase generada */

@Table(name="detalles")
public class DetalleBean
{

@Column(name="id", primaryKey=true, autoIncrement=false)
@Methods(setter="setId", getter="getId")
public Integer id = null;

public void setId(Integer x) { this.id = x; }

public Integer getId() { return this.id; }



@Column(name="anyo", primaryKey=false, autoIncrement=false, foreignKey="balances.anyo")
@Methods(setter="setAnyo", getter="getAnyo")
@Reference(classs="gallegux.test.beans.BalanceBean", attribute="anyo")
public Integer anyo = null;

public void setAnyo(Integer x) { this.anyo = x; }

public Integer getAnyo() { return this.anyo; }



@Column(name="mes", primaryKey=false, autoIncrement=false, foreignKey="balances.mes")
@Methods(setter="setMes", getter="getMes")
@Reference(classs="gallegux.test.beans.BalanceBean", attribute="mes")
public Integer mes = null;

public void setMes(Integer x) { this.mes = x; }

public Integer getMes() { return this.mes; }



@Column(name="concepto", primaryKey=false, autoIncrement=false)
@Methods(setter="setConcepto", getter="getConcepto")
public String concepto = null;

public void setConcepto(String x) { this.concepto = x; }

public String getConcepto() { return this.concepto; }



@Column(name="importe", primaryKey=false, autoIncrement=false)
@Methods(setter="setImporte", getter="getImporte")
public Integer importe = 0;

public void setImporte(Integer x) { this.importe = x; }

public Integer getImporte() { return this.importe; }



@Column(name="tipo", primaryKey=false, autoIncrement=false)
@Methods(setter="setTipo", getter="getTipo")
public String tipo = null;

public void setTipo(String x) { this.tipo = x; }

public String getTipo() { return this.tipo; }





public DetalleBean() {}

public DetalleBean(Integer id) {
	this.id = id;
}


}
