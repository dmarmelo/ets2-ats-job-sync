package pt.marmelo.ets2atsjobsync.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableAsync
class Ets2AtsJobSyncApplication {

    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Bean
    fun run() = CommandLineRunner {
        //println("HERE HERE HERE HERE")
    }

    @Bean("threadPoolTaskExecutor")
    fun getAsyncExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        executor.maxPoolSize = 20
        //executor.setQueueCapacity(100)
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setThreadNamePrefix("Async-")
        return executor
    }
}

fun main(args: Array<String>) {
    runApplication<Ets2AtsJobSyncApplication>(*args)
}