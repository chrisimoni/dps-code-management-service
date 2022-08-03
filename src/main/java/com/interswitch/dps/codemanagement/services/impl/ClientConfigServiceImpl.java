package com.interswitch.dps.codemanagement.services.impl;

import com.interswitch.dps.codemanagement.exceptions.BadRequestException;
import com.interswitch.dps.codemanagement.exceptions.RecordNotFoundException;
import com.interswitch.dps.codemanagement.models.ClientConfig;
import com.interswitch.dps.codemanagement.repositories.ClientConfigRepository;
import com.interswitch.dps.codemanagement.services.interfaces.ClientConfigService;
import com.interswitch.dps.codemanagement.utils.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientConfigServiceImpl implements ClientConfigService {
    @Autowired
    private ClientConfigRepository clientConfigRepository;
    @Autowired
    private Validators validators;

    @Override
    public ClientConfig createClientConfig(ClientConfig clientConfig) {
        validators.validateClientConfigRequestFields(clientConfig);

        ClientConfig config = clientConfigRepository.findByClientId(clientConfig.getClientId());
        if(config != null) {
            throw new BadRequestException("Code configuration already exist for this client");
        }

        return clientConfigRepository.insert(clientConfig);
    }

    @Override
    public List<ClientConfig> getAllClientConfigs() {
        return clientConfigRepository.findAll();
    }

    @Override
    public ClientConfig getClientConfig(Long clientId) {
        if(clientId == null) {
            throw new BadRequestException("clientId is required in the request header");
        }

        ClientConfig clientConfig =  clientConfigRepository.findByClientId(clientId);
        if(clientConfig==null){
            throw new RecordNotFoundException("No config record found for client with id:"+clientId);
        }

        return clientConfig;
    }
}
