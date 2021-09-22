package com.jam18;

import com.badlogic.gdx.graphics.Color;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class TestButton extends Button {

    private Color color;

    public TestButton(float x, float y, float width, float height, Color color){
        super(x, y, width, height);
        this.color = new Color(color);
    }

    public void draw(ShapeDrawer shapeDrawer){
        float old = shapeDrawer.setColor(color);
        shapeDrawer.filledRectangle(touchBounds);
        shapeDrawer.setColor(old);
    }
}
