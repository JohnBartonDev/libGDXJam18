package com.jam18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameCamera extends OrthographicCamera implements InputProcessor {

    private final int freeRoamAmount = 10;
    private float freeRoamX;
    private float freeRoamY;
    private float cameraXDistance;
    private float cameraYDistance;
    public float originalCameraX;
    public float originalCameraY;
    private final Rectangle viewport;

    public GameCamera(){
        viewport = new Rectangle(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
    }

    public void resize(){
        originalCameraX = position.x;
        originalCameraY = position.y;
    }

    public Rectangle getViewport(){
        return viewport;
    }

    public float getX(){
        return viewport.x;
    }

    public float getY(){
        return viewport.y;
    }

    public void setDistance(Player player){
        float x = player.getX() - (Constants.GAME_WIDTH - player.getWidth()) * 0.5f;
        float y = player.getY() - (Constants.GAME_HEIGHT - player.getHeight()) * 0.5f;
        cameraXDistance = Math.abs(viewport.x - x);
        cameraYDistance = Math.abs(viewport.y - y);
    }

    public void wrapLeft(Player player){
        float x = player.getX() - (Constants.GAME_WIDTH - player.getWidth()) * 0.5f;
        viewport.x = x - cameraXDistance;
        position.set(originalCameraX + viewport.x + freeRoamX, originalCameraY + viewport.y + freeRoamY, 0);
        update();
    }

    public void wrapRight(Player player) {
        float x = player.getX() - (Constants.GAME_WIDTH - player.getWidth()) * 0.5f;
        viewport.x = x + cameraXDistance;
        position.set(originalCameraX + viewport.x + freeRoamX, originalCameraY + viewport.y + freeRoamY, 0);
        update();
    }

    public void wrapUp(Player player) {
        float y = player.getY() - (Constants.GAME_HEIGHT - player.getHeight()) * 0.5f;
        viewport.y = y + cameraYDistance;
        position.set(originalCameraX + viewport.x + freeRoamX, originalCameraY + viewport.y + freeRoamY, 0);
        update();
    }

    public void wrapDown(Player player) {
        float y = player.getY() - (Constants.GAME_HEIGHT - player.getHeight()) * 0.5f;
        viewport.y = y - cameraYDistance;
        position.set(originalCameraX + viewport.x + freeRoamX, originalCameraY + viewport.y + freeRoamY, 0);
        update();
    }

    public void update(float delta, PlayScreen screen) {
        Player player = screen.player;

        float x = player.getX() - (Constants.GAME_WIDTH - player.getWidth()) * 0.5f;
        float y = player.getY() - (Constants.GAME_HEIGHT - player.getHeight()) * 0.5f;
        viewport.x = MathUtils.lerp(viewport.x, x, delta * 4f);
        viewport.y = MathUtils.lerp(viewport.y, y, delta * 4f);

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            freeRoamX -= freeRoamAmount;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            freeRoamX += freeRoamAmount;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            freeRoamY += freeRoamAmount;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            freeRoamY -= freeRoamAmount;
        }

        position.set(originalCameraX + viewport.x + freeRoamX, originalCameraY + viewport.y + freeRoamY, 0);
        update();
    }

    public void drawDebug(ShapeDrawer shapeDrawer){
        shapeDrawer.rectangle(viewport.x, viewport.y, viewport.width, viewport.height, Color.BLACK, 6f);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Input.Keys.R:
                zoom = 1f;
                freeRoamX = 0;
                freeRoamY = 0;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if(amountY > 0) {
            zoom += 0.2f;
        }
        else if(amountY < 0) {
            zoom -= 0.2f;
        }
        update();
        return false;
    }
}
