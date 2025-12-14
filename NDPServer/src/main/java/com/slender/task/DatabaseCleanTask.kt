package com.slender.task

import com.slender.service.interfase.TaskService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component

@Component
class DatabaseCleanTask(
    private val taskService: TaskService
) : Job {
    override fun execute(jobExecutionContext: JobExecutionContext) {
        taskService.cleanDataBase()
    }
}
