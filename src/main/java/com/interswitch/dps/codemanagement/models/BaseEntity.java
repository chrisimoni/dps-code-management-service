package com.interswitch.dps.codemanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BaseEntity {

    @JsonIgnore
    private LocalDateTime createdDate;

    @JsonIgnore
    private LocalDateTime lastModifiedDate;

    @JsonIgnore
    private boolean deletionStatus;
}
