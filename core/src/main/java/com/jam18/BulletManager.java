package com.jam18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class BulletManager {

    private Texture playerBulletTexture;
    private Texture enemyBulletTexture;
    private Array<Bullet> bullets;

    public BulletManager(){
        playerBulletTexture = new Texture(Gdx.files.internal("playerBullet.png"));
        enemyBulletTexture = new Texture(Gdx.files.internal("enemy1Bullet.png"));

        bullets = new Array<>(100);
    }

    public void checkCollisions(Character c){
        if(c.isPlayer){
            if(((Player)c).hasProtection) return;
            for(int i = 0; i < bullets.size; i++){
                Bullet b = bullets.get(i);
                if(b.shotByPlayer()) continue;

                if(c.collides(b)){
                    c.hit(b.getDamage());
                }
            }
        }
        else{
            for(int i = 0; i < bullets.size; i++){
                Bullet b = bullets.get(i);
                if(!b.shotByPlayer()) continue;

                if(c.collides(b)){
                    c.hit(b.getDamage());
                }
            }
        }
    }

    public void add(Bullet bullet){
        if(bullet == null) return;
        bullets.add(bullet);
    }

    public void update(float delta){
        for(int i = bullets.size - 1; i >= 0; i--){
            Bullet b = bullets.get(i);
            b.update(delta);

            if(b.isFinished()){
                bullets.removeIndex(i);
                Bullet.POOL.free(b);
            }
        }
    }

    public void drawDebug(ShapeDrawer shapeDrawer){
        for(int i = 0; i < bullets.size; i++){
            bullets.get(i).drawDebug(shapeDrawer);
        }
    }
}
