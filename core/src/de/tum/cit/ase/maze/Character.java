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

     private boolean hasKey;
     private int lives;
     private int enemiesKilled;
     private final Animation<TextureRegion> animation;
     private static float stateTime;


     // Character animation downwards
     private static Animation<TextureRegion> characterDownAnimation;
     private static Animation<TextureRegion> characterUpAnimation;
     private static Animation<TextureRegion> characterLeftAnimation;
     private static Animation<TextureRegion> characterRightAnimation;
     private static Animation<TextureRegion> characterFightRightAnimation;
     private static Animation<TextureRegion> characterFightLeftAnimation;
     private static Animation<TextureRegion> characterFightUpAnimation;
     private static Animation<TextureRegion> characterFightDownAnimation;

     private TextureRegion characterRegion;

     private boolean isTextVisible;
     private boolean shouldMove;
     private boolean isLeverPulled;
     private final OrthographicCamera camera;
     private final MazeRunnerGame game;
     private final BitmapFont font;
     private final GameScreen gameScreen;
     private boolean isKeyPressed;
     private boolean isTreasureOpened;

     private long lastCollisionTime;
     private static final long COLLISION_COOLDOWN = 3000; // for 3 seconds, the character doesn't lose its lives
     private static final long COLLISION_COOLDOWN1 = 1000;

     private boolean doneCooling;


     private static Animation<TextureRegion> currentAnimation;
     private static TextureRegion collisionImageRegion;
     private static TextureRegion angelMeetingImageRegion;
     private static TextureRegion heartImageRegion;
     private static TextureRegion keyImageRegion;




     //Constructor
     public Character(GameScreen gameScreen, MazeRunnerGame game, float x, float y, boolean hasKey, boolean isLeverPulled, boolean isTreasureOpened, int lives,  Animation<TextureRegion> animation) {
         super(x, y);
         this.game = game; // Store the game instance
         this.hasKey = hasKey;
         this.lives = lives;
         this.animation = animation;
         stateTime = 0f;
         this.gameScreen = gameScreen;
         this.isLeverPulled = isLeverPulled;
         this.isTreasureOpened=isTreasureOpened;

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

         Array<TextureRegion> walkFramesFightRight = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesFightLeft = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesFightUp = new Array<>(TextureRegion.class);
         Array<TextureRegion> walkFramesFightDown = new Array<>(TextureRegion.class);


         // Add frames to the respective animations
         for (int col = 0; col < animationFrames; col++) {
             walkFramesUp.add(
                     new TextureRegion(walkSheet, col * frameWidth, 2 * frameHeight, frameWidth, frameHeight));
             walkFramesDown.add(
                     new TextureRegion(walkSheet, col * frameWidth, 0, frameWidth, frameHeight));
             walkFramesLeft.add(
                     new TextureRegion(walkSheet, col * frameWidth, 3 * frameHeight, frameWidth, frameHeight));
             walkFramesRight.add(
                     new TextureRegion(walkSheet, col * frameWidth, frameHeight, frameWidth, frameHeight));


             walkFramesFightRight.add(
                    new TextureRegion(walkSheet, 8, 6* frameHeight, frameWidth, frameHeight));
             walkFramesFightRight.add(
                     new TextureRegion(walkSheet, 40, 6* frameHeight, frameWidth, frameHeight));
             walkFramesFightRight.add(
                     new TextureRegion(walkSheet, 72, 6* frameHeight, frameWidth, frameHeight));
             walkFramesFightRight.add(
                     new TextureRegion(walkSheet, 104, 6* frameHeight, frameWidth, frameHeight));


             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 8, 7* frameHeight, frameWidth, frameHeight));
             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 40, 7* frameHeight, frameWidth, frameHeight));
             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 72, 7* frameHeight, frameWidth, frameHeight));
             walkFramesFightLeft.add(
                     new TextureRegion(walkSheet, 104, 7* frameHeight, frameWidth, frameHeight));


             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 8, 5* frameHeight, frameWidth, frameHeight));
             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 40, 5* frameHeight, frameWidth, frameHeight));
             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 72, 5* frameHeight, frameWidth, frameHeight));
             walkFramesFightUp.add(
                     new TextureRegion(walkSheet, 104, 5* frameHeight, frameWidth, frameHeight));

             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 40, 4* frameHeight, frameWidth, frameHeight));
             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 40, 4* frameHeight, frameWidth, frameHeight));
             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 72, 4* frameHeight, frameWidth, frameHeight));
             walkFramesFightDown.add(
                     new TextureRegion(walkSheet, 104, 4* frameHeight, frameWidth, frameHeight));
     }



         characterFightRightAnimation = new Animation<>(0.1f, walkFramesFightRight);
         characterFightLeftAnimation = new Animation<>(0.1f, walkFramesFightLeft);
         characterFightUpAnimation = new Animation<>(0.1f, walkFramesFightUp);
         characterFightDownAnimation = new Animation<>(0.1f, walkFramesFightDown);


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
         if (isTextVisible) {
             font.draw(game.getSpriteBatch(), "Press ESC to Pause", gameScreen.getTextX(), gameScreen.getTextY());
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
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightUpAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightUpAnimation();
             }
             else{
                 currentAnimation = getCharacterUpAnimation();
                 characterRegion = getCharacterUpAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
             }

             if (!collidesWithWall(getX(), getY() + 5, GameScreen.getMazeArray())) {
                 setY((int) (getY() + 5));
             }
             shouldMove = true;
             isKeyPressed = true;


         } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightDownAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightDownAnimation();
             }
             else{
                 characterRegion = getCharacterDownAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterDownAnimation();
             }
             if (!collidesWithWall(getX(), getY() - 5, GameScreen.getMazeArray())) {
                 setY((int) (getY() - 5));
             }

             shouldMove = true;
             isKeyPressed = true;


         } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightLeftAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightLeftAnimation();
             }
             else{
                 characterRegion = getCharacterLeftAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterLeftAnimation();
             }
             if (!collidesWithWall(getX() - 5, getY(), GameScreen.getMazeArray())) {
                 setX((int) (getX() - 5));
             }


             shouldMove = true;
             isKeyPressed = true;
         } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
             if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                 characterRegion = getCharacterFightRightAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterFightRightAnimation();
             }
             else{
                 characterRegion = getCharacterRightAnimation().getKeyFrame(gameScreen.getSinusInput(), true);
                 currentAnimation = getCharacterRightAnimation();
             }

             if (!collidesWithWall(getX() + 5, getY(), GameScreen.getMazeArray())) {
                 setX((int) (getX() + 5));
             }

             shouldMove = true;
             isKeyPressed = true;

         } else {
             isKeyPressed = false;
         }

     }

     private long leverPullTime;


     public long getLeverPullTime() {
         return leverPullTime;
     }

     public void setLeverPullTime(long leverPullTime) {
         this.leverPullTime = leverPullTime;
     }

     public boolean collidesWithWall(float newX, float newY, int[][] mazeArray) {
         // Allowing the character to go through the wall a little bit to avoid getting stuck
         float collisionMarginTopRight = 0.8f * 50;
         float collisionMarginBottomLeft = 0.3f * 50;

         // Adjust the newX and newY with the collisionMarginTopRight
         float characterX = newX + collisionMarginBottomLeft;
         float characterY = newY + collisionMarginBottomLeft;
         float adjustedX = newX + collisionMarginTopRight;
         float adjustedY = newY + collisionMarginTopRight;

         // Calculate the cell coordinates for adjusted position
         int cellXForBottomLeft = (int) (characterX / 50);
         int cellYForBottomLeft = (int) (characterY / 50);
         int cellXForTopRight = (int) (adjustedX / 50);
         int cellYForTopRight = (int) (adjustedY / 50);


         if (cellXForTopRight < 0 || cellXForTopRight >= mazeArray.length || cellYForTopRight < 0 || cellYForTopRight >= mazeArray[0].length) {
             return true;
         }
         if (cellXForBottomLeft < 0 || cellXForBottomLeft >= mazeArray.length || cellYForBottomLeft < 0 || cellYForBottomLeft >= mazeArray[0].length) {
             return true;
         }

         boolean collisionTop = mazeArray[cellXForTopRight][cellYForTopRight] == 0; //
         boolean collisionBottom = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 0;
         boolean collisionLeft = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 0;
         boolean collisionRight = mazeArray[cellXForTopRight][cellYForTopRight] == 0;

         boolean collisionTopShadow = mazeArray[cellXForTopRight][cellYForTopRight] == 8; //
         boolean collisionBottomShadow = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 8;
         boolean collisionLeftShadow = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 8;
         boolean collisionRightShadow = mazeArray[cellXForTopRight][cellYForTopRight] == 8;

         boolean collisionTopMove = mazeArray[cellXForTopRight][cellYForTopRight] == 10; //
         boolean collisionBottomMove = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 10;
         boolean collisionLeftMove = mazeArray[cellXForBottomLeft][cellYForBottomLeft] == 10;
         boolean collisionRightMove = mazeArray[cellXForTopRight][cellYForTopRight] == 10;

         return collisionTop || collisionBottom || collisionLeft || collisionRight
                 || collisionBottomShadow || collisionLeftShadow || collisionRightShadow|| collisionTopShadow
                 || collisionBottomMove || collisionLeftMove || collisionRightMove|| collisionTopMove;
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

                 if(!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                     lastCollisionTime = currentTime;
                     loadCollisionImage();
                 }
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


     public boolean seesTheAngel(float angelX, float angelY) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - lastCollisionTime >= COLLISION_COOLDOWN1) {

             float characterX = getX();
             float characterY = getY();
             float characterWidth = 36;
             float characterHeight = 62;

             float angelWidth = 50;
             float angelHeight = 50;


             if (characterX < angelX + angelWidth + 100 &&
                     characterX + characterWidth + 100 > angelX &&
                     characterY < angelY + angelHeight + 100 &&
                     characterY + characterHeight + 100 > angelY) {
                 loadAngelMeetingImage();
                 return true;
             }
         }
             return false;
     }

     public boolean collidesWithAngel(float angelX, float angelY) {
         float characterX = getX();
         float characterY = getY();
         float characterWidth = 36;
         float characterHeight = 62;

         float angelWidth = 50;
         float angelHeight = 50;


         if (characterX < angelX + angelWidth + 20 &&
                  characterX + characterWidth + 20 > angelX &&
                  characterY < angelY + angelHeight + 20 &&
                  characterY + characterHeight + 20 > angelY) {
             loadAngelMeetingImage();
             return true;
         }
         return false;
     }

     public boolean collidesWithLever(float leverX, float leverY) {
         float characterX = getX();
         float characterY = getY();

         return characterX < leverX + 50 && characterX + 36 > leverX  &&
                 characterY <leverY + 50 && characterY + 31 > leverY;
     }

     public boolean collidesWithTreasure(float treasureX, float treasureY) {
         float characterX = getX();
         float characterY = getY();
         float characterWidth = 36;
         float characterHeight = 62;

         float treasureWidth = 50;
         float treasureHeight = 50;

         if(characterX < treasureX + treasureWidth &&
                 characterX + characterWidth > treasureX &&
                 characterY < treasureY + treasureHeight &&
                 characterY + characterHeight > treasureY) {
             return true;
         }
         return false;
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

     private void loadAngelMeetingImage() {
         Texture angelMeetingMark = new Texture(Gdx.files.internal("objects.png"));

         int frameWidth = 16;
         int frameHeight = 16;

         angelMeetingImageRegion = new TextureRegion(angelMeetingMark,3 * frameWidth,8 * frameHeight,frameWidth,frameHeight);
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

     public void renderAngelMeeting(SpriteBatch batch, float viewportWidth, float viewportHeight) {
        float angelMeetingMarkWidth = 40;
        float angelMeetingMarkHeight = 40;

        float angelMeetingImageX = viewportWidth + 20;
        float angelMeetingImageY = viewportHeight + 100;

        if (angelMeetingImageRegion != null) {
            batch.draw(angelMeetingImageRegion,angelMeetingImageX,angelMeetingImageY,angelMeetingMarkWidth,angelMeetingMarkHeight);
        }

         if (System.currentTimeMillis() - lastCollisionTime >= COLLISION_COOLDOWN1) {
             // Reset the cooldown image
             angelMeetingImageRegion = null;
         }
     }

     public void renderKeyImage(SpriteBatch batch, float viewportWidth, float viewportHeight) {
         float keyMarkWidth = 30;
         float keyMarkHeight = 30;

         float keyImageX = viewportWidth + 20;
         float keyImageY = viewportHeight + 100;

         if (keyImageRegion != null) {
             batch.draw(keyImageRegion,keyImageX,keyImageY,keyMarkWidth,keyMarkHeight);
         }

         if (System.currentTimeMillis() - lastCollisionTime >= COLLISION_COOLDOWN1) {
             // Reset the cooldown image
             keyImageRegion = null;
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

     public static Animation<TextureRegion> getCharacterFightRightAnimation() {
         return characterFightRightAnimation;
     }

     public static Animation<TextureRegion> getCharacterFightLeftAnimation() {
         return characterFightLeftAnimation;
     }

     public static Animation<TextureRegion> getCharacterFightUpAnimation() {
         return characterFightUpAnimation;
     }

     public static Animation<TextureRegion> getCharacterFightDownAnimation() {
         return characterFightDownAnimation;
     }

     public int getEnemiesKilled() {
         return enemiesKilled;
     }

     public void setEnemiesKilled(int enemiesKilled) {
         this.enemiesKilled = enemiesKilled;
     }

     public boolean isTreasureOpened() {
         return isTreasureOpened;
     }

     public void setTreasureOpened(boolean treasureOpened) {
         isTreasureOpened = treasureOpened;
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

     public boolean isLeverPulled() {
         return isLeverPulled;
     }

     public void setLeverPulled(boolean leverPulled) {
         isLeverPulled = leverPulled;
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

