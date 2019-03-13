package pl.fivarto.b2bplatform.appclient.models.forms;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RegisterForm {
    @NotEmpty(message = "Żadne pole nie może być puste")
    private String email;
    @NotEmpty(message = "Żadne pole nie może być puste")
    private String phoneNumber;
    @NotEmpty(message = "Żadne pole nie może być puste")
    private String companyName;
    @NotEmpty(message = "Żadne pole nie może być puste")
    private String nip;
}
