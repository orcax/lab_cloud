package com.prj.entity;

import java.util.Collection;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@MappedSuperclass
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class BaseEntity {
	long id;
	
	Timestamp createTime = new Timestamp(new java.util.Date().getTime());
	Timestamp modifyTime = (Timestamp) createTime.clone();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonIgnore
	@Column(name = "create_time", nullable = false)
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@JsonIgnore
	@Column(name = "modify_time", nullable = false)
	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public static boolean contains(Collection<? extends BaseEntity> c, BaseEntity e) {
		boolean contains = false;
		for (BaseEntity i : c) {
			if (i.id == e.id) {
				contains = true;
				break;
			}
		}
		return contains;
	}
}
