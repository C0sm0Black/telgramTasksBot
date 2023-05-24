package pro.sky.telegrambot.sheduler;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.interfaces.NotificationTaskService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class NotificationScheduler {

    private Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
    private final NotificationTaskService notificationTaskService;

    public NotificationScheduler(NotificationTaskService notificationTaskService) {
        this.notificationTaskService = notificationTaskService;
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public Collection<SendMessage> executeNotificationTask() {

        List<SendMessage> sendMessages = new ArrayList<>();

        if (!notificationTaskService
                .getNotificationTaskByLocalDateTimeTask(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .isEmpty()) {

            notificationTaskService.getNotificationTaskByLocalDateTimeTask(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                    .stream()
                    .forEach(notificationTask -> {

                        String answer = "Dear " + notificationTask.getUserName() + " you asked to be reminded "
                                + notificationTask.getText();

                        sendMessages.add(new SendMessage(notificationTask.getIdChat(), answer));

                    });

        }

        return sendMessages;

    }
}
