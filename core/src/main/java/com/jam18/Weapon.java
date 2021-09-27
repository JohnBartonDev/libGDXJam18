package com.jam18;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Weapon implements Pool.Poolable {

    public static class WeaponData {

        private float shootInterval;
        private int bulletDamage;
        private float maxBulletTravel;
        public int bulletSpeed;
        private float weaponActiveDuration;
        private boolean playerWeapon;

        public static WeaponData create(boolean playerWeapon, float shootInterval, int bulletDamage, float maxBulletTravel, int bulletSpeed, float weaponActiveDuration) {
            return new WeaponData(playerWeapon, shootInterval, bulletDamage, maxBulletTravel, bulletSpeed, weaponActiveDuration);
        }

        public WeaponData(boolean playerWeapon, float shootInterval, int bulletDamage, float maxBulletTravel, int bulletSpeed, float weaponActiveDuration) {
            set(playerWeapon, shootInterval, bulletDamage, maxBulletTravel, bulletSpeed, weaponActiveDuration);
        }

        public void set(boolean playerWeapon, float shootInterval, int bulletDamage, float maxBulletTravel, int bulletSpeed, float weaponActiveDuration){
            this.playerWeapon = playerWeapon;
            this.shootInterval = shootInterval;
            this.bulletDamage = bulletDamage;
            this.maxBulletTravel = maxBulletTravel;
            this.bulletSpeed = bulletSpeed;
            this.weaponActiveDuration = weaponActiveDuration;
        }

        public boolean shotByPlayer(){
            return playerWeapon;
        }

        public int getBulletDamage(){
            return bulletDamage;
        }

        public float getMaxBulletTravel(){
            return maxBulletTravel;
        }

        public float getBulletSpeed(){
            return bulletSpeed;
        }

    }

    private boolean isActive;
    private boolean canShoot = true;
    private float activeDurationTimer;
    private float shootIntervalTimer;
    private WeaponData data;
    private static Vector2 directionVector = new Vector2(1, 1);

    public Weapon() {}

    public Weapon(WeaponData data){
        this.data = data;
    }

    public void setData(WeaponData data){
        this.data = data;
    }

    public WeaponData getData(){
        return data;
    }

    public void setActive(){
        isActive = true;
    }

    public boolean isActive(){
        return isActive;
    }

    public Bullet shoot(Vector2 startPos, Vector2 endPos) {
        if(!isActive || !canShoot) return null;

        Bullet bullet = Bullet.getBullet();

        float angle = Utils.getAngle(endPos, startPos);

        directionVector.setAngleDeg(angle);
        directionVector.setLength(data.getBulletSpeed());

        bullet.setup(startPos, directionVector, data);
        canShoot = false;
        return bullet;
    }

    public void update(float delta) {
        if(!isActive) return;

        if(data.weaponActiveDuration > -1){
            if((activeDurationTimer += delta) > data.weaponActiveDuration){
                isActive = false;
                return;
            }
        }

        if (!canShoot) {
            if ((shootIntervalTimer += delta) > data.shootInterval) {
                shootIntervalTimer = 0;
                canShoot = true;
            }
        }
    }

    @Override
    public void reset() {
        isActive = false;
        data = null;
        canShoot = true;
        activeDurationTimer = 0;
        shootIntervalTimer = 0;
        directionVector.set(1, 1);
    }
}
