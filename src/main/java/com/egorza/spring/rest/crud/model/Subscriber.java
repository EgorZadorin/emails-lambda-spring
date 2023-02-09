package com.egorza.spring.rest.crud.model;

import javax.persistence.*;

@Entity
@Table(name = "subscribers")
public class Subscriber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "email")
	private String email;

	public Subscriber() {

	}

	public Subscriber(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Subscriber [id=" + id + ", title=" + email + "]";
	}

}
