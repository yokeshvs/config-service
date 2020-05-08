package com.k8sClient.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.k8sClient.model.ConfigMapEntry;
import com.k8sClient.model.ConfigMapResponse;
import com.k8sClient.projection.Views;
import com.k8sClient.service.K8sConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/config",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminConfigProvider {

    @Autowired
    private K8sConfigService k8sConfigService;

    @GetMapping(path = "/{namespace}/{configName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Public.class)
    public ResponseEntity<ConfigMapResponse> listConfigs(@PathVariable(name = "namespace") String namespace, @PathVariable(name = "configName") String configName) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(k8sConfigService.listConfigMapData(namespace, configName));
    }

    @PatchMapping(path = "/{namespace}/{configName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Public.class)
    public ResponseEntity<ConfigMapResponse> editConfigEntry(@PathVariable(name = "namespace") String namespace, @PathVariable(name = "configName") String configName, @RequestBody ConfigMapEntry configMapEntry) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(k8sConfigService.editConfigMapEntry(namespace, configName, configMapEntry));
    }

    @GetMapping(path = "/hello", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Public.class)
    public ResponseEntity<String> hello() throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body("hello there!");
    }
}
