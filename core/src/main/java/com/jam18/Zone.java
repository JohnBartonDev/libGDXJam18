package com.jam18;

import com.badlogic.gdx.math.Rectangle;

public class Zone {

    public final int row;
    public final int column;
    private final Rectangle bounds;
    public final int zoneToRenderIndex;

    public Zone(float x, float y, float width, float height, int row, int column, int zoneToRender) {
        bounds = new Rectangle(x, y, width, height);
        this.row = row;
        this.column = column;
        this.zoneToRenderIndex = zoneToRender;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean overlaps(Rectangle r) {
        return bounds.overlaps(r);
    }

    public float getX() {
        return bounds.x;
    }

    public float getY() {
        return bounds.y;
    }

    public float getWidth() {
        return bounds.width;
    }

    public float getHeight() {
        return bounds.height;
    }

    @Override
    public String toString() {
        return "(" + column + "," + row + ") : indexToRender: " + zoneToRenderIndex;
    }
}
