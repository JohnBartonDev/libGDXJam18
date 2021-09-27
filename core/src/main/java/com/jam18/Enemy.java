package com.jam18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.vabrant.actionsystem.actions.MoveAction;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Enemy extends Character {

    private float acceleration = 0.7f;
    private float maxVelocity = 200;
    private static float size = 128;
    private static float offset = 39;
    private Vector2 velocity;
    private Weapon.WeaponData defaultWeaponData = Weapon.WeaponData.create(false, 0.2f, 30, 500, 500, -1);
    private Texture enemyTexture;
    private Player player;
    private float distanceToPlayer;

    public Enemy(){
        setSize(128, 128);
        hitBox.setSize(50, 50);
        enemyTexture = new Texture(Gdx.files.internal("enemy1.png"));

        weapon.setData(defaultWeaponData);
        weapon.setActive();

        velocity = new Vector2(100, 100);
        velocity.setAngleDeg(0);
    }

    public void setup(float x, float y, Player player){
        this.player = player;
        setPosition(x, y);

        acceleration = MathUtils.random(0.5f, 30f);
        maxVelocity = MathUtils.random(100, 300);
        velocity.set(maxVelocity, maxVelocity);
        distanceToPlayer = MathUtils.random(50, 300);

        defaultWeaponData.set(false, MathUtils.random(0.1f, 1f), 10, distanceToPlayer, MathUtils.random(100, 300), -1);
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
            position.x = position.x - Constants.WORLD_WIDTH;
        } else if (position.x < -size) {
            position.x = Constants.WORLD_WIDTH + position.x;
        }
    }

    public void moveY(float amount) {
        setY(position.y + amount);

        if (position.y >= Constants.WORLD_HEIGHT) {
            position.y = position.y - Constants.WORLD_HEIGHT;
        } else if (position.y < -size) {
            position.y = Constants.WORLD_HEIGHT + position.y;
        }
    }

    public void accelerate(float xAmount, float yAmount){
        velocity.x += xAmount;
        velocity.x = MathUtils.clamp(velocity.x, -maxVelocity, maxVelocity);
        velocity.y += yAmount;
        velocity.y = MathUtils.clamp(velocity.y, -maxVelocity, maxVelocity);
    }

    public void update(float delta) {
        super.update(delta);

        float distance = Vector2.dst(position.x, position.y, player.position.x, player.position.y);

        if(distance > distanceToPlayer){
            float angle = Utils.getAngle(player.position, position);
            float x = MoveAction.offsetX(angle, acceleration);
            float y = MoveAction.offsetY(angle, acceleration);
            accelerate(x, y);
        }
        else{
            shoot(centerPosition, player.position);
        }

        moveX(velocity.x * delta);
        moveY(velocity.y * delta);
    }

    public void draw(Batch batch, ShapeDrawer shapeDrawer) {
        batch.draw(enemyTexture, position.x, position.y);

//        float old = shapeDrawer.setColor(Color.RED);
//        shapeDrawer.circle(centerPosition.x, centerPosition.y, distanceToPlayer, 3);
//        shapeDrawer.setColor(old);
//        shapeDrawer.filledRectangle(position.x, position.y, size, size, Color.BLACK);
//        shapeDrawer.filledRectangle(hitBox, Color.RED);
    }

}
