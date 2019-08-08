package pt.marmelo.ets2atsjobsync.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class Ets2AtsJobSyncApplication {

    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Bean
    fun run() = CommandLineRunner {
        //println("HERE HERE HERE HERE")
    }
}

fun main(args: Array<String>) {
    runApplication<Ets2AtsJobSyncApplication>(*args)
}