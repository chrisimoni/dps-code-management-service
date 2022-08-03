package com.interswitch.dps.codemanagement.services.interfaces;

import com.interswitch.dps.codemanagement.models.ClientConfig;

import java.util.List;

public interface ClientConfigService {
    ClientConfig createClientConfig(ClientConfig clientConfig);

    List<ClientConfig> getAllClientConfigs();

    ClientConfig getClientConfig(Long clientId);
}
