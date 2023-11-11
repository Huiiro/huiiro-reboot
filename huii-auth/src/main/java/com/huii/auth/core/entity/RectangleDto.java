package com.huii.auth.core.entity;

import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
public class RectangleDto implements Serializable {
    private int x;
    private int y;
    private int width;
    private int height;

    public RectangleDto(Rectangle rectangle) {
        this.x = rectangle.x;
        this.y = rectangle.y;
        this.width = rectangle.width;
        this.height = rectangle.height;
    }
}
