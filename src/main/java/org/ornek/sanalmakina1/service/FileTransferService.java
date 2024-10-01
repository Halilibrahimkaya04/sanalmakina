package org.ornek.sanalmakina1.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class FileTransferService {

    public void uploadFile(String host, String username, String password, String localFilePath, String remoteDir) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "yes");
        session.connect();

        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        sftpChannel.connect();
        File localFile = new File(localFilePath);
        sftpChannel.put(localFile.getAbsolutePath(), remoteDir);
        session.disconnect();
        System.out.println("File uploaded successfully");
    }
}
