package com.jam18;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.DelayAction;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class SplashScreen extends Screen implements ActionListener<DelayAction> {

    private BitmapFont font;

    public SplashScreen(LibGDXJAM18 game){
        super(game);
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().scale(3);

        actionManager.addAction(DelayAction.delay(2).addListener(this));
    }

    @Override
    public void render(Batch batch, ShapeDrawer shapeDrawer) {
        font.draw(batch, "Splash Screen", Constants.GAME_WIDTH * 0.5f, 400);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void actionStart(DelayAction delayAction) {

    }

    @Override
    public void actionEnd(DelayAction delayAction) {
        game.switchScreen(new MainMenuScreen(game), true);
    }

    @Override
    public void actionKill(DelayAction delayAction) {

    }

    @Override
    public void actionRestart(DelayAction delayAction) {

    }
}
