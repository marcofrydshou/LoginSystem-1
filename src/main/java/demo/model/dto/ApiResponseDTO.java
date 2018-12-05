package demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponseDTO {

    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;

}
