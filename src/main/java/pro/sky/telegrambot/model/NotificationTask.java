package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue
    private Long idTask;
    private LocalDateTime notifyTaskDateTime;
    private String text;
    private Long idChat;
    private String userName;

    public NotificationTask() {
    }

    public Long getIdTask() {
        return idTask;
    }

    public void setIdTask(Long idTask) {
        this.idTask = idTask;
    }

    public LocalDateTime getNotifyTaskDateTime() {
        return notifyTaskDateTime;
    }

    public void setNotifyTaskDateTime(LocalDateTime notifyTaskDateTime) {
        this.notifyTaskDateTime = notifyTaskDateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(idTask, that.idTask) && Objects.equals(notifyTaskDateTime, that.notifyTaskDateTime) && Objects.equals(text, that.text) && Objects.equals(idChat, that.idChat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTask, notifyTaskDateTime, text, idChat);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "idTask=" + idTask +
                ", notifyTaskDateTime=" + notifyTaskDateTime +
                ", text='" + text + '\'' +
                ", idChat=" + idChat +
                '}';
    }

}
