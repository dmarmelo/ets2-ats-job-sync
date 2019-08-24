package pt.marmelo.ets2atsjobsync.backend.service

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.Future

@Service
class AsyncService {
    @Async("threadPoolTaskExecutor")
    fun execute(function: () -> Unit) {
        function()
    }

    @Async("threadPoolTaskExecutor")
    fun <T> execute(function: () -> Future<T>): Future<T> {
        return function()
    }

    @Async("threadPoolTaskExecutor")
    @Transactional
    fun transactional(function: () -> Unit) {
        function()
    }

    @Async("threadPoolTaskExecutor")
    @Transactional
    fun <T> transactional(function: () -> Future<T>): Future<T> {
        return function()
    }

    @Async("threadPoolTaskExecutor")
    @Transactional(readOnly = true)
    fun transactionalReadOnly(function: () -> Unit) {
        function()
    }

    @Async("threadPoolTaskExecutor")
    @Transactional(readOnly = true)
    fun <T> transactionalReadOnly(function: () -> Future<T>): Future<T> {
        return function()
    }
}