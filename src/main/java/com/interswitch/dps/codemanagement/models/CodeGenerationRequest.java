package com.interswitch.dps.codemanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection="code_generation_requests")
public class CodeGenerationRequest extends BaseEntity {
    @Id
    private String id;
    @NotBlank(message = "client id is required")
    private Long clientId;
    @Min(value=1,message = "Number of codes cannot be less than 1")
    private Long numberOfCodes;
    @Min(value=1,message = "length of codes cannot be less than 1")
    private Integer codeLength;
    @Min(value=1,message = "Number of codes per file cannot be less than 1")
    private Integer codePerFile;
    private String prefixValue;
    @NotEmpty(message = "please specify code generation algorithm")
    private String codeType;
    private Integer numberOfFileReusable = 0;
    private String status;

}
