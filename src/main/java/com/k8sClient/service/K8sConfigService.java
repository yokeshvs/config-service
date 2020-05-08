package com.k8sClient.service;

import com.k8sClient.model.ConfigMapEntry;
import com.k8sClient.model.ConfigMapResponse;
import io.kubernetes.client.openapi.ApiException;

import java.io.IOException;

public interface K8sConfigService {
    ConfigMapResponse listConfigMapData(String namespace, String configMapName) throws ApiException, IOException;
    ConfigMapResponse editConfigMapEntry(String namespace, String configMapName, ConfigMapEntry configMapEntry);
    ConfigMapResponse listConfigMapDataByDefaultClient(String namespace, String configMapName) throws ApiException;
}
