package com.k8sClient.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.k8sClient.projection.Views;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonView(Views.Public.class)
public class ConfigMapEntry {
    private String cmKey;
    private String cmValue;
}
