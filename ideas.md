# General ideas to implement and about the implementation
The objective of this markdown file is to write down the general ideas that i have during the process of making the game.
The game to be implemented is [Conways game of life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).
The initial idea is to present it to the user through a java swing generated window.
Nevertheless, the logic of the game should be implemented before. As it was implicitly said, i'm going to use the
MVC (Model-View-Controller) architecture to structure all the program.
Therefore, some other software patterns such as the Observer design pattern may be implemented.

Having stated the general porpuse of this project, any other idea (mainly more specific ideas) will be classified either as an [*abstract idea*](https://github.com/Jaimepas77/The-game-of-life/edit/main/ideas.md#abstract-ideas) or as an [*implementation idea*](https://github.com/Jaimepas77/The-game-of-life/edit/main/ideas.md#implementation-ideas).

## Abstract ideas

- Doing an UML diagram for general planning -> NO
- Game mode to select a certain area of the board. After this, you may be able to save it in a file or move it to another part of the board. -> done
- Speed variations (enable an extreme speed slider with a button or smth like that) -> exponential slider
- Documenting the java functions with the @ built in functionality

## Implementation ideas

- Setter for the board active cells color -> done (maybe do a second color, the background one)
- Undo step option (stack of past states) -> done
- Replacing delay by speed -> done
- Load patterns from files -> done
- Making the board a torus (geometric topology) (easy) -> done
- Personalise option for the size of the board
- Use a graphic rendered component instead of a grid of buttons to represent the board -> done
- ~~Replace the selection mode with a drag to select functionality or~~ implement the selection mode in a way that you can drag to select when enabled -> done
- Add a help button to see a description of every functionality
- Add the ability to rotate the pieces to be inserted with the wheel of the mouse -> done
- Disable the posibility of clicking with the right button or wheel button of the mouse for normal actions
- Add a check box to disable or enable the grid borders.
- Change the colors slightly when the selection mode is activated. Also change the mouse cursor (already done this last part).