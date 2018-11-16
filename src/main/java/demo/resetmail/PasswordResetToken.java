package demo.resetmail;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import demo.model.User;
import demo.service.UserService;
import demo.service.impl.UserServiceImpl;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PasswordResetToken {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;

@Column(nullable = false, unique = true)
private String token;


@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
@JoinColumn(nullable = false, name = "id")
private User user;


@Column(nullable = false)
	private Date expiryDate;


public void setExpiryDate(int minutes){
	Calendar now = Calendar.getInstance();
	now.add(Calendar.MINUTE, minutes);
	this.expiryDate = now.getTime();
}

public boolean isExpired(){
	return new Date().after(this.expiryDate);
}

}
