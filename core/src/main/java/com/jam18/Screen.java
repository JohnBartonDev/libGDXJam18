package com.jam18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vabrant.actionsystem.actions.ActionManager;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Screen {

    protected LibGDXJAM18 game;
    protected Viewport viewport;
    protected InputMultiplexer inputMultiplexer;
    protected ActionManager actionManager;

    public Screen(LibGDXJAM18 game){
        this(game, new OrthographicCamera());
    }

    public Screen(LibGDXJAM18 game, OrthographicCamera camera){
        this.game = game;
        viewport = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT, camera);
        inputMultiplexer = new InputMultiplexer(viewport);
        actionManager = new ActionManager(50);
    }

    public Viewport getViewport(){
        return viewport;
    }

    public LibGDXJAM18 getGame(){
        return game;
    }

    public ActionManager getActionManager(){
        return actionManager;
    }

    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    public void show(){
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

    public void update(float delta){
        actionManager.update(delta);
    }

    public void render(Batch batch, ShapeDrawer shapeDrawer){}
    public void dispose(){}
    public void pause(){}
    public void resume(){}


}
