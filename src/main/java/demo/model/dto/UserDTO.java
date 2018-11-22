package demo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
	private long id;
	private String username;
	private String password;
	private String email;
	private String authority;

	public UserDTO(long id, String username, String email, String authority) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.authority = authority;
	}
	public UserDTO(){

	}
}
