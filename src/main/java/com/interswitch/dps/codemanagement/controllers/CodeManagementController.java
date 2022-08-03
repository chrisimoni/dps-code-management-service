package com.interswitch.dps.codemanagement.controllers;

import com.interswitch.dps.codemanagement.dto.CodeValidationRequest;
import com.interswitch.dps.codemanagement.exceptions.BadRequestException;
import com.interswitch.dps.codemanagement.response.CustomResponse;
import com.interswitch.dps.codemanagement.models.CodeGenerationRequest;
import com.interswitch.dps.codemanagement.services.interfaces.CodeManagementService;
import com.interswitch.dps.codemanagement.utils.ErrorResponseManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1/dps")
@Api(tags = "Code Generation And Management", description = "The collection of REST API Endpoints to manage codes generation on DPS 2.0 code management service", hidden = false, produces = "application/json")
public class CodeManagementController {

    @Autowired
    private CodeManagementService codeManagementService;

    @GetMapping(value="/codes/algorithms",produces="application/json")
    @ApiOperation(value = "Code Generation Algorithms", notes = "Get code generation types on DPS 2.0 code management service.")
    public @ResponseBody
    ResponseEntity<?> getCodeGenerationTypes() {

        List<String> algorithms = codeManagementService.getCodeGenerationTypes();
        CustomResponse<?> responseBody = new CustomResponse.CustomResponseBuilder<>()
                .withCode("200")
                .withMessage("fetched list of code generation types successfully.")
                .withTimestamp(new Date())
                .withData(algorithms)
                .withStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(responseBody, responseBody.getStatus());

    }

    @PostMapping(value="/codes/generate",produces="application/json")
    @ApiOperation(value = "Code Generation", notes = "Generate codes on DPS 2.0 code management service.")
    public ResponseEntity<?> generateCodes(@RequestParam(value = "proceed", required = false, defaultValue = "false") boolean proceed, @RequestBody @Validated CodeGenerationRequest request, BindingResult result) {

        if (result.hasErrors()) {
            throw new BadRequestException("" + ErrorResponseManager.getErrorMessages(result));
        }

        codeManagementService.submitCodeGenerationRequest(request, proceed);
        CustomResponse<?> responseBody = new CustomResponse.CustomResponseBuilder<>()
                .withCode("200")
                .withMessage("code request submitted successfully for processing")
                .withTimestamp(new Date())
                .withData("")
                .withStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(responseBody, responseBody.getStatus());

    }

    @PostMapping(value = "/codes/validate", produces="application/json")
    @ApiOperation(value = "Validate Codes", notes = "Validate codes on DPS 2.0 code management service.")
    public ResponseEntity<?> validateCodes(@RequestBody CodeValidationRequest request) {
        if(request == null) {
            throw new BadRequestException("RequestBody is required");
        }

        codeManagementService.validateCodes(request);
        CustomResponse<?> responseBody = new CustomResponse.CustomResponseBuilder<>()
                .withCode("200")
                .withMessage("code validation request submitted successfully for processing")
                .withTimestamp(new Date())
                .withData("")
                .withStatus(HttpStatus.OK).build();

        return new ResponseEntity<>(responseBody, responseBody.getStatus());
    }
}
