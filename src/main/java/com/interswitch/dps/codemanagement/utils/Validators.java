package com.interswitch.dps.codemanagement.utils;

import com.interswitch.dps.codemanagement.enums.CodeGenerationTypes;
import com.interswitch.dps.codemanagement.exceptions.BadRequestException;
import com.interswitch.dps.codemanagement.models.ClientConfig;
import com.interswitch.dps.codemanagement.models.CodeGenerationRequest;
import org.springframework.stereotype.Component;

@Component
public class Validators {
    public void validateCodeGenerationRequestFields(CodeGenerationRequest request) throws BadRequestException {

        if (request.getClientId() == null) {
            throw new BadRequestException("clientId is required and cannot be null");
        }

        if (request.getNumberOfCodes() == null || request.getNumberOfCodes() < 1) {
            throw new BadRequestException("Number of codes cannot be less than 1");
        }

        if (request.getCodeLength() == null || request.getCodeLength() < 1) {
            throw new BadRequestException("code length cannot be less than 1");
        }

        if(request.getCodePerFile() != null) {
            if(request.getCodePerFile() < 1 || request.getCodePerFile() > 10000000) {
                throw new BadRequestException("number of code per file cannot be less than 1 or greater than 10, 000, 000");
            }
        }

        if(isNullOrEmptyString(request.getCodeType())) {
            throw new BadRequestException("algorithm cannot be null or empty");
        }

        CodeGenerationTypes.get(request.getCodeType());
    }

    public static boolean isNullOrEmptyString(String stringValue) {
        return (stringValue == null || stringValue.trim().isEmpty());
    }

    public void validateClientConfigRequestFields(ClientConfig clientConfig) {
        if(clientConfig.getClientId() == null){
            throw new BadRequestException("Please clientId cannot be null");
        }

        if(isNullOrEmptyString(clientConfig.getClientName())) {
            throw new BadRequestException("Please clientName cannot be null");
        }

        if(isNullOrEmptyString(clientConfig.getAlgorithmKey())) {
            throw new BadRequestException("Please algorithmKey cannot be null");
        }
    }
}
