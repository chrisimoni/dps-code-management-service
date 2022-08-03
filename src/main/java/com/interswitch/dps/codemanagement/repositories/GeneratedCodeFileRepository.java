package com.interswitch.dps.codemanagement.repositories;

import com.interswitch.dps.codemanagement.dto.CodeValidationRequest;
import com.interswitch.dps.codemanagement.models.GeneratedCodeFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneratedCodeFileRepository extends MongoRepository<GeneratedCodeFile, String> {
    GeneratedCodeFile findByCodeGenerationRequestId(String requestId);
}
