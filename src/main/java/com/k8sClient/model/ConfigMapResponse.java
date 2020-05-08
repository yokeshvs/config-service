package com.k8sClient.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.k8sClient.projection.Views;
import io.fabric8.kubernetes.api.model.ConfigMap;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonView(Views.Public.class)
public class ConfigMapResponse {
    private String namespace;
    private String configMapName;
    private List<ConfigMapEntry> configMapEntries;
}
