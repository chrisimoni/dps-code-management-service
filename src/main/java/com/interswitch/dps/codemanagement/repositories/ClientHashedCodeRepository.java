package com.interswitch.dps.codemanagement.repositories;

import com.interswitch.dps.codemanagement.models.ClientHashedCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientHashedCodeRepository extends MongoRepository<ClientHashedCode, String> {
}
