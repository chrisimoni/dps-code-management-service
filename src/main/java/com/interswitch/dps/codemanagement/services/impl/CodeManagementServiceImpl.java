package com.interswitch.dps.codemanagement.services.impl;

import com.interswitch.dps.codemanagement.async.AsyncRunner;
import com.interswitch.dps.codemanagement.dto.CodeValidationRequest;
import com.interswitch.dps.codemanagement.enums.CodeProcessStatus;
import com.interswitch.dps.codemanagement.exceptions.BadRequestException;
import com.interswitch.dps.codemanagement.exceptions.RecordNotFoundException;
import com.interswitch.dps.codemanagement.models.ClientConfig;
import com.interswitch.dps.codemanagement.models.CodeGenerationRequest;
import com.interswitch.dps.codemanagement.models.GeneratedCodeFile;
import com.interswitch.dps.codemanagement.repositories.CodeManagementRepository;
import com.interswitch.dps.codemanagement.repositories.GeneratedCodeFileRepository;
import com.interswitch.dps.codemanagement.repositories.MongoTemplateDao;
import com.interswitch.dps.codemanagement.services.interfaces.ClientConfigService;
import com.interswitch.dps.codemanagement.services.interfaces.CodeManagementService;
import com.interswitch.dps.codemanagement.enums.CodeGenerationTypes;
import com.interswitch.dps.codemanagement.utils.Validators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CodeManagementServiceImpl implements CodeManagementService {
    @Autowired
    private Validators validators;
    @Autowired
    private CodeManagementRepository codeManagementRepository;
    @Autowired
    private ClientConfigService clientConfigService;
    @Autowired
    private AsyncRunner asyncRunner;
    @Autowired
    private MongoTemplateDao mongoTemplateDao;
    @Autowired
    private GeneratedCodeFileRepository generatedCodeFileRepository;

    public List<String> getCodeGenerationTypes() {
        List<String> types = new ArrayList<>();
        types.add(CodeGenerationTypes.ALPHANUMERIC.name());
        types.add(CodeGenerationTypes.ALPHABETICAL.name());
        types.add(CodeGenerationTypes.NUMERIC.name());
        return types;
    }

    @Override
    public void submitCodeGenerationRequest(CodeGenerationRequest request, boolean proceed) {
        validators.validateCodeGenerationRequestFields(request);

        ClientConfig config = clientConfigService.getClientConfig(request.getClientId());
        if(config == null){
            throw new RecordNotFoundException("No config record found for client with id:"+request.getClientId());
        }

        List<String> errorList = new ArrayList<>();
        if(!proceed) {
            if(mongoTemplateDao.checkDuplicateRequest(request)) {
                errorList.add("A code set with similar rule already exist. " +
                        "It is advisable to change to change the code settings to avoid code duplications. " +
                        "Would you like to proceed with the current settings ?");
            }

            if(request.getCodeType().equalsIgnoreCase(CodeGenerationTypes.NUMERIC.name())) {
                errorList.add("For Numeric type, the number of codes that can be generated could be within 10 - 25% of the available numbers in that range");
                int numOfCodesLen = String.valueOf(request.getNumberOfCodes()).length();

                if(numOfCodesLen - request.getCodeLength() == 1 && request.getCodeLength() < 9) {
                    errorList.add("To generate more values, you are advised to increase code length to "+(request.getCodeLength() + 1));
                }
            }

            if(!errorList.isEmpty()) {
                throw new BadRequestException(errorList.toString());
            }
        }

        request.setStatus(CodeProcessStatus.PENDING.name());
        CodeGenerationRequest savedRequest = codeManagementRepository.insert(request);

        asyncRunner.generateClientCodes(savedRequest);
    }

    @Override
    public CodeGenerationRequest save(CodeGenerationRequest request) {
        return codeManagementRepository.save(request);
    }

    @Override
    public void validateCodes(CodeValidationRequest request) {
        if(Validators.isNullOrEmptyString(request.getCodeRequestId())) {
            throw new BadRequestException("Please codeRequestId cannot be null");
        }

        GeneratedCodeFile generatedCodeFile = generatedCodeFileRepository.findByCodeGenerationRequestId(request.getCodeRequestId());
        if(generatedCodeFile == null) {
            throw new BadRequestException("No record found for the provided id");
        }

        if(generatedCodeFile.getIsActivated()) {
            throw new BadRequestException("The codes attached to this request id has already been activated");
        }

        try {
            asyncRunner.activateCodes(generatedCodeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
