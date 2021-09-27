package com.jam18.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jam18.Constants;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class MoveTest extends ApplicationAdapter {

    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MoveTest(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("MoveTest");
        configuration.setWindowedMode(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }

    private float angle = 40;
    private Batch batch;
    private ShapeDrawer shapeDrawer;
    private Vector2 pos1;
    private Vector2 dir1;
    private Vector2 pos2;

    @Override
    public void create() {
        batch = new SpriteBatch();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.drawPixel(0, 0, 0xfffffff);

        shapeDrawer = new ShapeDrawer(batch, new TextureRegion(new Texture(pix)));
        pix.dispose();

        pos1 = new Vector2(350, 300);
        dir1 = new Vector2(0, 1000);
        dir1.setLength(100);
        dir1.setAngleDeg(angle);

        pos2 = new Vector2(400, 300);

        Vector2 v = new Vector2(10, 10);
//        float oldLen = v.len2();
//        float newLen = 50 * 50;
//        float xVal = v.x *= (float)Math.sqrt(newLen / oldLen);
//        float yVal = v.y *= (float)Math.sqrt(newLen / oldLen);
//        v.x = xVal;
//        v.y = yVal;
        v.setLength(50);
        v.setAngleDeg(angle);

        System.out.println("x: " + v.x);
        System.out.println("y: " + v.y);
        System.out.println();

        v = new Vector2(2, 1857300f);
//        oldLen = v.len2();
//        newLen = 50 * 50;
//        xVal = v.x *= (float)Math.sqrt(newLen / oldLen);
//        yVal = v.y *= (float)Math.sqrt(newLen / oldLen);
        v.setLength(50);
        v.setAngleDeg(angle);

        System.out.println("x: " + v.x);
        System.out.println("y: " + v.y);
        System.out.println();
    }

    public float offsetX(float angle, float amount) {
        return amount * MathUtils.cosDeg(angle);
    }

    public float offsetY(float angle, float amount) {
        return amount * MathUtils.sinDeg(angle);
    }

    float timer = 0;
    float newAngle = 270;
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        timer += delta;
        float p = timer / 2f;
        float angle = MathUtils.lerp(this.angle, newAngle, Interpolation.sineIn.apply(p));

        if(timer > 2f){
            angle = newAngle;
        }

        dir1.setAngleDeg(angle);

        pos1.x += dir1.x * delta;
        pos1.y += dir1.y * delta;

        pos2.x += offsetX(angle, 100 * delta);
        pos2.y += offsetY(angle, 100 * delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        shapeDrawer.filledCircle(pos1, 10, Color.RED);
        shapeDrawer.filledCircle(pos2, 10, Color.BLUE);
        batch.end();
    }
}
