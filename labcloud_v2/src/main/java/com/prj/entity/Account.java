package com.prj.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prj.util.CopyRequired;

@Entity
@Table(name = "account")
public class Account extends BaseEntity {

	public enum Role {
		ADMINISTRATOR, ALL_TEACHER, NOR_TEACHER, LAB_TEACHER, STUDENT;

		/**
		 * add default rolePrefix 'ROLE_' to fit comparison in hasRole()
		 * {@link org.springframework.security.access.expression.SecurityExpressionRoot#hasRole(String) }
		 *
		 * @return
		 */
		public String toRoleString() {
			return "ROLE_" + super.toString();
		}
	}

	public enum Gender {
		MALE, FEMALE
	}

	private Gender gender;
	private String number;
	private String password; // 用户的密码，不返回
	private String initialPassword; // 管理员添加时用户的初始密码
	private Role role;
	private boolean active = true;
	private boolean passwordChanged = true; // [true]未设置初始密码
	private String iconPath;
	private String name;
	
	private String mobilePhone;

	// Admin & Teacher
	private String title;

	// Student
	private String email;
	private String grade;
	private String major;
	private Set<Clazz> studentClazzs = new HashSet<Clazz>();
	private Set<StudentReservation> studentReservation = new HashSet<StudentReservation>();

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "student_reservation_student", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = { @JoinColumn(name = "student_reservation_id") })
	public Set<StudentReservation> getStudentReservation() {
		return studentReservation;
	}

	public void setStudentReservation(Set<StudentReservation> studentReservation) {
		this.studentReservation = studentReservation;
	}

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "clazz_student", joinColumns = { @JoinColumn(name = "student_id") }, inverseJoinColumns = { @JoinColumn(name = "clazz_id") })
	public Set<Clazz> getStudentClazzs() {
		return studentClazzs;
	}

	public void setStudentClazzs(Set<Clazz> studentClazzs) {
		this.studentClazzs = studentClazzs;
	}

	@CopyRequired
	@Column(nullable = false, unique = true)
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@CopyRequired(update = false)
	@JsonIgnore
	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "icon_path")
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@CopyRequired()
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@CopyRequired
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Column(nullable = false)
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "password_changed", nullable = false)
	public boolean isPasswordChanged() {
		return passwordChanged;
	}

	public void setPasswordChanged(boolean isNew) {
		this.passwordChanged = isNew;
	}

	@Transient
	public String getInitialPassword() {
		return initialPassword;
	}

	public void setInitialPassword(String initialPassword) {
		this.initialPassword = initialPassword;
	}

	@CopyRequired
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@CopyRequired
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@CopyRequired
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@CopyRequired
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@CopyRequired
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

    /**
     * Getter method for property <tt>mobilePhone</tt>.
     * 
     * @return property value of mobilePhone
     */
	@CopyRequired
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Setter method for property <tt>mobilePhone</tt>.
     * 
     * @param mobilePhone value to be assigned to property mobilePhone
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
	
	
}
