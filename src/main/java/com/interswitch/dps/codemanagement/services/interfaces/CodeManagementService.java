package com.interswitch.dps.codemanagement.services.interfaces;

import com.interswitch.dps.codemanagement.dto.CodeValidationRequest;
import com.interswitch.dps.codemanagement.models.CodeGenerationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CodeManagementService {

    public List<String> getCodeGenerationTypes();

    //public void generateCodes(CodeGenerationRequest codeGenReq);

    void submitCodeGenerationRequest(CodeGenerationRequest request, boolean proceed);
    CodeGenerationRequest save(CodeGenerationRequest request);

    void validateCodes(CodeValidationRequest request);
}
