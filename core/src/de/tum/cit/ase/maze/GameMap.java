package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GameMap {

    private static TextureRegion WallImageRegion;
    private static TextureRegion EntryPointImageRegion;
    private static TextureRegion ExitPointImageRegion;
    private static TextureRegion TrapImageRegion;
    private static TextureRegion EnemyImageRegion;
    private static TextureRegion KeyImageRegion;
    private static TextureRegion FloorImageRegion;

    //Life Image
    private static Animation<TextureRegion> lifeAnimation;
    private static float lifeStateTime;


    public static void loadBackground() {
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));
        Texture extra = new Texture(Gdx.files.internal("mobs.png"));




        int frameWidth = 16;
        int frameHeight = 15;

        // Create a TextureRegion for the first image
        WallImageRegion = new TextureRegion(map, 16, 0, frameWidth, frameHeight);
        EntryPointImageRegion = new TextureRegion(map,32, 96, frameWidth, frameHeight);
        ExitPointImageRegion = new TextureRegion(map, 0, 96, frameWidth, frameHeight);
        TrapImageRegion = new TextureRegion(map, 16, 96, frameWidth, frameHeight);
        EnemyImageRegion = new TextureRegion(extra, 96, 64, frameWidth, frameHeight);
        KeyImageRegion = new TextureRegion(extra, 0, 0, frameWidth, frameHeight);
        FloorImageRegion = new TextureRegion(map, 1000, 1000, frameWidth, frameHeight);
    }

    public static void lifeImageAnimation() {
        Texture life = new Texture(Gdx.files.internal("objects.png"));
        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 4;

        Array<TextureRegion> lifeFrames = new Array<>(TextureRegion.class);
        for (int col = 0; col < animationFrames; col++) {
            lifeFrames.add(new TextureRegion(life, col * frameWidth,3 * frameHeight, frameWidth, frameHeight));
        }
        lifeAnimation = new Animation<>(0.2f, lifeFrames);
    }

    public static void renderLives(SpriteBatch spriteBatch, float delta, float viewportWidth, float viewportHeight, int characterLives) {
        float spacing = 20;
        float lifeWidth = 40;
        float lifeHeight = 40;

        float lifeX = viewportWidth + 20 + (characterLives * spacing) / 2;
        float lifeY = viewportHeight + 100;

        lifeStateTime += Gdx.graphics.getDeltaTime(); // Update animation time

        for (int i = 0; i < characterLives; i++) {
            TextureRegion currentLifeFrame = GameMap.getLifeAnimation().getKeyFrame(lifeStateTime, true);
            spriteBatch.draw(currentLifeFrame, lifeX + i * spacing, lifeY, lifeWidth, lifeHeight);
        }
    }


    public static TextureRegion getFloorImageRegion() {
        return FloorImageRegion;
    }

    public static TextureRegion getWallImageRegion() {
        return WallImageRegion;
    }

    public static TextureRegion getEntryPointImageRegion() {
        return EntryPointImageRegion;
    }

    public static TextureRegion getExitPointImageRegion() {
        return ExitPointImageRegion;
    }

    public static TextureRegion getTrapImageRegion() {
        return TrapImageRegion;
    }

    public static TextureRegion getEnemyImageRegion() {
        return EnemyImageRegion;
    }

    public static TextureRegion getKeyImageRegion() {
        return KeyImageRegion;
    }

    public static Animation<TextureRegion> getLifeAnimation() {
        return lifeAnimation;
    }

    public static float getLifeStateTime() {
        return lifeStateTime;
    }

}