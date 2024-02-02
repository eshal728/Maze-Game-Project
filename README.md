#Project Work (Project: Maze Runner) - NOOTNOOT

Maze Runner Game in Java with libGDX

#DESCRIPTION
This project is a 2D Maze Runner Game implemented in Java using the libGDX framework.
The game involves the Player navigating a character past Traps, Enemies and Walls, successfully exiting the Maze using the Key.
The Player must collect a Key to unlock the exit before losing all Lives.
The Key is only made visible to the player once the Treasure has been opened.
The game features a two-dimensional top-down view with a dynamic camera, and it supports different screen sizes.
The size of the screen can be adjusted by zooming in or out using the COMMA or PERIOD button.
Additionally, it includes background music, sound effects, and a user-friendly navigation system.

#PROJECT STRUCTURE
The project is organized into several classes, each serving a specific purpose. Here's a brief overview:

- GameObject.java: the superclass for the dynamic objects such as Enemy, Devil, Guardian Angel, and Character.
It is responsible for their basic functionalities and stores the x,y coordinates to position them in the 2D game world.

- MapObject.java: the superclass for the static objects such as Trap, Wall, Exit and Entry. It offers methods for loading
static and dynamic textures, rendering these textures on the game screen. This class serves as a foundational structure
for creating diverse map objects with unique visual elements in the game.

- GameScreen.java: responsible for rendering the gameplay screen. It handles the game logic, rendering of elements,
and includes features for dynamic obstacle movement. It also loads maze data from a properties file, camera setup.
Additionally, it handles the screen's appearance, and functionality during gameplay.

Character Movement
- In order to move the character, the player can either use the arrow keys or the WASD keys based on player preference.
To kill the enemies, the SPACE key must be pressed along with the movement keys. The character can only kill enemies
while it is in motion.


- Devil.java: represents a Grim Reaper, a smart Enemy that follows and attacks the character.
Colliding with the Grim Reaper results in the Character losing. It features animations for different directions
and renders the devil. To make it distinctive,the Grim Reaper is not affected by wall collisions and
cannot be killed by pressing the SPACE key. It is unaffected by game characteristics that otherwise affect enemies.

- Lever.java: represents a lever that, when pulled, triggers animations and updates textures related to movable walls.
The movable wall itself, crumbles when the lever is pulled.

- Treasure.java: represents a treasure chest in the maze. Opening the treasure is a necessary action to make the Key
appear in the maze. It includes methods for loading textures, handling animations, and rendering the treasure.

- Tree.java: represents a Tree of Good and Evil which has two distinct textures. Upon collision with
the Tree of Good and Evil, the character experiences either a restricted view of enemies or immunity to fire.

- Lamp.java: represents a purely aesthetic attribute of the maze stationed on either side of the Exit.

Temporary and Informative Screens of the parent class Screens

Informative screens are screens that appear after collisions throughout the game. They are set to provide the player with
a description of the Maze Object or Game Object they collided with. Additionally, it describes what happens to the character
due to this collision. Screens that fall under this category are KeyScreen, AngelScreen, TreeScreenGood and TreeScreenEvil.

Temporary screens are primarily there for aesthetics or visual confirmation of the after-effects of the collision.
An example is the HeartScreen that appears after the AngelScreen. This screen is strictly aesthetic but also allows
the player to visually recognize that they have gained a heart. Screens that fall under this category are TreasureScreen,
and HeartScreen.

In the Victory Screen, the player can see how long they took to successfully navigate the maze. The goal is to encourage
 the player to create intrinsic goals and want to replay the game.
