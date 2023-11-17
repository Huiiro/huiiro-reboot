package com.huii.auth.core.entity;

import lombok.Data;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

@Data
public class RectangleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
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

    public RectangleDto(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
