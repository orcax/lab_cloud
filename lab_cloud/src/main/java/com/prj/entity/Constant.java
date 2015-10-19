package com.prj.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "constant")
public class Constant extends BaseEntity {
	private String name;
	private String strValue;
	private Integer intValue;
	private Double doulbeValue;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	public Integer getIntValue() {
		return intValue;
	}
	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}
	public Double getDoulbeValue() {
		return doulbeValue;
	}
	public void setDoulbeValue(Double doulbeValue) {
		this.doulbeValue = doulbeValue;
	}

}
