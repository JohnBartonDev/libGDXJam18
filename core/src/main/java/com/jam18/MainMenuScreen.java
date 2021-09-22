package com.jam18;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class MainMenuScreen extends Screen {

    private TestButton playButton;

    public MainMenuScreen(LibGDXJAM18 game){
        super(game);

        playButton = new TestButton((Constants.GAME_WIDTH - 100) * 0.5f, 200, 100, 100, Color.BLUE);
        playButton.addListener(new Button.ButtonListener(){
            @Override
            public void touchUpSelected() {
                playButton.setActive(false);
                game.switchScreen(new PlayScreen(game), true);
            }
        });

        inputMultiplexer.addProcessor(playButton);
    }

    @Override
    public void show() {
        super.show();
        playButton.setActive(true);
    }

    @Override
    public void render(Batch batch, ShapeDrawer shapeDrawer) {
        super.render(batch, shapeDrawer);
        playButton.draw(shapeDrawer);
    }
}
