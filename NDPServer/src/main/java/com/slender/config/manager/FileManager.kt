package com.slender.config.manager

import com.aliyun.oss.ClientException
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSException
import com.slender.config.configuration.OSSConfiguration
import com.slender.enumeration.FileType
import com.slender.exception.file.FileNameErrorException
import com.slender.exception.file.FileNameNotFoundException
import com.slender.exception.file.OSSProcessingException
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class FileManager(
    private val client: OSS
) {
    fun upload(fileName: String, content: ByteArray, type: FileType): String {
        try {
            val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"))
            val fileId = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."))
            val objectName = "$date/${type.name}/$fileId"
            client.putObject(OSSConfiguration.BUCKET_NAME, objectName, ByteArrayInputStream(content))
            return "https://${OSSConfiguration.BUCKET_NAME}.${OSSConfiguration.ENDPOINT}/$objectName"
        } catch (_: OSSException) {
            throw OSSProcessingException()
        } catch (_: ClientException) {
            throw OSSProcessingException()
        } catch (_: NullPointerException) {
            throw FileNameNotFoundException()
        } catch (_: IndexOutOfBoundsException) {
            throw FileNameErrorException()
        }
    }
}