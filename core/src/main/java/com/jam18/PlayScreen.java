package com.jam18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionListener;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class PlayScreen extends Screen implements ActionListener {

    boolean pause;
    private final int playButtonSize = 50;
    private ZoneManager zoneManager;
    private GameCamera gameCamera;
    Player player;
    private FrameBuffer mirrorBuffer;

    public enum GameState {
        RUNNING,
        PAUSED,
        OVER
    }

    private TestButton pauseButton;
    private GameState state = GameState.RUNNING;

    public PlayScreen(LibGDXJAM18 game){
        super(game, new GameCamera());

        mirrorBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, false);

        gameCamera = (GameCamera) viewport.getCamera();
        player = new Player(gameCamera);

        int offset = 10;
        pauseButton = new TestButton(offset, Constants.GAME_HEIGHT - playButtonSize - offset, playButtonSize, playButtonSize, Color.BLUE);
        zoneManager = new ZoneManager();

        inputMultiplexer.addProcessor(player);
        inputMultiplexer.addProcessor(gameCamera);
        inputMultiplexer.addProcessor(new InputAdapter(){
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.SPACE){
                    pause = !pause;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameCamera.resize();
    }

    @Override
    public void update(float delta) {
        if(pause) return;
        super.update(delta);
        player.update(delta);
        gameCamera.update(delta,this);
        zoneManager.checkForZoneMirrors(gameCamera);
    }

    @Override
    public void render(Batch batch, ShapeDrawer shapeDrawer) {
        super.render(batch, shapeDrawer);

        zoneManager.debugZones(shapeDrawer);
        drawWorld(batch, shapeDrawer);

        Array<Zone> zonesToMirror = zoneManager.zonesToMirror;
        for(int i = 0; i < zonesToMirror.size; i++){
            Zone zone = zonesToMirror.get(i);
            Zone mirrorZone = zoneManager.zones[zone.zoneToRenderIndex];

            batch.end();

            //Save old camera pos
            float oldX = gameCamera.position.x;
            float oldY = gameCamera.position.y;
            float oldZoom = gameCamera.zoom;

            //Move camera to the zone the be mirrored
            gameCamera.zoom = 1f;
            gameCamera.position.set(gameCamera.originalCameraX + mirrorZone.getX(), gameCamera.originalCameraY + mirrorZone.getY(), 0);
            gameCamera.update();

            //Draw the world
            mirrorBuffer.begin();
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.setProjectionMatrix(gameCamera.combined);
            batch.begin();
            drawWorld(batch, shapeDrawer);

            //put the camera back to its old position
            gameCamera.position.set(gameCamera.originalCameraX + zone.getX(), gameCamera.originalCameraY + zone.getY(), 0);
            gameCamera.update();
            batch.setProjectionMatrix(gameCamera.combined);
            drawWorld(batch, shapeDrawer);

            batch.end();
            mirrorBuffer.end();

            gameCamera.zoom = oldZoom;
            gameCamera.position.set(oldX, oldY, 0);
            gameCamera.update();

            batch.setProjectionMatrix(gameCamera.combined);
            batch.enableBlending();
            batch.begin();

            Texture tex = mirrorBuffer.getColorBufferTexture();
            batch.draw(tex, zone.getX(), zone.getY(), 0, 0, tex.getWidth(), tex.getHeight(), 1f, 1f, 0, 0, 0, tex.getWidth(), tex.getHeight(), false, true);

        }

        if(!batch.isDrawing()){
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();
        }

        gameCamera.drawDebug(shapeDrawer);
    }

    private void drawWorld(Batch batch, ShapeDrawer shapeDrawer){
        shapeDrawer.filledRectangle(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), Color.PINK);
        shapeDrawer.filledRectangle(Constants.GAME_WIDTH, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), Color.RED);
        shapeDrawer.filledRectangle(0, Constants.GAME_HEIGHT, viewport.getWorldWidth(), viewport.getWorldHeight(), Color.ORANGE);
        shapeDrawer.filledRectangle(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, viewport.getWorldWidth(), viewport.getWorldHeight(), Color.PURPLE);

        player.draw(shapeDrawer);
    }

    @Override
    public void actionStart(Action action) {

    }

    @Override
    public void actionEnd(Action action) {
        game.switchScreen(new GameOverScreen(game), true);
    }

    @Override
    public void actionKill(Action action) {

    }

    @Override
    public void actionRestart(Action action) {

    }
}
