package org.ornek.sanalmakina1.dto;

import lombok.Data;

@Data
public class FileUploadRequest {
    private String host;
    private String username;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String password;
    private String localFilePath;
    private String remoteDir;




}
