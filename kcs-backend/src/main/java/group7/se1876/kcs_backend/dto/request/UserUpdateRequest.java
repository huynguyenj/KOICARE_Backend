package group7.se1876.kcs_backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  UserUpdateRequest {
    private Long userId;
    private String userName;
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String phone;
    private String email;
    private boolean status;
    private List<String> roles;
}
