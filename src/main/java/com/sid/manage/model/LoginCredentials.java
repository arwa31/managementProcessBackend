package com.sid.manage.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginCredentials {

    private String username;
    private String password;

	public String getUsername() {

		return username;
	}
	public void setUsername(String username) {

		this.username = username;
	}
	public String getPassword() {

		return password;
	}
	public void setPassword(String password) {

		this.password = password;
	}
    
    

}