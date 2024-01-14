package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;
    private Animation<TextureRegion> characterUpAnimation;
    private Animation<TextureRegion> characterLeftAnimation;
    private Animation<TextureRegion> characterRightAnimation;


    private TextureRegion WallImageRegion;
    private TextureRegion EntryPointImageRegion;
    private TextureRegion ExitPointImageRegion;
    private TextureRegion TrapImageRegion;
    private TextureRegion EnemyImageRegion;
    private TextureRegion KeyImageRegion;




    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin


        this.loadBackground(); //Load game background


        this.loadCharacterAnimation(); // Load character animation

        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen
    }


    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {

        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        // libGDX internal Array instead of ArrayList because of performance
        Array<TextureRegion> walkFramesUp = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesDown = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesLeft = new Array<>(TextureRegion.class);
        Array<TextureRegion> walkFramesRight = new Array<>(TextureRegion.class);

        // Add frames to the respective animations
        for (int col = 0; col < animationFrames; col++) {
            walkFramesUp.add(
                    new TextureRegion(walkSheet, col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
            walkFramesDown.add(
                    new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
            walkFramesLeft.add(
                    new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
            walkFramesRight.add(
                    new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight));}

        characterUpAnimation = new Animation<>(0.1f, walkFramesUp);
        characterDownAnimation = new Animation<>(0.1f, walkFramesDown);
        characterLeftAnimation = new Animation<>(0.1f, walkFramesLeft);
        characterRightAnimation = new Animation<>(0.1f, walkFramesRight);
    }



    public void loadBackground() {
        Texture map = new Texture(Gdx.files.internal("basictiles.png"));
        Texture extra = new Texture(Gdx.files.internal("mobs.png"));



        int frameWidth = 16;
        int frameHeight = 15;

        // Create a TextureRegion for the first image
        WallImageRegion = new TextureRegion(map, 16, 0, frameWidth, frameHeight);
        EntryPointImageRegion = new TextureRegion(map, 0, 96, frameWidth, frameHeight);
        ExitPointImageRegion = new TextureRegion(map,32, 96, frameWidth, frameHeight);
        TrapImageRegion = new TextureRegion(map, 16, 96, frameWidth, frameHeight);
        EnemyImageRegion = new TextureRegion(extra, 96, 64, frameWidth, frameHeight);
        KeyImageRegion = new TextureRegion(extra, 0, 0, frameWidth, frameHeight);

    }

    public TextureRegion getWallImageRegion() {
        return WallImageRegion;
    }

    public TextureRegion getEntryPointImageRegion() {
        return EntryPointImageRegion;
    }

    public TextureRegion getExitPointImageRegion() {
        return ExitPointImageRegion;
    }

    public TextureRegion getTrapImageRegion() {
        return TrapImageRegion;
    }

    public TextureRegion getEnemyImageRegion() {
        return EnemyImageRegion;
    }

    public TextureRegion getKeyImageRegion() {
        return KeyImageRegion;
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin


        //MapImageRegion.dispose();
    }


    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }

}
