package com.jam18;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class EnemyManager {

    private float timer;
    private float spawnInterval = 1;
    private int maxEnemies = 5;
    private final int startSpawnAmount = 5;
    private Array<Enemy> enemies;
    private PlayScreen playScreen;

    public EnemyManager(PlayScreen playScreen) {
        this.playScreen = playScreen;
        enemies = new Array<>();

        for(int i = 0; i < startSpawnAmount; i++){
           spawn();
        }

    }

    private void spawn(){
        Enemy e = Pools.obtain(Enemy.class);
        float x = MathUtils.random(0, Constants.WORLD_WIDTH - e.width);
        float y = MathUtils.random(0, Constants.WORLD_HEIGHT - e.height);
        e.setup(x, y, playScreen.getPlayer());
        e.setPlayScreen(playScreen);
        enemies.add(e);
    }

    public void checkCollisions(BulletManager manager){
        for(int i = 0; i < enemies.size; i++){
            manager.checkCollisions(enemies.get(i));
        }
    }

    public void update(float delta){
        for(int i = enemies.size - 1; i >= 0; i--){
            Enemy e = enemies.get(i);
            e.update(delta);

            if (e.isFinished()) {
                enemies.removeIndex(i);
                Pools.free(e);
            }
        }

        if(enemies.size < maxEnemies){
            if((timer += delta) > spawnInterval){
                spawn();
                timer = 0;
            }
        }
    }

    public void draw(Batch batch, ShapeDrawer shapeDrawer){
        for(int i = 0 ; i < enemies.size; i++){
            enemies.get(i).draw(batch, shapeDrawer);
        }
    }
}
