package com.smalaca.taskamanager.bdd.client;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class ProjectManagementClientFactory {
    public static ProjectManagementClient create(String url) {
        return new ProjectManagementClient(restTemplate(), url);
    }

    private static RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {

            }
        });

        return restTemplate;
    }
}
