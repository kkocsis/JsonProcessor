package com.kkocsis.jsonprocessor.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    @JsonRawValue
    private String message;

    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }

}
