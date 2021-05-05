# Snake Game

Snake is a video game where the player maneuvers a line which grows in length, with the line itself being a primary obstacle. <br />
The goal is to control the snake to eat as much food as possible while preventing the snake from hitting itself. <br />

The player can control the snake movements with the arrow keys. Eating a food block will increase the snake length and speed. <br />

Pressing the number keys 1, 2 and 3 allows the player to switch between levels. The game will proceed to the next level automatically if the snake is still alive when the timer reaches 0 on the previous level. <br />

Developer's Journal:

1. The snake starts to move really fast after frequently switching between game levels. In such cases, please press p to pause and press p again to resume as this resets the snake's speed to normal.

2. The countdown timer is shared by all three levels; switching between levels does not reset the timer. Level 3 also has a timer display but user may continue to play after timer reaches 0.

3. If the user is in level 3 and timer is currently 0, switching to other levels will reset the timer to 30.

4. Pressing R reset the game to the splash screen; however, sometimes the splash screen will turn into score screen after a few seconds. In such cases, please press R in the score screen to successfully reset the game. 



openjdk version "11.0.8" 2020-07-14 <br>
macOS 10.14.6 (MacBook Pro 2019)
