package com.interswitch.dps.codemanagement.services.impl;

import com.interswitch.dps.codemanagement.exceptions.SequenceException;
import com.interswitch.dps.codemanagement.models.DatabaseSequence;
import com.interswitch.dps.codemanagement.services.interfaces.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private final MongoOperations mongoOperation;

    public SequenceGeneratorServiceImpl(@Lazy MongoOperations mongoOperation) {
        this.mongoOperation = mongoOperation;
    }

    public long getNextSequenceId(String key) throws SequenceException {
        // get sequence id
        Query query = new Query(Criteria.where("_key").is(key));
        // increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);
        // return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        // this is where the magic happens.
        DatabaseSequence counter = mongoOperation.findAndModify(query, update, options, DatabaseSequence.class);
        // if no id, throws SequenceException
        if (counter == null) {
            throw new SequenceException("Failed to generate next sequence Id.You might need to ensure that a database sequence collection is created manually.");
        }
        // optional, just a way to tell user when the sequence id is failed to generate.
        return !Objects.isNull(counter) ? counter.getSeq() : 1;

    }

    public void dropSequencesCollection(String collectionName) {
        mongoOperation.dropCollection(collectionName);
    }

}
