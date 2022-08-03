package com.interswitch.dps.codemanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection="generated_code_files")
public class GeneratedCodeFile extends BaseEntity {
    @Id
    private String id;
    @NotBlank(message = "code generation request id is required")
    private String codeGenerationRequestId;
    private List<GeneratedFile> plainCodeFiles;
    private List<GeneratedFile> hashedCodeFiles;
    private Boolean isActivated;
}
