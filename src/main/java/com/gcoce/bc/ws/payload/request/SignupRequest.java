package com.gcoce.bc.ws.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @NotNull
    @Size(max = 100)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 50)
    private String password;

    private Set<String> role;
}
