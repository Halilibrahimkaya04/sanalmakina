package org.ornek.sanalmakina1.dto;

import lombok.Data;

@Data
public class ConnectionRequest {
    private String host;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String password;
}

