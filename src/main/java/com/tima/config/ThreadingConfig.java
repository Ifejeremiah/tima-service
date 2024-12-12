package com.tima.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

@Configuration
public class ThreadingConfig implements AsyncConfigurer, SchedulingConfigurer {
    @Value("${EXECUTOR.CORE.POOL.SIZE}")
    private Integer executorCorePoolSize;
    @Value("${EXECUTOR.MAX.POOL.SIZE}")
    private Integer executorMaxPoolSize;
    @Value("${EXECUTOR.QUEUE.CAPACITY}")
    private Integer executorQueueCapacity;
    @Value("${SCHEDULER.POOL.SIZE}")
    private Integer schedulerPoolSize;

    @Bean
    @Qualifier("executor")
    @Override
    public TaskExecutor getAsyncExecutor() {
        // This will create a new thread each time a task comes until there are corePoolSize threads.
        // Then each new task will be kept in the queue until there are queueCapacity tasks in the queue
        // Then create new threads for subsequent tasks until there are maxPoolSize threads.
        // Then throw TaskRejected Exception.
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(executorCorePoolSize);
        executor.setMaxPoolSize(executorMaxPoolSize);
        executor.setQueueCapacity(executorQueueCapacity);
        return executor;
    }

    /**
     * The {@link org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(schedulerPoolSize));
    }
}
