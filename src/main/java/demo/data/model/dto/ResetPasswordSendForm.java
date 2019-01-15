package demo.data.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordSendForm {

    @NotNull
    private String email;

    public ResetPasswordSendForm(@NotNull String email) {

        this.email = email;
    }

}
