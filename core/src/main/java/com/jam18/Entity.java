package com.jam18;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Entity implements Pool.Poolable {

    //For animation purposes
    protected boolean isFinished;
    protected boolean isDead;
    protected float width;
    protected float height;
    protected Vector2 position;
    protected Vector2 centerPosition;
    protected Rectangle hitBox;

    public Entity() {
        this(0, 0, 0, 0);
    }

    public Entity(float x, float y, float width, float height){
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        hitBox = new Rectangle(x, y, width, height);
        centerPosition = new Vector2(x + (width * 0.5f), y + (height * 0.5f));
    }

    public void setDead(){
        isDead = true;
    }

    public boolean isDead(){
        return isDead;
    }

    public void setFinished(){
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Vector2 getPosition(){
        return position;
    }

    public void setPosition(Vector2 pos){
        setPosition(pos.x, pos.y);
    }

    public void setPosition(float x, float y){
        setX(x);
        setY(y);
    }

    public void set(float x, float y, float width, float height){
        setPosition(x, y);
        setSize(width, height);
    }

    public void setX(float x){
        position.x = x;
        hitBox.x = x;
        centerPosition.x = x + (width * 0.5f);
    }

    public void setY(float y){
        position.y = y;
        hitBox.y = y;
        centerPosition.y = y + (height * 0.5f);
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public void setSize(float width, float height){
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(float width){
        this.width = width;
        hitBox.width = width;
        centerPosition.x = position.x + (width * 0.5f);
    }

    public void setHeight(float height){
        this.height = height;
        hitBox.height = height;
        centerPosition.y = position.y + (height * 0.5f);
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public boolean collides(Entity e){
        if(this.isDead() || e.isDead()) return false;
        return hitBox.overlaps(e.hitBox);
    }

    @Override
    public void reset() {
        isDead = false;
        isFinished = false;
        width = 0;
        height = 0;
        position.set(0, 0);
        hitBox.set(0, 0, 0, 0);
    }
}
