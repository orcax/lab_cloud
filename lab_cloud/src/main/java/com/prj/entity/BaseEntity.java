package com.prj.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@MappedSuperclass
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class BaseEntity {
	int id;
	
	Date create_time = new Date();
	Date modify_time = create_time;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	@Column(nullable = false)
	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
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
