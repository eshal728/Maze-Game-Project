 package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;


 public class Character extends GameObject {

     private  boolean hasKey;
    private int lives;
    private final Animation<TextureRegion> animation;
    private static float stateTime;



    // Character animation downwards
    private static Animation<TextureRegion> characterDownAnimation;
    private static Animation<TextureRegion> characterUpAnimation;
    private static Animation<TextureRegion> characterLeftAnimation;
    private static Animation<TextureRegion> characterRightAnimation;


    private TextureRegion characterRegion;

    private boolean isTextVisible;
    private boolean shouldMove;
    private final OrthographicCamera camera;
    private final MazeRunnerGame game;
    private final BitmapFont font;
    private final GameScreen gameScreen;
    private boolean isKeyPressed;

     private long lastCollisionTime;
     private static final long COLLISION_COOLDOWN = 3000; // for 3 seconds, the character doesn't lose its lives
     private boolean doneCooling;


     private static Animation<TextureRegion> currentAnimation;
     private static TextureRegion collisionImageRegion;


    //Constructor
    public Character(GameScreen gameScreen, MazeRunnerGame game, float x, float y, boolean hasKey, int lives, Animation<TextureRegion> animation) {
        super(x, y);
        this.game = game; // Store the game instance
        this.hasKey = hasKey;
        this.lives = lives;
        this.animation = animation;
        stateTime = 0f;
        this.gameScreen = gameScreen;

        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        font = game.getSkin().getFont("font");

        currentAnimation = getCharacterDownAnimation();

    }

     public static void resetAnimation() {
         // Reset the animation to the default animation
         currentAnimation = getCharacterDownAnimation();

         // Reset the state time to start the animation from the beginning
         stateTime = 0f;
     }


     /**
     * Loads the character animation from the character.png file.
     */
    public static void loadCharacterAnimation() {

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

    public void move() {
            stateTime += Gdx.graphics.getDeltaTime();


        /**
         * boolean for the text showing
         */
        if(isTextVisible) {
        font.draw(game.getSpriteBatch(), "Press SPACE to Pause", gameScreen.getTextX(), gameScreen.getTextY());
    }

    /**
     * All key controls to move the character
     */
        if (Gdx.input.isKeyPressed(Input.Keys.UP)
            || Gdx.input.isKeyPressed(Input.Keys.W)
            || Gdx.input.isKeyPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyPressed(Input.Keys.S)
            || Gdx.input.isKeyPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyPressed(Input.Keys.A)
            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyPressed(Input.Keys.D)) {

            gameScreen.setTextX(camera.position.x);
            gameScreen.setTextY(camera.position.y);
        isTextVisible = false;
    } else {
        isTextVisible = true;
    }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            currentAnimation = getCharacterUpAnimation();
            characterRegion = getCharacterUpAnimation().getKeyFrame(gameScreen.getSinusInput(), true);

            float newY = MathUtils.clamp(getY() + 5, 30, GameScreen.getMaxY()*2 + 50);

            if (isCollisionWithWallsUp()) {
                setY((int) newY);
            }
            else{

                setY((int) newY);
            }


            shouldMove = true;
            isKeyPressed = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            characterRegion = getCharacterDownAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
            currentAnimation = getCharacterDownAnimation();

            float newY = MathUtils.clamp(getY() - 5, 30, GameScreen.getMaxY()*2 + 50);

            if (isCollisionWithWallsDown()) {
                setY((int) newY);
            }
            else{

                setY((int) newY);
            }
            shouldMove = true;
            isKeyPressed = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            characterRegion = getCharacterLeftAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
            currentAnimation = getCharacterLeftAnimation();

            float newX = MathUtils.clamp(getX() - 5, 40, GameScreen.getMaxX()*2 + 50);

            if (isCollisionWithWallsLeft()) {
                setX((int) newX);
            }
            else{

                setX((int) newX);
            }

            shouldMove = true;
            isKeyPressed = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            characterRegion = getCharacterRightAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
            currentAnimation = getCharacterRightAnimation();

            float newX = MathUtils.clamp(getX() + 5, 40, GameScreen.getMaxX()*2 + 50);

            if (isCollisionWithWallsRight()) {
                setX((int) newX);
            }
            else{

                setX((int) newX);
            }

            shouldMove = true;
            isKeyPressed = true;
        }
        else {
            isKeyPressed = false;
        }

    }

     public int findMaxWallX() {
         int maxWallX = 0; // Initialize to the smallest possible integer value

         for (int i = 0; i < GameScreen.getMazeData().length; i++) {
             int wallX = GameScreen.getMazeData()[i][0];

             // Update maxWallX if the current wallX is greater
             if (wallX > maxWallX) {
                 maxWallX = wallX;
             }
         }

         return maxWallX;
     }

     public int findMaxWallY() {
         int maxWallY = 0; // Initialize to the smallest possible integer value

         for (int i = 0; i < GameScreen.getMazeData().length; i++) {
             int wallY = GameScreen.getMazeData()[i][1];

             // Update maxWallY if the current wallY is greater
             if (wallY > maxWallY) {
                 maxWallY = wallY;
             }
         }

         return maxWallY;
     }

     private boolean isCollisionWithWallsLeft() {
         for (int i = 0; i < GameScreen.getMazeData().length; i++) {
             int wallX = GameScreen.getMazeData()[i][0];
             int wallY = GameScreen.getMazeData()[i][1];
             int variable = GameScreen.getMazeData()[i][2];

             if (getX() > wallX && getX() - 5 <= wallX + 50 && getY() + 62 > wallY && getY() < wallY + 50) {
                 return false; // Collision with this wall, return false
             }
         }
         return true; // No collision with any walls
     }

     private boolean isCollisionWithWallsUp() {
         for (int i = 0; i < GameScreen.getMazeData().length; i++) {
             int wallX = GameScreen.getMazeData()[i][0];
             int wallY = GameScreen.getMazeData()[i][1];
             int variable = GameScreen.getMazeData()[i][2];
             float characterX = getX();
             float characterY = getY();
             float characterWidth = 36;
             float characterHeight = 62;

             if(variable == 0) {
                 if (getX() > wallX && getX() - 5 <= wallX + 50 && getY() + 62 > wallY && getY() < wallY + 50) {
                     return false; // Collision with this wall, return false
                 }
             }
         }
         return true; // No collision with any walls
     }

     private boolean isCollisionWithWallsDown() {
         for (int i = 0; i < GameScreen.getMazeData().length; i++) {
             int wallX = GameScreen.getMazeData()[i][0];
             int wallY = GameScreen.getMazeData()[i][1];
             int variable = GameScreen.getMazeData()[i][2];

             if (getY() - 5 <= wallY + 50 && getY() + 62 >= wallY && getX() + 36 > wallX && getX() < wallX + 50) {
                 return false; // Collision with this wall, return false
             }
         }
         return true; // No collision with any walls
     }

     private boolean isCollisionWithWallsRight() {
         for (int i = 0; i < GameScreen.getMazeData().length; i++) {
             int wallX = GameScreen.getMazeData()[i][0];
             int wallY = GameScreen.getMazeData()[i][1];
             int variable = GameScreen.getMazeData()[i][2];

             if (getX() + 5 <= wallX + 50 && getX() + 36 >= wallX && getY() + 62 > wallY && getY() < wallY + 50) {
                 return false; // Collision with this wall, return false
             }
         }
         return true; // No collision with any walls
     }





     private boolean isCollisionWithWalls(float newX, float newY) {
         for (int i = 0; i < GameScreen.getMazeData().length; i++) {
             int wallX = GameScreen.getMazeData()[i][0];
             int wallY = GameScreen.getMazeData()[i][1];
             int variable = GameScreen.getMazeData()[i][2];

             if (variable == 0 && newX < wallX + 50 && newX + 36 > wallX && newY < wallY + 150 && newY + 62 > wallY) {
                 return true; // Collision with wall, return true
             }
         }
         return false; // No collision with walls
     }


     public boolean collidesWithEnemy(float enemyX1, float enemyY1) {
         //retrieves the current time in milliseconds.
         long currentTime = System.currentTimeMillis();

         // Check if enough time has passed since the last collision
         if (currentTime - lastCollisionTime >= COLLISION_COOLDOWN) {
             float characterX = getX();
             float characterY = getY();
             float characterWidth = 36;
             float characterHeight = 62;

             float keyWidth = 50;
             float keyHeight = 50;

             // Check for collision between character and enemy
             if (characterX < enemyX1 + keyWidth &&
                     characterX + characterWidth > enemyX1 &&
                     characterY < enemyY1 + keyHeight &&
                     characterY + characterHeight > enemyY1) {

                 // Set the last collision time
                 lastCollisionTime = currentTime;
                 loadCollisionImage();
                 doneCooling = true;
                 return true;
             }
         }
         return false;

     }

     public boolean collidesWithKey(float keyX, float keyY) {
         float characterX = getX();
         float characterY = getY();
         float characterWidth = 36;
         float characterHeight = 62;

         float keyWidth = 50;
         float keyHeight = 50;

         return characterX < keyX + keyWidth &&
                 characterX + characterWidth > keyX &&
                 characterY < keyY + keyHeight &&
                 characterY + characterHeight > keyY;
     }
     public boolean collidesWithTrap(float trapX, float trapY) {
         //retrieves the current time in milliseconds.
         long currentTime = System.currentTimeMillis();

         // Check if enough time has passed since the last collision
         if (currentTime - lastCollisionTime >= COLLISION_COOLDOWN) {
             float characterX = getX();
             float characterY = getY();
             float characterWidth = 36;
             float characterHeight = 62;

             float trapWidth = 50;
             float trapHeight = 50;

             // Check for collision between character and enemy
             if (characterX < trapX + trapWidth &&
                     characterX + characterWidth > trapX &&
                     characterY + 25< trapY + trapHeight &&
                     characterY + characterHeight > trapY) {

                 // Set the last collision time
                 lastCollisionTime = currentTime;
                 loadCollisionImage();
                 doneCooling = true;
                 return true;
             }
         }
         return false;
     }

     private void loadCollisionImage() {
         Texture collisionMark = new Texture(Gdx.files.internal("objects.png"));

         int frameWidth = 16;
         int frameHeight = 16;

         collisionImageRegion = new TextureRegion(collisionMark,2 * frameWidth,8 * frameHeight,frameWidth,frameHeight);
         // Set the time when the cooldown image is set
         lastCollisionTime = System.currentTimeMillis();
     }

     public void renderCollision(SpriteBatch batch, float viewportWidth, float viewportHeight) {
         float collisionMarkWidth = 40;
         float collisionMarkHeight = 40;

         float collisionImageX = viewportWidth + 20;
         float collisionImageY = viewportHeight + 100;

         if (collisionImageRegion != null) {
             batch.draw(collisionImageRegion,collisionImageX,collisionImageY,collisionMarkWidth,collisionMarkHeight);
         }

         if (System.currentTimeMillis() - lastCollisionTime >= COLLISION_COOLDOWN) {
             // Reset the cooldown image
             collisionImageRegion = null;
         }
     }
    public static Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public static Animation<TextureRegion> getCharacterUpAnimation() {
        return characterUpAnimation;
    }

    public static Animation<TextureRegion> getCharacterLeftAnimation() {
        return characterLeftAnimation;
    }

    public static Animation<TextureRegion> getCharacterRightAnimation() {
        return characterRightAnimation;
    }


    public TextureRegion getCharacterRegion() {
        return characterRegion;
    }

     public static Animation<TextureRegion> getCurrentAnimation() {
         return currentAnimation;
     }

     public int getLives() {
         return lives;
     }

     public boolean isHasKey() {
         return hasKey;
     }

     public boolean isShouldMove() {
        return shouldMove;
    }

     public boolean isKeyPressed() {
         return isKeyPressed;
     }

     public void setTextVisible(boolean textVisible) {
        isTextVisible = textVisible;
    }

    public void setCharacterRegion(TextureRegion characterRegion) {
        this.characterRegion = characterRegion;
    }

     public static void setCurrentAnimation(Animation<TextureRegion> currentAnimation) {
         Character.currentAnimation = currentAnimation;
     }

     public void setHasKey(boolean hasKey) {
         this.hasKey = hasKey;
     }

     public void setLives(int lives) {
         this.lives = lives;
     }

     public boolean isDoneCooling() {
         return doneCooling;
     }

     public void render(SpriteBatch batch) {
         TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
         float width = 4*currentFrame.getRegionWidth();
         float height = 4* currentFrame.getRegionHeight();

         batch.draw(currentFrame, x,y,width,height);
     }
}

