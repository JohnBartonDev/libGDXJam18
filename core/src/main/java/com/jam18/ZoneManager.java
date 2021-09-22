package com.jam18;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class ZoneManager {

    public static final int MAX_ROW = 2;
    public static final int MAX_COLUMN = 2;
    public static final int MAX_PLAYABLE_ZONES = MAX_ROW * MAX_COLUMN;
    public static final int MAX_ZONES = (MAX_ROW + 2) * (MAX_COLUMN + 2);

    final Array<Zone> playableZones;
    final Array<Zone> zonesToMirror;
    final Zone[] zones;
    public final Zone bottomLeftZone;
    public final Zone bottomRightZone;
    public final Zone topLeftZone;
    public final Zone topRightZone;

    public ZoneManager() {
        playableZones = new Array<>(MAX_PLAYABLE_ZONES);
        zones = new Zone[MAX_ZONES];
        zonesToMirror = new Array<>(zones.length);

        float x = 0;
        float y = -Constants.GAME_HEIGHT;
        float width = Constants.GAME_WIDTH;
        float height = Constants.GAME_HEIGHT;
        final int maxIndex = (MAX_ROW + 2) * (MAX_COLUMN + 2) - 1;
        int index = 0;

        for(int row = 0; row < MAX_PLAYABLE_ZONES; row++) {
            x = -Constants.GAME_WIDTH;

            for(int column = 0; column < MAX_PLAYABLE_ZONES; column++, index++) {
                int zoneToMirror = -1;

                //corner one
                if(column == 0 && row == 0) {
                    zoneToMirror = maxIndex - 1 - MAX_PLAYABLE_ZONES;
                }//corner two
                else if(column == MAX_PLAYABLE_ZONES - 1 && row == 0) {
                    zoneToMirror = MAX_PLAYABLE_ZONES * (MAX_PLAYABLE_ZONES - 1) + 1 - MAX_PLAYABLE_ZONES;
                }//corner three
                else if(column == 0 && row == (MAX_PLAYABLE_ZONES - 1)) {
                    zoneToMirror = (MAX_PLAYABLE_ZONES - 1) - 1 + MAX_PLAYABLE_ZONES;
                }//corner four
                else if(column == (MAX_PLAYABLE_ZONES - 1) && row == (MAX_PLAYABLE_ZONES - 1)) {
                    zoneToMirror = 1 + MAX_PLAYABLE_ZONES;
                }
                else if(row == 0) {
                    zoneToMirror = index + MAX_PLAYABLE_ZONES * (MAX_PLAYABLE_ZONES - 1) - MAX_PLAYABLE_ZONES;
                }
                else if(row == (MAX_PLAYABLE_ZONES - 1)) {
                    zoneToMirror = index - MAX_PLAYABLE_ZONES * (MAX_PLAYABLE_ZONES - 2);
                }
                else if(column == 0) {
                    zoneToMirror = index + (MAX_PLAYABLE_ZONES - 2);
                }
                else if(column == MAX_PLAYABLE_ZONES - 1) {
                    zoneToMirror = index - (MAX_PLAYABLE_ZONES - 2);
                }

                zones[index] = new Zone(x, y, width, height, row, column, zoneToMirror);
                x += width;
            }
            y += height;
        }

//        bottomLeftZone = zones[index(0, 0)];
//        bottomRightZone = zones[index(maxColumn - 1, 0)];
//        topLeftZone = zones[index(0, maxRow - 1)];
//        topRightZone = zones[index(maxColumn - 1, maxRow - 1)];


        bottomLeftZone = zones[index(0, 0)];
        bottomRightZone = zones[index(MAX_PLAYABLE_ZONES- 1, 0)];
        topLeftZone = zones[index(0, MAX_PLAYABLE_ZONES -1)];
        topRightZone = zones[index(MAX_PLAYABLE_ZONES - 1, MAX_PLAYABLE_ZONES -1)];
    }

    public void checkForZoneMirrors(GameCamera camera) {
        zonesToMirror.clear();

        final Rectangle cameraViewport = camera.getViewport();

        if(cameraViewport.x < 0) {
            if(cameraViewport.overlaps(bottomLeftZone.getBounds())) {
                zonesToMirror.add(bottomLeftZone);
                zonesToMirror.add(zones[index(0, 1)]);
                zonesToMirror.add(zones[index(1, 0)]);
            }
            else if(cameraViewport.overlaps(topLeftZone.getBounds())) {
                zonesToMirror.add(topLeftZone);
                zonesToMirror.add(zones[index(topLeftZone.column + 1, topLeftZone.row)]);
                zonesToMirror.add(zones[index(topLeftZone.column, topLeftZone.row - 1)]);
            }
            else {
                //Get the y index of the bottom left corner
                int bottomLeftCornerYZone = (int)(cameraViewport.y + Constants.GAME_HEIGHT) / Constants.GAME_HEIGHT;
                zonesToMirror.add(zones[index(0, bottomLeftCornerYZone)]);

                //Get the y index of the top left corner
                int topLeftCornerYZone = (int)((cameraViewport.y + cameraViewport.height) + Constants.GAME_HEIGHT - 1) / Constants.GAME_HEIGHT;
                if(topLeftCornerYZone > bottomLeftCornerYZone) {
                    zonesToMirror.add(zones[index(0, topLeftCornerYZone)]);
                }
            }
        }
        else if((cameraViewport.x + cameraViewport.width) > Constants.WORLD_WIDTH) {
            if(cameraViewport.overlaps(topRightZone.getBounds())) {
                zonesToMirror.add(topRightZone);
                zonesToMirror.add(zones[index(topRightZone.column - 1, topRightZone.row)]);
                zonesToMirror.add(zones[index(topRightZone.column, topRightZone.row - 1)]);
            }
            else if(cameraViewport.overlaps(bottomRightZone.getBounds())) {
                zonesToMirror.add(bottomRightZone);
                zonesToMirror.add(zones[index(bottomRightZone.column - 1, bottomRightZone.row)]);
                zonesToMirror.add(zones[index(bottomRightZone.column, bottomRightZone.row + 1)]);
            }
            else {
                int bottomRightCornerYZone = (int)(cameraViewport.y + Constants.GAME_HEIGHT) / Constants.GAME_HEIGHT;
                zonesToMirror.add(zones[index(MAX_PLAYABLE_ZONES - 1, bottomRightCornerYZone)]);

                int topRightCornerYZone = (int)((cameraViewport.y + cameraViewport.height) + Constants.GAME_HEIGHT - 1) / Constants.GAME_HEIGHT;
                if(topRightCornerYZone > bottomRightCornerYZone) {
                    zonesToMirror.add(zones[index(MAX_PLAYABLE_ZONES - 1, topRightCornerYZone)]);
                }
            }
        }
        else if(cameraViewport.y < 0) {
            int bottomLeftCornerXZone = (int)(cameraViewport.x + Constants.GAME_WIDTH) / Constants.GAME_WIDTH;
            int bottomLeftCornerYZone = (int)(cameraViewport.y + Constants.GAME_HEIGHT) / Constants.GAME_HEIGHT;
            int index = index(bottomLeftCornerXZone, bottomLeftCornerYZone);
            zonesToMirror.add(zones[index]);

            int bottomRightCornerXZone = (int)((cameraViewport.x + cameraViewport.width) + Constants.GAME_WIDTH - 1) / Constants.GAME_WIDTH;
            if(bottomRightCornerXZone > bottomLeftCornerYZone) {
                zonesToMirror.add(zones[index(bottomRightCornerXZone, bottomLeftCornerYZone)]);
            }
        }
        else if((cameraViewport.y + cameraViewport.height) > Constants.WORLD_HEIGHT) {
            int topLeftCornerXZone = (int)(cameraViewport.x + Constants.GAME_WIDTH) / Constants.GAME_WIDTH;
            int topLeftCornerYZone = (int)((cameraViewport.y + cameraViewport.height) + Constants.GAME_HEIGHT) / Constants.GAME_HEIGHT;
            zonesToMirror.add(zones[index(topLeftCornerXZone, topLeftCornerYZone)]);

            int topRightCornerXZone = (int)((cameraViewport.x + cameraViewport.width) + Constants.GAME_WIDTH - 1) / Constants.GAME_WIDTH;
            if(topRightCornerXZone > topLeftCornerXZone) {
                zonesToMirror.add(zones[index(topRightCornerXZone, topLeftCornerYZone)]);
            }
        }
    }

    public int index(int x,  int y) {
        if(x < 0 || x > MAX_PLAYABLE_ZONES - 1 || y < 0 || y > MAX_PLAYABLE_ZONES) return -1;
        return y * MAX_PLAYABLE_ZONES + x;
    }

    public void debugZones(ShapeDrawer shapeDrawer) {
        for(int i = 0, size = MAX_PLAYABLE_ZONES * MAX_PLAYABLE_ZONES; i < size; i++) {
            Zone zone = zones[i];
            shapeDrawer.rectangle(zone.getX(), zone.getY(), zone.getWidth(), zone.getHeight(), Color.GREEN, 6f);
        }

        for(int i = 0; i < zonesToMirror.size; i++) {
            Zone zone = zonesToMirror.get(i);
            shapeDrawer.rectangle(zone.getX(), zone.getY(), zone.getWidth(), zone.getHeight(), Color.RED, 6f);
        }
    }
}
