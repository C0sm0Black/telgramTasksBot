package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.interfaces.NotificationTaskService;
import pro.sky.telegrambot.model.NotificationTask;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static final Pattern PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    private final NotificationTaskService notificationTaskService;

    public TelegramBotUpdatesListener(NotificationTaskService notificationTaskService) {
        this.notificationTaskService = notificationTaskService;
    }

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {

            logger.info("Processing update: {}", update);

            /*
            Check for message "/start"
             */
            startMessage(update);

            /*
            ad
             */
            createNewNotificationTaskInDataBase(update);

            /*
            asd
             */
            executeNotificationTask();


        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;

    }

    private void startMessage(Update update) {

        if (update.message().text().equals("/start")) {

            String answer = "Hi " + update.message().chat().username();
            SendMessage sendMessage;
            sendMessage = new SendMessage(update.message().chat().id(), answer);

            telegramBot.execute(sendMessage);

        }

    }

    private void createNewNotificationTaskInDataBase(Update update) {

        if (checkForPattern(update.message().text())) {

            notificationTaskService.createNotificationTask(createNewNotificationTask(update.message()));

        }

    }

    private boolean checkForPattern(String text) {

        Matcher matcher = PATTERN.matcher(text);

        if (matcher.matches()) {
            return true;
        }

        return false;

    }

    private NotificationTask createNewNotificationTask(Message message) {

        String text = message.text();
        Matcher matcher = PATTERN.matcher(text);
        String date = null;
        String task = null;

        if (matcher.matches()) {

            date = matcher.group(1);
            task = matcher.group(3);

        }

        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setNotifyTaskDateTime(localDateTime);
        notificationTask.setText(task);
        notificationTask.setIdChat(message.chat().id());
        notificationTask.setUserName(message.chat().username());

        return notificationTask;

    }

    @Scheduled(cron = "0 0/1 * * * *")
    private void executeNotificationTask() {

        if (!notificationTaskService
                .getNotificationTaskByLocalDateTimeTask(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .isEmpty()) {

            notificationTaskService.getNotificationTaskByLocalDateTimeTask(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                    .stream()
                    .forEach(notificationTask -> {

                        String answer = "Dear " + notificationTask.getUserName() + " you asked to be reminded "
                                + notificationTask.getText();

                        SendMessage sendMessage;
                        sendMessage = new SendMessage(notificationTask.getIdChat(), answer);

                        SendResponse response = telegramBot.execute(sendMessage);

                        if (!response.isOk()) {
                            logger.error("Error number is: {}" + response.errorCode());
                        }

                    });

        }

    }

}
