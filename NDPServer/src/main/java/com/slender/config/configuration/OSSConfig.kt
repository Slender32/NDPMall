package com.slender.config.configuration

import com.aliyun.oss.ClientBuilderConfiguration
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.common.auth.DefaultCredentialProvider
import com.aliyun.oss.common.comm.SignVersion
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OSSConfig {
    companion object {
        const val OSS_ENDPOINT = "oss-cn-guangzhou.aliyuncs.com"
        const val OSS_REGION = "cn-guangzhou"
        val OSS_ACCESS_KEY_ID: String? = System.getenv("OSS_ACCESS_KEY_ID")
        val OSS_ACCESS_KEY_SECRET: String? = System.getenv("OSS_ACCESS_KEY_SECRET")
    }
    @Bean
    open fun provider(): DefaultCredentialProvider{
        return DefaultCredentialProvider(OSS_ACCESS_KEY_ID, OSS_ACCESS_KEY_SECRET)
    }

    @Bean
    open fun clientConfig(): ClientBuilderConfiguration {
        return ClientBuilderConfiguration().apply {
            setSignatureVersion(SignVersion.V4)
        }
    }

    @Bean
    open fun ossClient(
        provider: DefaultCredentialProvider,
        config: ClientBuilderConfiguration
    ): OSS {
        return OSSClientBuilder.create()
            .credentialsProvider(provider)
            .clientConfiguration(config)
            .region(OSS_REGION)
            .endpoint(OSS_ENDPOINT)
            .build()
    }
}