package ru.miniprog.minicrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;
import ru.miniprog.minicrm.model.MessageStatus;

import java.io.Serializable;

/**
 * DTO for {@link ru.miniprog.minicrm.model.Message}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto implements Serializable {
    String senderName;
    String message;
    String date;
    MessageStatus status;
}