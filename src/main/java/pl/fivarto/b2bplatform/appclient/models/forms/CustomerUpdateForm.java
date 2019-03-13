package pl.fivarto.b2bplatform.appclient.models.forms;


import lombok.Data;

@Data
public class CustomerUpdateForm {
    private int id;
    private String login;
    private String password;
    private String email;
    private String nip;
    private String phone;
    private String companyName;
    private boolean isAccepted;


}
