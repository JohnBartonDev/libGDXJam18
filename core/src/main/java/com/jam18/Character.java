package com.jam18;

import com.badlogic.gdx.math.Vector2;

public class Character extends Entity {

    protected boolean isPlayer;
    protected int health = 300;
    protected Weapon weapon;
    protected PlayScreen playScreen;

    public Character() {
        this(0, 0, 0, 0);
    }

    public Character(float x, float y, float width, float height){
        super(x, y, width, height);
        weapon = new Weapon();
    }

    public void setPlayScreen(PlayScreen playScreen){
        this.playScreen = playScreen;
    }

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }

    public void shoot(Vector2 startPos, Vector2 endPos){
        playScreen.getBulletManager().add(weapon.shoot(startPos, endPos));
    }

    public void shoot(Weapon weapon, Vector2 startPos, Vector2 endPos){
        playScreen.getBulletManager().add(weapon.shoot(startPos, endPos));
    }

    public void update(float delta){
       weapon.update(delta);
    }

    public void hit(int damage){
        health -= damage;

        if(health < 0){
            setDead();
            setFinished();
        }
    }

}
