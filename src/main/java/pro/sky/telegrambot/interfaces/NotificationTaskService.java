package pro.sky.telegrambot.interfaces;

import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

public interface NotificationTaskService {

    NotificationTask createNotificationTask(NotificationTask notificationTask);

    Collection<NotificationTask> getNotificationTaskByLocalDateTimeTask(LocalDateTime localDateTime);

}
