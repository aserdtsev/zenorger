package ru.serdtsev.zenorger.config

import org.slf4j.MDC
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskDecorator
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.context.request.RequestContextHolder
import ru.serdtsev.zenorger.common.ApiRequestContextHolder


@Configuration
@EnableAsync
class SpringAsyncConfig(val apiRequestContextHolder: ApiRequestContextHolder) {
    @Bean
    fun getAsyncExecutor() = ThreadPoolTaskExecutor().apply {
        setTaskDecorator(ContextCopyingDecorator(apiRequestContextHolder))
        initialize()
    }
}

class ContextCopyingDecorator(val apiRequestContextHolder: ApiRequestContextHolder) : TaskDecorator {
    override fun decorate(runnable: Runnable): Runnable {
        val context = RequestContextHolder.currentRequestAttributes()
        val contextMap = MDC.getCopyOfContextMap()
        val apiRequestContext = apiRequestContextHolder.apiRequestContext
        return Runnable {
            try {
                RequestContextHolder.setRequestAttributes(context)
                MDC.setContextMap(contextMap)
                apiRequestContextHolder.apiRequestContext = apiRequestContext
                runnable.run()
            } finally {
                MDC.clear()
                RequestContextHolder.resetRequestAttributes()
                apiRequestContextHolder.clear()
            }
        }
    }
}