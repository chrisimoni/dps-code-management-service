package com.interswitch.dps.codemanagement.controllers;

import com.interswitch.dps.codemanagement.exceptions.BadRequestException;
import com.interswitch.dps.codemanagement.response.CustomResponse;
import com.interswitch.dps.codemanagement.models.ClientConfig;
import com.interswitch.dps.codemanagement.services.interfaces.ClientConfigService;
import com.interswitch.dps.codemanagement.utils.ErrorResponseManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/v1/dps")
@Api(tags = "Clients Code Configuration", description = "The collection of REST API Endpoints to manage clients on DPS 2.0 code management service", hidden = false, produces = "application/json")
public class ClientConfigController {
    @Autowired
    private ClientConfigService clientConfigService;

    @PostMapping(value="/configs",consumes="application/json",produces="application/json")
    @ApiOperation(value = "Create Client Code Config", notes = "creates a new client code config on DPS 2.0 code management service.")
    public ResponseEntity<?> createClientConfig(@RequestBody @Validated ClientConfig clientConfig, BindingResult result) {

        if(clientConfig == null) {
            throw new BadRequestException("RequestBody is required");
        }

        if (result.hasErrors()) {
            throw new BadRequestException("" + ErrorResponseManager.getErrorMessages(result));
        }

        ClientConfig config = clientConfigService.createClientConfig(clientConfig);
        CustomResponse<?> responseBody = new CustomResponse.CustomResponseBuilder<>()
                .withCode("201")
                .withMessage("client code config created successfully.")
                .withTimestamp(new Date())
                .withData(config)
                .withStatus(HttpStatus.CREATED).build();
        return new ResponseEntity<>(responseBody, responseBody.getStatus());

    }

    @GetMapping(value="/configs/all",produces="application/json")
    @ApiOperation(value = "Get All Clients Code Configuration", notes = "get all existing clients code configurations.")
    public ResponseEntity<?> getAllClientConfigs() {
        List<ClientConfig> clients  = clientConfigService.getAllClientConfigs();
        CustomResponse<?> responseBody = new CustomResponse.CustomResponseBuilder<>()
                .withCode("200")
                .withMessage(!clients.isEmpty()? "fetched all clients code configurations successfully." : "No clients code configuration saved saved yet")
                .withTimestamp(new Date())
                .withData(clients)
                .withStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(responseBody, responseBody.getStatus());

    }

    @GetMapping(value="/configs",produces="application/json")
    @ApiOperation(value = "Get Client Code Configuration", notes = "gets an existing clientv code configuration by clientId.")
    public ResponseEntity<?> getClientConfig(@RequestHeader(value = "clientId", required = true) long clientId) {
        ClientConfig client = clientConfigService.getClientConfig(clientId);
        CustomResponse<?> responseBody = new CustomResponse.CustomResponseBuilder<>()
                .withCode("200")
                .withMessage("client config found successfully.")
                .withTimestamp(new Date())
                .withData(client)
                .withStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(responseBody, responseBody.getStatus());

    }

}
