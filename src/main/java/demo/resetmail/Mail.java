package demo.resetmail;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





@AllArgsConstructor
@Data
@NoArgsConstructor
public class Mail {

	private String from;
	private String to;
	private String subject;
	private Map<String, Object> model;



}
