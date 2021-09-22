package com.jam18;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameOverScreen extends Screen {

    private final int buttonSize = 150;

    private TestButton replayButton;
    private TestButton mainMenuButton;

    public GameOverScreen(LibGDXJAM18 game){
        super(game);

        final float y = 200;
        final float buttonsWidth = buttonSize * 2;
        final float buttonOffset = 30;
        final float startX = (Constants.GAME_WIDTH - buttonsWidth) * 0.5f;

        replayButton = new TestButton(startX, y, buttonSize, buttonSize, Color.BLUE);
        replayButton.addListener(new Button.ButtonListener(){
            @Override
            public void touchUpSelected() {
                replayButton.setActive(false);
                mainMenuButton.setActive(false);
                game.switchScreen(new PlayScreen(game), true);
            }
        });

        mainMenuButton = new TestButton(startX + buttonSize + buttonOffset, y, buttonSize, buttonSize, Color.PURPLE);
        mainMenuButton.addListener(new Button.ButtonListener(){
            @Override
            public void touchUpSelected() {
                replayButton.setActive(false);
                mainMenuButton.setActive(false);
                game.switchScreen(new MainMenuScreen(game), true);
            }
        });

        inputMultiplexer.addProcessor(replayButton);
        inputMultiplexer.addProcessor(mainMenuButton);
    }

    @Override
    public void show() {
        super.show();
        replayButton.setActive(true);
        mainMenuButton.setActive(true);
    }

    @Override
    public void render(Batch batch, ShapeDrawer shapeDrawer) {
        super.render(batch, shapeDrawer);
        replayButton.draw(shapeDrawer);
        mainMenuButton.draw(shapeDrawer);
    }
}
