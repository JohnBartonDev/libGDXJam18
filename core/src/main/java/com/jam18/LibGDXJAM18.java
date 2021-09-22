package com.jam18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;
import space.earlygrey.shapedrawer.ShapeDrawer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class LibGDXJAM18 extends ApplicationAdapter {

    private boolean disposeCurrentScreen;
    private boolean switchScreens;
    private AssetManager assetManager;
    private Batch batch;
    private ShapeDrawer shapeDrawer;
    private Screen currentScreen;
    private Screen nextScreen;
    private Texture pixTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.getLogger().setLevel(Logger.DEBUG);
        currentScreen = new SplashScreen(this);
        currentScreen.show();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.drawPixel(0, 0, 0xffffffff);
        shapeDrawer = new ShapeDrawer(batch, new TextureRegion(pixTexture = new Texture(pix)));
        pix.dispose();
    }

    public Batch getBatch(){
        return batch;
    }

    public ShapeDrawer getShapeDrawer(){
        return shapeDrawer;
    }

    public AssetManager getAssetManager(){
        return assetManager;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        currentScreen.dispose();
        pixTexture.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        currentScreen.resize(width, height);
    }

    public void switchScreen(Screen screen, boolean disposeCurrentScreen) {
        if(screen == null) throw new IllegalArgumentException("Screen is null.");
        switchScreens = true;
        nextScreen = screen;
        this.disposeCurrentScreen = disposeCurrentScreen;
    }

    private void switchScreens() {
        switchScreens = false;

        if(currentScreen != null) {
            currentScreen.hide();
            if(disposeCurrentScreen) currentScreen.dispose();
        }

        currentScreen = nextScreen;
        nextScreen = null;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        currentScreen.show();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1,1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();
        currentScreen.update(delta);

        batch.setProjectionMatrix(currentScreen.getViewport().getCamera().combined);
        batch.begin();
        currentScreen.render(batch, shapeDrawer);
        batch.end();

        if(switchScreens) switchScreens();
    }
}