package com.interswitch.dps.codemanagement.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document(collection="client_configs")
public class ClientConfig {
    @Id
    private String id;
    @NotBlank(message = "client id is required")
    @Indexed(unique = true)
    private Long clientId;
    @NotBlank(message = "client name is required")
    private String clientName;
    @NotBlank(message = "algorithm key is required")
    private String algorithmKey;
}
