package com.interswitch.dps.codemanagement.services.interfaces;

import com.interswitch.dps.codemanagement.exceptions.SequenceException;
import org.springframework.stereotype.Service;

@Service
public interface SequenceGeneratorService {

    public abstract long getNextSequenceId(String key) throws SequenceException;
}
