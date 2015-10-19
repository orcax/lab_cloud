package com.prj.entity;

import javax.persistence.Column;
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
	
	@Column(name = "str_value")
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	@Column(name = "int_value")
	public Integer getIntValue() {
		return intValue;
	}
	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	@Column(name = "double_value")
	public Double getDoulbeValue() {
		return doulbeValue;
	}
	public void setDoulbeValue(Double doulbeValue) {
		this.doulbeValue = doulbeValue;
	}

}
