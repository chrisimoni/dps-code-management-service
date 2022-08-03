package com.interswitch.dps.codemanagement.repositories;

import com.interswitch.dps.codemanagement.models.CodeGenerationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CodeManagementRepository extends MongoRepository<CodeGenerationRequest, String> {
}
