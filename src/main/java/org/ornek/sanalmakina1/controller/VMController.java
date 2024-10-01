package org.ornek.sanalmakina1.controller;

import org.ornek.sanalmakina1.dto.ConnectionRequest;
import org.ornek.sanalmakina1.dto.FileUploadRequest;
import org.ornek.sanalmakina1.service.FileTransferService;
import org.ornek.sanalmakina1.service.SSHConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ssh")
public class VMController {

    @Autowired
    private SSHConnectionService sshConnectionService;

    @Autowired
    private FileTransferService fileTransferService;

    @PostMapping("/connect")
    public ResponseEntity<String> connectToVM(@RequestBody ConnectionRequest request) {
        try {
            sshConnectionService.connectToVM(request.getHost(), request.getUsername(), request.getPassword());
            return ResponseEntity.ok("Connected to VM successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to connect to VM: " + e.getMessage());
        }
    }

    @GetMapping("/connect")
    public ResponseEntity<String> getConnectionStatus() {
        return ResponseEntity.ok("VM baglanti durumu kontrol ediliyor");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestBody FileUploadRequest request) {
        try {
            fileTransferService.uploadFile(request.getHost(), request.getUsername(), request.getPassword(),
                    request.getLocalFilePath(), request.getRemoteDir());
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }
}
