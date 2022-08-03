package com.interswitch.dps.codemanagement.enums;

import com.interswitch.dps.codemanagement.exceptions.BadRequestException;

public enum CodeGenerationTypes {
    ALPHANUMERIC, ALPHABETICAL, NUMERIC;

    public static CodeGenerationTypes get(String type) throws BadRequestException {
        CodeGenerationTypes value;
        try {
            value = CodeGenerationTypes.valueOf(type);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BadRequestException(
                    "Invalid codeType. Valid values are: ALPHANUMERIC, ALPHABETICAL, or NUMERIC");
        }
        return value;
    }
}
