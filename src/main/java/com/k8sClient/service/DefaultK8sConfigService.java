package com.k8sClient.service;

import com.k8sClient.model.ConfigMapEntry;
import com.k8sClient.model.ConfigMapResponse;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ConfigMapList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DefaultK8sConfigService implements K8sConfigService {
    @Autowired
    private KubernetesClient k8sClient;

    @Autowired
    private CoreV1Api coreV1Api;

    @Override
    public ConfigMapResponse listConfigMapData(String namespace, String configMapName) {
        ConfigMapList configMapList = k8sClient.configMaps().inNamespace(namespace).list();
        ConfigMapResponse configMapResponse = new ConfigMapResponse();
        List<ConfigMap> requestedConfigMapList = configMapList.getItems().stream().filter(configMap -> configMap.getMetadata().getName().equals(configMapName)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(requestedConfigMapList)) {
            ConfigMap requestedConfigMap = requestedConfigMapList.get(0);
            List<ConfigMapEntry> configMapEntries = new ArrayList<>();
            configMapResponse.setConfigMapName(requestedConfigMap.getMetadata().getName());
            configMapResponse.setNamespace(requestedConfigMap.getMetadata().getNamespace());
            requestedConfigMap.getData().forEach((key, value) -> {
                configMapEntries.add(new ConfigMapEntry(key, value));
            });
            configMapResponse.setConfigMapEntries(configMapEntries);
        } else {
            configMapResponse.setConfigMapName(configMapName);
            configMapResponse.setNamespace(namespace);
        }
        return configMapResponse;
    }

    @Override
    public ConfigMapResponse editConfigMapEntry(String namespace, String configMapName, ConfigMapEntry configMapEntry) {
        ConfigMap updatedMap = k8sClient.configMaps().inNamespace(namespace).withName(configMapName).edit().addToData(configMapEntry.getCmKey(), configMapEntry.getCmValue()).done();
        ConfigMapResponse savedConfigMapResponse = new ConfigMapResponse();
        savedConfigMapResponse.setNamespace(updatedMap.getMetadata().getNamespace());
        savedConfigMapResponse.setConfigMapName(updatedMap.getMetadata().getName());
        List<ConfigMapEntry> configMapEntries = new ArrayList<>();
        updatedMap.getData().forEach((key, value) -> {
            configMapEntries.add(new ConfigMapEntry(key, value));
        });
        savedConfigMapResponse.setConfigMapEntries(configMapEntries);
        return savedConfigMapResponse;
    }


    @Override
    public ConfigMapResponse listConfigMapDataByDefaultClient(String namespace, String configMapName) throws ApiException {
        V1ConfigMapList v1ConfigMapList = coreV1Api.listNamespacedConfigMap(namespace, null, null, null, null, null, null, null, null, null);
        ConfigMapResponse savedConfigMapResponse = new ConfigMapResponse();
        Optional<V1ConfigMap> v1ConfigMapOptional = v1ConfigMapList.getItems().stream().filter(v1ConfigMap1 -> Objects.equals(Objects.requireNonNull(v1ConfigMap1.getMetadata()).getName(), configMapName)).findFirst();
        if (v1ConfigMapOptional.isPresent()) {
            V1ConfigMap v1ConfigMap = v1ConfigMapOptional.get();
            savedConfigMapResponse.setConfigMapName(Objects.requireNonNull(v1ConfigMap.getMetadata()).getName());
            savedConfigMapResponse.setNamespace(Objects.requireNonNull(v1ConfigMap.getMetadata()).getNamespace());
            List<ConfigMapEntry> configMapEntries = new ArrayList<>();
            Objects.requireNonNull(v1ConfigMap.getData()).forEach((key, value) -> {
                configMapEntries.add(new ConfigMapEntry(key, value));
            });
            savedConfigMapResponse.setConfigMapEntries(configMapEntries);
        } else {
            savedConfigMapResponse.setConfigMapName(configMapName);
            savedConfigMapResponse.setNamespace(namespace);
        }
        return savedConfigMapResponse;
    }
}
