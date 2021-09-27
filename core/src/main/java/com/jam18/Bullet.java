package com.jam18;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Bullet extends Entity {

    public final static Pool<Bullet> POOL = new Pool<Bullet>(){
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    static {
        for(int i = 0; i < 100; i++){
            POOL.free(new Bullet());
        }
    }

    public static Bullet getBullet(){
        return POOL.obtain();
    }

    private int damage;
    private float maxDistance;
    private float speed;
    private boolean shotByPlayer;
    private Vector2 startPosition;
    private Vector2 velocity;

    public Bullet(){
        velocity = new Vector2();
        startPosition = new Vector2();
    }

    public void setup(Vector2 startPos, Vector2 dirVect, Weapon.WeaponData data){
        setSize(10, 10);
        startPosition.set(startPos);
        setPosition(startPos);
        speed = data.getBulletSpeed();
        damage = data.getBulletDamage();
        maxDistance = data.getMaxBulletTravel();
        velocity.set(dirVect);
        shotByPlayer = data.shotByPlayer();
    }

    public void update(float delta){
        float x = position.x + (velocity.x * delta);
        float y = position.y + (velocity.y * delta);
        setPosition(x, y);

        float distanceFromStart = Vector2.dst(startPosition.x, startPosition.y, x, y);

        if(distanceFromStart > maxDistance){
            setDead();
            setFinished();
        }
    }

    public boolean shotByPlayer() {
        return shotByPlayer;
    }

    public int getDamage(){
        return damage;
    }


    public void drawDebug(ShapeDrawer shapeDrawer){
        shapeDrawer.rectangle(hitBox, Color.RED);
    }

    @Override
    public void reset() {
        super.reset();
        damage = 0;
        speed = 0;
        velocity.set(0, 0);
        startPosition.set(0, 0);
        shotByPlayer = false;
        maxDistance = 0;
    }
}
