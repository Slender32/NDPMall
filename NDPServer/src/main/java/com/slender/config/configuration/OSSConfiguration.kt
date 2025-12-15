package com.slender.config.configuration

import com.aliyun.oss.ClientBuilderConfiguration
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.common.auth.DefaultCredentialProvider
import com.aliyun.oss.common.comm.SignVersion
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class OSSConfiguration {
    companion object {
        val ACCESS_KEY_ID: String? = System.getenv("OSS_ACCESS_KEY_ID")
        val ACCESS_KEY_SECRET: String? = System.getenv("OSS_ACCESS_KEY_SECRET")
        const val ENDPOINT = "oss-cn-guangzhou.aliyuncs.com"
        const val REGION = "cn-guangzhou"
        const val BUCKET_NAME = "ndp-mall"
    }
    @Bean
    open fun provider(): DefaultCredentialProvider
        = DefaultCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET)


    @Bean
    open fun clientConfig(): ClientBuilderConfiguration
        = ClientBuilderConfiguration().apply {
            setSignatureVersion(SignVersion.V4)
        }


    @Bean
    open fun ossClient(
        provider: DefaultCredentialProvider,
        config: ClientBuilderConfiguration
    ): OSS
        = OSSClientBuilder.create()
            .credentialsProvider(provider)
            .clientConfiguration(config)
            .region(REGION)
            .endpoint(ENDPOINT)
            .build()

}