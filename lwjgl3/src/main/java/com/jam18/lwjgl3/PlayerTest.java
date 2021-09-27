package com.jam18.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jam18.Constants;
import com.jam18.Player;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlayerTest extends ApplicationAdapter {

    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new PlayerTest(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("PlayerTest");
        configuration.setWindowedMode(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }

    Player player;
    Batch batch;
    ShapeDrawer shapeDrawer;

    @Override
    public void create() {
        player = new Player();
        batch = new SpriteBatch();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.drawPixel(0, 0, 0xffffffff);
        shapeDrawer = new ShapeDrawer(batch, new TextureRegion(new Texture(pix)));
        pix.dispose();

        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update(Gdx.graphics.getDeltaTime());

        batch.begin();
        player.draw(batch, shapeDrawer);
        batch.end();
    }
}
