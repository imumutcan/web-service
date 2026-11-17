package com.umut.project.database.mywebservice.file;

import com.umut.project.database.mywebservice.exception.FileStorageException;
import com.umut.project.database.mywebservice.exception.InvalidFileException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${app.upload-dir}")
    private String uploadDir;

    private Path profileDir;

    @PostConstruct
    void init()
    {
        profileDir = Paths.get(uploadDir, "profile").toAbsolutePath().normalize();
        try
        {
            Files.createDirectories(profileDir);
        }
        catch (IOException exception)
        {
            throw new IllegalStateException("Upload klasoru olusturulamadi: " + profileDir, exception);
        }
    }

    public String saveProfileImage(MultipartFile file)
    {
        if(file == null || file.isEmpty())
        {
            throw new InvalidFileException();
        }

        String extension = detectImageExtension(file);
        String fileName = UUID.randomUUID() + "." + extension;
        Path target = profileDir.resolve(fileName);

        try(InputStream inputStream = file.getInputStream())
        {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException exception)
        {
            log.error("Profil resmi kaydedilemedi", exception);
            throw new FileStorageException();
        }
        return fileName;
    }

    public void deleteProfileImage(String fileName)
    {
        if(fileName == null || fileName.isBlank())
        {
            return;
        }
        try
        {
            Path target = profileDir.resolve(fileName).normalize();
            if(target.startsWith(profileDir))
            {
                Files.deleteIfExists(target);
            }
        }
        catch (IOException exception)
        {
            log.warn("Eski profil resmi silinemedi: {}", fileName, exception);
        }
    }

    private String detectImageExtension(MultipartFile file)
    {
        byte[] header = new byte[12];
        int read;
        try(InputStream inputStream = file.getInputStream())
        {
            read = inputStream.readNBytes(header, 0, header.length);
        }
        catch (IOException exception)
        {
            throw new InvalidFileException();
        }

        if(read >= 8
                && (header[0] & 0xFF) == 0x89 && header[1] == 'P' && header[2] == 'N' && header[3] == 'G')
        {
            return "png";
        }
        if(read >= 3
                && (header[0] & 0xFF) == 0xFF && (header[1] & 0xFF) == 0xD8 && (header[2] & 0xFF) == 0xFF)
        {
            return "jpg";
        }
        if(read >= 6
                && header[0] == 'G' && header[1] == 'I' && header[2] == 'F' && header[3] == '8')
        {
            return "gif";
        }
        if(read >= 12
                && header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P')
        {
            return "webp";
        }
        throw new InvalidFileException();
    }
}
