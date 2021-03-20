
public class Employee {
private Man man;
private String doljnost;
private int ZP;
 
public Employee(Man m,String d, int Zp) {
	this.man = m;
	this.doljnost = d;
	this.ZP = Zp;
}
public Man getMan() {return man;}
public String getDoljnost() {return doljnost;}
public int getZP() {return ZP;}
}
