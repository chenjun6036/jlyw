/**
 * 
 */
package com.jlyw.manager;

import java.util.Comparator;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author xx
 *
 */
public class MyClass implements Comparable<MyClass> {
	private Integer ID;
	private Double Avg;
	private Double Total;
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public Double getAvg() {
		return Avg;
	}
	public void setAvg(Double avg) {
		Avg = avg;
	}
	public Double getTotal() {
		return Total;
	}
	public void setTotal(Double total) {
		Total = total;
	}
	
	@Override
	public int compareTo(MyClass o) {
		// TODO Auto-generated method stub
		 if((((MyClass)o).getTotal()-this.getTotal()>0.0))
		 return 1;
		 else return -1;
	}
}
