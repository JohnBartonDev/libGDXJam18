package com.jam18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class StarField {

    private class Star {
        float size;
        float rotation;
        boolean rotate;
        float timer;
        Vector2 position;
        Color color;

        Star(){
            position = new Vector2();
            color = new Color();
        }

        void update(float delta){
            if(!rotate) return;

            rotation += (100 * delta);
            rotation %= 360;
        }
    }


    private Texture starTexture;
    private final int maxStars = 1000;
    private Array<Star> stars;

    public StarField(){
        starTexture = new Texture(Gdx.files.internal("star.png"));

        stars = new Array<>(maxStars);

        for(int i = 0; i < maxStars; i++){
            Star s = new Star();
            float min = 1f;
//            s.size = MathUtils.randomTriangular(min, 10f, min);
            s.rotate = MathUtils.randomBoolean();
            s.size = MathUtils.random(min, 10f);
            s.position.x = MathUtils.random(0, Constants.WORLD_WIDTH - s.size);
            s.position.y = MathUtils.random(0, Constants.WORLD_HEIGHT - s.size);

            float c = MathUtils.random(0, 0.5f);
            s.color.set(c, c, c, 1f);
            stars.add(s);
        }
    }

    public void update(float delta){
        for(int i = 0; i < stars.size; i++){
            Star s = stars.get(i);
            s.update(delta);
        }
    }

    public void draw(Batch batch, ShapeDrawer shapeDrawer){
        for(int i = 0; i < stars.size; i++){
            Star s = stars.get(i);

            batch.setColor(s.color);
            batch.draw(starTexture, s.position.x, s.position.y, s.size * 0.5f, s.size * 0.5f, s.size, s.size, 1, 1, s.rotation, 0, 0, starTexture.getWidth(), starTexture.getHeight(), false, false);
            batch.setColor(Color.WHITE);
//            shapeDrawer.filledCircle(s.position, s.size, Color.BLACK);
        }
    }
}
