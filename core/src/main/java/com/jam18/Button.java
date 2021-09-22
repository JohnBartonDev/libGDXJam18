package com.jam18;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Button extends InputAdapter {

    public interface ButtonListener {
        public default void activateButton() {}
        public default void touchDown() {}
        public default void touchDownSelected() {}
        public default void touchUp() {}
        public default void touchUpSelected(){}
    }

    protected boolean isActive;
    protected Rectangle touchBounds;
    protected Array<ButtonListener> listeners;

    public Button(float x, float y, float width, float height) {
        touchBounds = new Rectangle(x, y, width, height);
        listeners = new Array<>();
    }

    public Rectangle getTouchBounds(){
        return touchBounds;
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public boolean isActive(){
        return isActive;
    }

    public void addListener(ButtonListener listener){
        listeners.add(listener);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!isActive) return false;
        if(listeners.size == 0) return false;
        if(contains(screenX, screenY)) {
            for(int i = 0, size = listeners.size; i < size; i++) {
                listeners.get(i).touchDownSelected();
            }
        }
        else {
            for(int i = 0, size = listeners.size; i < size; i++) {
                listeners.get(i).touchDown();
            }
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!isActive) return false;
        if(listeners.size == 0) return false;
        if(contains(screenX, screenY)) {
            for(int i = 0, size = listeners.size; i < size; i++) {
                listeners.get(i).touchUpSelected();
            }
        }
        else {
            for(int i = 0, size = listeners.size; i < size; i++) {
                listeners.get(i).touchUp();
            }
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    public boolean contains(float x, float y){
        return touchBounds.contains(x, y);
//        return touchBounds.x <= x && touchBounds.x + touchBounds.width >= x && touchBounds.y <= y && touchBounds.y + touchBounds.height >= y;
    }

    public void drawDebug(ShapeDrawer shapeDrawer){
        float old = shapeDrawer.setColor(Color.RED);
        shapeDrawer.rectangle(touchBounds);
        shapeDrawer.setColor(old);
    }
}
