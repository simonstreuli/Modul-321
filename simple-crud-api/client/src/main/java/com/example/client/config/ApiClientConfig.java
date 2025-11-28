package com.example.client.config;

import com.example.client.ApiClient;
import com.example.client.api.ItemsApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiClientConfig {

    @Value("${api.server.url:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApiClient apiClient(RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(serverUrl);
        return apiClient;
    }

    @Bean
    public ItemsApi itemsApi(ApiClient apiClient) {
        return new ItemsApi(apiClient);
    }
}
