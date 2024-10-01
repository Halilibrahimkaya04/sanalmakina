package org.ornek.sanalmakina1.service;

import com.jcraft.jsch.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
public class SSHConnectionService {
    private Session session;

    public void connectToVM(String host, String username, String password) throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(username, host, 22);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(30000);
        session.connect();
        System.out.println("Connected to VM");
    }

    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            System.out.println("Disconnected from VM");
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> listRemoteFiles(String remoteDir) throws JSchException, SftpException {
        ChannelSftp sftpChannel = null;
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            List<String> fileList = new ArrayList<>();
            Vector<ChannelSftp.LsEntry> files = sftpChannel.ls(remoteDir);
            for (ChannelSftp.LsEntry entry : files) {
                fileList.add(entry.getFilename());
            }
            return fileList;
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }
        }
    }

    public void downloadFile(String remoteFilePath, String localFilePath) throws JSchException, SftpException {
        ChannelSftp sftpChannel = null;
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.get(remoteFilePath, localFilePath);
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }
        }
    }

    public void uploadFile(String localFilePath, String remoteFilePath) throws JSchException, SftpException {
        ChannelSftp sftpChannel = null;
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            sftpChannel.put(localFilePath, remoteFilePath);
        } finally {
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }
        }
    }
}
