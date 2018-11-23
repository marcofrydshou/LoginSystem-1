package demo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
	private long id;
	private String username;
	private String password;
	private String email;
	private String authority;

	public UserDTO(long id, String username, String password, String email, String authority) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.authority = authority;
	}
	public UserDTO(String username, String password, String email, String authority) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.authority = authority;
	}
	public UserDTO(){

	}
}
