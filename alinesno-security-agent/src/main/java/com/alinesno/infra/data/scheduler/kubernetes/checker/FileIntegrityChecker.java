package com.alinesno.infra.data.scheduler.kubernetes.checker;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.Base64;

public class FileIntegrityChecker {

    private static final String HASH_ALGORITHM = "SHA-256";

    public static void main(String[] args) {
        // 指定要检查的目录
        Path directory = Paths.get("/path/to/your/website");

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (Files.isRegularFile(file)) {  // 只处理普通文件
                        byte[] hash = calculateHash(file);
                        System.out.println("File: " + file + ", Hash: " + Base64.getEncoder().encodeToString(hash));
                        // 这里可以改为将哈希值保存到文件或数据库
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] calculateHash(Path file) throws IOException {
        try (InputStream fis = Files.newInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}