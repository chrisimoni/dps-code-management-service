package com.interswitch.dps.codemanagement.configs;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.interswitch.dps.codemanagement.*"})
public class MongoConfiguration extends AbstractMongoClientConfiguration {


    @Value("${dps.code-management.database.host}")
    private String mongoHost;

    @Value("${dps.code-management.database.name}")
    private String databaseName;

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoHost);
    }
    protected String getDatabaseName() {
        return databaseName;
    }

    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(),getDatabaseName());
    }

    @Override
    public boolean autoIndexCreation() {
        return true;
    }
}
