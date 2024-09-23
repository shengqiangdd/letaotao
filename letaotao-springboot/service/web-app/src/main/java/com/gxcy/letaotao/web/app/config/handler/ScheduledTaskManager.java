package com.gxcy.letaotao.web.app.config.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
public class ScheduledTaskManager {

    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;

    @Autowired
    public ScheduledTaskManager(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    // 注册定时任务
    public void registerTask(Runnable task, Duration period) {
        scheduledFuture = taskScheduler.scheduleAtFixedRate(task, period);
    }

    // 取消定时任务
    public void cancelTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}

