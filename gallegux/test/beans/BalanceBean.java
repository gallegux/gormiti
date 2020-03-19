
package gallegux.test.beans;


import gallegux.db.orm.annotation.Table;
import gallegux.db.orm.annotation.Column;
import gallegux.db.orm.annotation.Methods;
import gallegux.db.orm.annotation.Reference;

/** clase generada */

@Table(name="balances")
public class BalanceBean
{

@Column(name="anyo", primaryKey=true, autoIncrement=false)
@Methods(setter="setAnyo", getter="getAnyo")
public Integer anyo = null;

public void setAnyo(Integer x) { this.anyo = x; }

public Integer getAnyo() { return this.anyo; }



@Column(name="mes", primaryKey=true, autoIncrement=false)
@Methods(setter="setMes", getter="getMes")
public Integer mes = null;

public void setMes(Integer x) { this.mes = x; }

public Integer getMes() { return this.mes; }



@Column(name="ingresos", primaryKey=false, autoIncrement=false)
@Methods(setter="setIngresos", getter="getIngresos")
public Integer ingresos = 0;

public void setIngresos(Integer x) { this.ingresos = x; }

public Integer getIngresos() { return this.ingresos; }



@Column(name="gastos", primaryKey=false, autoIncrement=false)
@Methods(setter="setGastos", getter="getGastos")
public Integer gastos = 0;

public void setGastos(Integer x) { this.gastos = x; }

public Integer getGastos() { return this.gastos; }





public BalanceBean() {}

public BalanceBean(Integer anyo, Integer mes) {
	this.anyo = anyo;
	this.mes = mes;
}


}
