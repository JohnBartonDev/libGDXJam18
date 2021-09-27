package com.jam18;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Utils {

    public static float getAngle(Vector2 v1, Vector2 v2){
        float angle = MathUtils.atan2(v1.y - v2.y, v1.x - v2.x) * MathUtils.radiansToDegrees;
        if(angle < 0) {
            angle = 360 + angle;
        }
        return angle;
    }
}
