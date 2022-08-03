package com.interswitch.dps.codemanagement.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@Document(collection="client_hashed_codes")
public class ClientHashedCode extends BaseEntity {
    @Id
    private String id;
    private String generatedCodeFileId;
    private String hashedCode;
    private String campaignId;
    private Boolean isUsed;
}
