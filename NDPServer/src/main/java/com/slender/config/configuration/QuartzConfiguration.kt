package com.slender.config.configuration;

import com.slender.task.DatabaseCleanTask
import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class QuartzConfiguration {
    @Bean
    open fun databaseCleanupJobDetail(): JobDetail {
        return JobBuilder.newJob(DatabaseCleanTask::class.java)
            .withIdentity("Clean")
            .storeDurably()
            .build()
    }

    @Bean
    open fun databaseCleanupJobTrigger(): CronTrigger {
        val scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 2 * * ?")
            .withMisfireHandlingInstructionFireAndProceed()
        return TriggerBuilder.newTrigger()
            .forJob(databaseCleanupJobDetail())
            .withIdentity("CleanTrigger")
            .withSchedule(scheduleBuilder)
            .build()
    }

}
