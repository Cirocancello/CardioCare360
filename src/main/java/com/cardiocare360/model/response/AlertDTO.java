package com.cardiocare360.model.response;

public class AlertDTO {

    private String message;   // Testo dell’alert
    private String type;      // warning, error, info

    public AlertDTO() {}

    public AlertDTO(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
