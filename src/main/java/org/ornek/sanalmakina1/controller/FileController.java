package org.ornek.sanalmakina1.controller;

import org.ornek.sanalmakina1.service.SSHConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private SSHConnectionService sshConnectionService;

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @GetMapping
    public ResponseEntity<List<String>> listRemoteFiles() {
        try {
            sshConnectionService.connectToVM(host, username, password);
            List<String> files = sshConnectionService.listRemoteFiles("/remote/directory");
            sshConnectionService.disconnect();
            return new ResponseEntity<>(files, HttpStatus.OK);
        } catch (JSchException | SftpException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            File localFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(localFile);
            sshConnectionService.connectToVM(host, username, password);
            sshConnectionService.uploadFile(localFile.getAbsolutePath(), "/remote/directory/" + file.getOriginalFilename());
            sshConnectionService.disconnect();
            return new ResponseEntity<>("Dosya başarıyla yüklendi", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Dosya yükleme hatası", e);
            return new ResponseEntity<>("Dosya yüklenirken bir hata oluştu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            sshConnectionService.connectToVM(host, username, password);
            String localPath = "/local/directory/" + fileName;
            sshConnectionService.downloadFile("/remote/directory/" + fileName, localPath);
            sshConnectionService.disconnect();

            byte[] fileContent = Files.readAllBytes(new File(localPath).toPath());
            return new ResponseEntity<>(fileContent, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

