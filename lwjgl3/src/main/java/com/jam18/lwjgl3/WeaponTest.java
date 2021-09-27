package com.jam18.lwjgl3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.jam18.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class WeaponTest extends ApplicationAdapter {

    public static void main(String[] args) {
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new WeaponTest(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("WeaponTest");
        configuration.setWindowedMode(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }

    private Weapon.WeaponData weaponData1 = Weapon.WeaponData.create(false, 0.1f, 100, 400, 200, 5);
    private Weapon.WeaponData weaponData2 = Weapon.WeaponData.create(false, 0.04f, 20, 300, 500, 5);

    private Weapon weapon;
    private Batch batch;
    private ShapeDrawer shapeDrawer;
    private Vector2 centerPos;
    private Vector2 mousePos;
    private BulletManager bulletManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        bulletManager = new BulletManager();
        centerPos = new Vector2(Constants.GAME_WIDTH * 0.5f, Constants.GAME_HEIGHT * 0.5f);
        mousePos = new Vector2();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.drawPixel(0, 0, 0xffffffff);

        shapeDrawer = new ShapeDrawer(batch, new TextureRegion(new Texture(pix)));
        pix.dispose();


        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                switch(keycode){
                    case Input.Keys.SPACE:
                        if(weapon != null){
//                            mousePos.set(Gdx.input.getX(), Constants.GAME_HEIGHT - Gdx.input.getY());
//                            bulletManager.add(weapon.shoot(centerPos, mousePos));
                        }
                        else{
                            System.out.println("No weapon");
                        }
                        break;
                }

                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                System.out.println(button);
                if(weapon == null){
                    if(button == 0){
                        weapon = new Weapon(weaponData1);
                    }
                    else {
                        weapon = new Weapon(weaponData2);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(weapon != null){
                mousePos.set(Gdx.input.getX(), Constants.GAME_HEIGHT - Gdx.input.getY());
                bulletManager.add(weapon.shoot(centerPos, mousePos));
            }
        }

        float delta = Gdx.graphics.getDeltaTime();

        if(weapon != null){
            weapon.update(delta);
            if(weapon.isActive()) weapon = null;
        }
        bulletManager.update(delta);

        batch.begin();
        shapeDrawer.filledCircle(centerPos, 10, Color.BLACK);

        bulletManager.drawDebug(shapeDrawer);

        batch.end();
    }

}
