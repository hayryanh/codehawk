package ai.codehawk.common.entity.github;

import java.time.OffsetDateTime;

public class PRMetadata {
    private Long prNumber;
    private String repository;
    private String title;
    private String url;
    private String userLogin;
    private String action;        // opened, edited, closed
    private String state;         // open, closed
    private OffsetDateTime timestamp;

    public Long getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(Long prNumber) {
        this.prNumber = prNumber;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PRMetadata{" +
                "prNumber=" + prNumber +
                ", repository='" + repository + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", action='" + action + '\'' +
                ", state='" + state + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
