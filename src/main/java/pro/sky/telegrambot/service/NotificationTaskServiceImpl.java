package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.interfaces.NotificationTaskService;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class NotificationTaskServiceImpl implements NotificationTaskService {

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskServiceImpl(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Override
    public NotificationTask createNotificationTask(NotificationTask notificationTask) {

        if (notificationTask == null) {
            throw new IllegalArgumentException("Task not set!");
        }

        return notificationTaskRepository.save(notificationTask);

    }

    @Override
    public Collection<NotificationTask> getNotificationTaskByLocalDateTimeTask(LocalDateTime localDateTime) {
        return notificationTaskRepository.findNotificationTaskByNotifyTaskDateTime(localDateTime);
    }

}
