package com.orpc.example.domain;

import java.time.Instant;

public class User {
	private long id;
	private String name;
	private String email;
	private String address;
	private Instant create;
	public User(long id, String name, String email, String address, Instant create) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
		this.create = create;
	}
	public User() {
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", create=" + create
				+ "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Instant getCreate() {
		return create;
	}
	public void setCreate(Instant create) {
		this.create = create;
	}
}
