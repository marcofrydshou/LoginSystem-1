package demo.data.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditUserDTO {

    @NotNull
    private long id;
    private String name; // will not be included if value is null
    private String username; // will not be included if value is null
    private String password; // will not be included if value is null
    private String email; // will not be included if value is null
    private String address; // will not be included if value is null
    private String authority;// will not be included if value is null

}
