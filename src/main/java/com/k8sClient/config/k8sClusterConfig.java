package com.k8sClient.config;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class k8sClusterConfig {

    @Bean
    public KubernetesClient initFabric8Client() throws IOException {
        Config config = new ConfigBuilder().build();
        return new DefaultKubernetesClient(config);
    }

    @Bean
    public CoreV1Api initDefaultClient() throws IOException {
        ApiClient apiClient = io.kubernetes.client.util.Config.defaultClient();
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(apiClient);
        return new CoreV1Api(apiClient);
    }
}
