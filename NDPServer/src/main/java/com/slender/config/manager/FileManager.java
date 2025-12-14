package com.slender.config.manager;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.slender.config.configuration.OSSConfiguration;
import com.slender.exception.file.FileNameErrorException;
import com.slender.exception.file.FileNameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public final class FileManager {
    private final OSS client;
    public String upload(String fileName,byte[] content){
        try {
            Objects.requireNonNull(fileName);
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            String fileId = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
            String objectName = date+"/"+fileId;
            client.putObject(OSSConfiguration.BUCKET_NAME,objectName,new ByteArrayInputStream(content));
            return "https://"+ OSSConfiguration.BUCKET_NAME+"."+ OSSConfiguration.ENDPOINT+"/"+objectName;
        } catch (OSSException  | ClientException _) {
            throw new OSSException();
        } catch (NullPointerException _) {
            throw new FileNameNotFoundException();
        }catch (IndexOutOfBoundsException _){
            throw new FileNameErrorException();
        }
    }
}
