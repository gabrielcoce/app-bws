package com.gcoce.bc.ws.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class JwtResponse {
    //private String username;
    private String accessToken;
    //private List<String> roles;

    public JwtResponse(String accessToken) {
        //this.username = username;
        this.accessToken = accessToken;
        //this.roles = roles;
    }
}
