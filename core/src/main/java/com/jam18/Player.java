package com.jam18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vabrant.actionsystem.actions.MoveAction;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Player extends Character implements InputProcessor {

    private final int maxHelath = 500;

    public boolean hasProtection;
    private float protectionDuration = 1f;
    private float protectionTimer = 0;

    private final float acceleration = 0.7f;
    private float maxVelocity = 200;
    private static float size = 128;
    private static float offset = 39;
    private Vector2 velocity;
    private Vector2 crosshairPosition;
    private GameCamera gameCamera;
    private Weapon.WeaponData weaponData1 = Weapon.WeaponData.create(true, 0.3f, 40, 600, 500, -1);
    private Weapon.WeaponData weaponData2 = Weapon.WeaponData.create(true, 0.1f, 10, 400, 700, -1);
    private Texture crosshairTexture;
    private Texture playerTexture;

    public Player() {

    }

    public Player(GameCamera gameCamera) {
        super((Constants.GAME_WIDTH * 0.5f) - (size * 0.5f), (Constants.GAME_HEIGHT * 0.5f) - (size * 0.5f), size, size);

        health = maxHelath;

        isPlayer = true;
        hitBox.setSize(50, 50);

        crosshairTexture = new Texture(Gdx.files.internal("crosshair.png"));
        playerTexture = new Texture(Gdx.files.internal("player.png"));

        this.gameCamera = gameCamera;

        weapon.setData(weaponData1);
        weapon.setActive();

        velocity = new Vector2(100, 100);
        velocity.setAngleDeg(0);
        crosshairPosition = new Vector2();
    }

    public void revive(){
        setPosition((Constants.GAME_WIDTH * 0.5f) - (size * 0.5f), (Constants.GAME_HEIGHT * 0.5f) - (size * 0.5f));
        health = maxHelath;
        isDead = false;
        isFinished = false;
        hasProtection = true;
        protectionTimer = 0;
        velocity.set(100, 100);
        velocity.setAngleDeg(0);
    }

    private void shootInput() {
        if (Gdx.input.isButtonPressed(0)) {
            crosshairPosition = playScreen.getViewport().unproject(crosshairPosition.set(Gdx.input.getX(), Gdx.input.getY()));
            shoot(centerPosition, crosshairPosition);
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return false;
    }

    @Override
    public void setX(float x) {
        position.x = x;
        hitBox.x = x + offset;
        centerPosition.x = x + (width * 0.5f);
    }

    @Override
    public void setY(float y) {
        position.y = y;
        hitBox.y = y + offset;
        centerPosition.y = y + (height * 0.5f);
    }

    public void moveX(float amount) {
        setX(position.x + amount );

        if (position.x >= Constants.WORLD_WIDTH) {
            gameCamera.setDistance(this);
            position.x = position.x - Constants.WORLD_WIDTH;
            gameCamera.wrapLeft(this);
        } else if (position.x < -size) {
            gameCamera.setDistance(this);
            position.x = Constants.WORLD_WIDTH + position.x;
            gameCamera.wrapRight(this);
        }
    }

    public void moveY(float amount) {
        setY(position.y + amount);

        if (position.y >= Constants.WORLD_HEIGHT) {
            gameCamera.setDistance(this);
            position.y = position.y - Constants.WORLD_HEIGHT;
            gameCamera.wrapDown(this);
        } else if (position.y < -size) {
            gameCamera.setDistance(this);
            position.y = Constants.WORLD_HEIGHT + position.y;
            gameCamera.wrapUp(this);
        }
    }

    public void accelerate(float xAmount, float yAmount){
        velocity.x += xAmount;
        velocity.x = MathUtils.clamp(velocity.x, -maxVelocity, maxVelocity);
        velocity.y += yAmount;
        velocity.y = MathUtils.clamp(velocity.y, -maxVelocity, maxVelocity);
    }

    private void boost(){
        playScreen.viewport.unproject(crosshairPosition.set(Gdx.input.getX(), Gdx.input.getY()));
        float angle = Utils.getAngle(crosshairPosition, position);

        float x = MoveAction.offsetX(angle, maxVelocity);
        float y = MoveAction.offsetY(angle, maxVelocity);

        accelerate(x, y);
    }

    public void update(float delta) {
        super.update(delta);

        Input input = Gdx.input;
        if(input.isKeyPressed(Input.Keys.D)) {
            accelerate(acceleration, 0);
        }
        else if(input.isKeyPressed(Input.Keys.A)) {
            accelerate(-acceleration, 0);
        }

        if(input.isKeyPressed(Input.Keys.W)) {
            accelerate(0, acceleration);
        }
        else if(input.isKeyPressed(Input.Keys.S)) {
            accelerate(0, -acceleration);
        }

        if(input.isKeyPressed(Input.Keys.SPACE)){
            boost();
        }

        if(input.isKeyPressed(Input.Keys.NUM_1)){
            weapon.setData(weaponData1);
        }
        else if(input.isKeyPressed(Input.Keys.NUM_2)){
            weapon.setData(weaponData2);
        }

        if(hasProtection){
            if((protectionTimer += delta) > protectionDuration){
                hasProtection = false;
            }
        }

        moveX(velocity.x * delta);
        moveY(velocity.y * delta);
        shootInput();
    }

    Vector2 v = new Vector2();
    public void drawHealthBar(Viewport viewport, ShapeDrawer shapeDrawer){
        viewport.unproject(v.set((Constants.GAME_WIDTH - 300) * 0.5f, Constants.GAME_HEIGHT - 680));
        shapeDrawer.filledRectangle(v.x, v.y, 300, 20, Color.BLACK);
        shapeDrawer.filledRectangle(v.x + 3, v.y + 3, MathUtils.map(0, maxHelath, 0, 300 - 6, health), 20 - 6, Color.RED);
    }

    public void draw(Batch batch, ShapeDrawer shapeDrawer) {
        batch.draw(playerTexture, position.x, position.y);
//        shapeDrawer.filledRectangle(position.x, position.y, size, size, Color.BLACK);
//        shapeDrawer.filledRectangle(hitBox, Color.RED);
    }

    public void drawCrosshair(Batch batch, ShapeDrawer shapeDrawer){
//        shapeDrawer.filledCircle(crosshairPosition, 5);
//        batch.draw(crosshairTexture, Gdx.input.getX(), Constants.GAME_HEIGHT - Gdx.input.getY());
        playScreen.viewport.unproject(crosshairPosition.set(Gdx.input.getX(), Gdx.input.getY()));
        batch.draw(crosshairTexture, crosshairPosition.x, crosshairPosition.y);
    }

    @Override
    public boolean keyDown(int keycode) {
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
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
