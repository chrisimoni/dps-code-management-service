package com.interswitch.dps.codemanagement.repositories;

import com.interswitch.dps.codemanagement.models.CodeGenerationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MongoTemplateDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean checkDuplicateRequest(CodeGenerationRequest request) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(request.getClientId())
                //.and("numberOfCodes").is(request.getNumberOfCodes())
                .and("codeLength").is(request.getCodeLength())
                .and("prefixValue").is(request.getPrefixValue())
                .and("codeType").is(request.getCodeType())
                .and("numberOfFileReusable").is(request.getNumberOfFileReusable()));

        List<CodeGenerationRequest> result = mongoTemplate.find(query, CodeGenerationRequest.class);

        return result.size() < 1 || result.isEmpty() ? false : true;
    }
}
