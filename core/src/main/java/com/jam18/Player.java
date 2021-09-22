package com.jam18;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Player extends InputAdapter {

    float xAmount = 200;
    private float size = 60;
    boolean change = false;
    float timer;
    float percent;
    float maxDuration = 0.1f;
    private Vector2 pos;
    private Vector2 startVelocity;
    private Vector2 velocity;
    private Vector2 changeVelocity;
    private float maxVelocity = 500;
    private GameCamera gameCamera;

    public Player(){}

    public Player(GameCamera gameCamera) {
        this.gameCamera = gameCamera;

        float x = (Constants.GAME_WIDTH * 0.5f) - (size * 0.5f);
        float y = (Constants.GAME_HEIGHT * 0.5f) - (size * 0.5f);

        pos = new Vector2(x, y);
        startVelocity = new Vector2();
        velocity = new Vector2(200, 200);
        velocity.setAngleDeg(0);
        changeVelocity = new Vector2(200, 0);
    }

    public float getX(){
        return pos.x;
    }

    public float getY(){
        return pos.y;
    }

    public float getWidth(){
        return size;
    }

    public float getHeight(){
        return size;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float x = Constants.GAME_WIDTH * 0.5f;
        float y = Constants.GAME_HEIGHT * 0.5f;
        float angle = MathUtils.atan2(screenY - y, screenX - x) * MathUtils.radiansToDegrees;

        if(angle < 0) {
            angle = 360 + angle;
        }
        if(angle > 0) angle = 360 - angle;

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.W:
                velocity.setAngleDeg(90);
                break;
            case Input.Keys.A:
                velocity.setAngleDeg(180);
                break;
            case Input.Keys.S:
                velocity.setAngleDeg(270);
                break;
            case Input.Keys.D:
                velocity.setAngleDeg(0);
                break;
        }

        return false;
    }

    public void moveX(float amount){
        pos.x += amount;

        if(pos.x >= Constants.WORLD_WIDTH){
            gameCamera.setDistance(this);
            pos.x = pos.x - Constants.WORLD_WIDTH;
            gameCamera.wrapLeft(this);
        }
        else if(pos.x < -size){
            gameCamera.setDistance(this);
            pos.x = Constants.WORLD_WIDTH + pos.x;
            gameCamera.wrapRight(this);
        }
    }

    public void moveY(float amount){
        pos.y += amount;

        if(pos.y >= Constants.WORLD_HEIGHT){
            gameCamera.setDistance(this);
            pos.y = pos.y - Constants.WORLD_HEIGHT;
            gameCamera.wrapDown(this);
        }
        else if(pos.y < -size){
            gameCamera.setDistance(this);
            pos.y = Constants.WORLD_HEIGHT + pos.y;
            gameCamera.wrapUp(this);
        }
    }

    Vector2 thrustVector = new Vector2(200, 200);

    private void accelerate(float amount, float angle){
        change = true;
        timer = 0;
        percent = 0;

        thrustVector.set(velocity);
        thrustVector.setLength(200);
        thrustVector.setAngleDeg(135);

        startVelocity.set(velocity);
        changeVelocity.setLength(200);
        changeVelocity.setAngleDeg(angle);

        velocity.add(thrustVector);

        if(velocity.x > 200) velocity.x = 200;
        if(velocity.x < -200) velocity.x = -200;
        if(velocity.y > 200) velocity.y = 200;
        if(velocity.y < -200) velocity.y = -200;
    }

    public void update(float delta){
        moveX(velocity.x * delta);
        moveY(velocity.y * delta);
    }

    public void testUpdate(float delta){

        old(delta);


        if(pos.x > Constants.GAME_WIDTH){
            pos.x = pos.x - Constants.GAME_WIDTH;
        }
        else if(pos.x < 0){
            pos.x = Constants.GAME_WIDTH - pos.x;
        }

        if(pos.y > Constants.GAME_HEIGHT){
            pos.y = pos.y - Constants.GAME_HEIGHT;
        }
        else if(pos.y < 0){
            pos.y = Constants.GAME_HEIGHT - pos.y;
        }
    }

    private void newNew(float delta){



        moveX(velocity.x * delta);
        moveY(velocity.y * delta);
    }

    private void neww(float delta){
        if(change) {
            timer += delta;
            percent = timer / maxDuration;
            if(percent > 1) percent = 1;

            float x = MathUtils.lerp(0, thrustVector.x, Interpolation.pow5In.apply(percent));
            float y = MathUtils.lerp(0, thrustVector.y, Interpolation.pow5In.apply(percent));
            velocity.x = startVelocity.x + x;
            velocity.y = startVelocity.y + y;
            if(percent == 1) change = false;
        }

        moveX(velocity.x * delta);
        moveY(velocity.y * delta);
    }

    private void old(float delta){
        if(change){
            timer += delta;
            percent = timer / maxDuration;
            if(percent > 1) percent = 1;
            float x = MathUtils.lerp(0, changeVelocity.x, Interpolation.pow5In.apply(percent)) ;
            float y = MathUtils.lerp(0, changeVelocity.y, Interpolation.pow5In.apply(percent));
            velocity.x = MathUtils.lerp(startVelocity.x * 0.8f, startVelocity.x + x, Interpolation.pow5In.apply(percent));
            velocity.y = MathUtils.lerp(startVelocity.y * 0.8f, startVelocity.y + y, Interpolation.pow5In.apply(percent));
//            velocity.x = startVelocity.x + x;
//            velocity.y = startVelocity.y + y;
//            velocity.x = MathUtils.lerp(velocity.x, changeVelocity, lerp.apply(percent));

            if(percent == 1) change = false;
        }

        moveX(velocity.x * delta);
        moveY(velocity.y * delta);
    }

    public void draw(ShapeDrawer shapeDrawer){
        shapeDrawer.filledRectangle(pos.x, pos.y, size, size, Color.BLACK);
    }
}
