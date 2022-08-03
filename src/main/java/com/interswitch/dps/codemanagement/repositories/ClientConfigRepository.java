package com.interswitch.dps.codemanagement.repositories;

import com.interswitch.dps.codemanagement.models.ClientConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientConfigRepository extends MongoRepository<ClientConfig, String> {
    ClientConfig findByClientId(Long clientId);
    //findBy
}
