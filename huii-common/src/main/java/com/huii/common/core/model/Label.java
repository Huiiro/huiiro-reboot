package com.huii.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Label vo
 *
 * @author huii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Label implements Serializable {

    private Long id;

    private String label;

    private String data;

    public Label(Long id, String label) {
        this.id = id;
        this.label = label;
    }

}
