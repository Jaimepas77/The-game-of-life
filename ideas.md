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
- idea3

## Implementation ideas

- Setter for the board active cells color -> done (maybe do a second color, the background one)
- Undo step option (stack of past states) -> done
- Load patterns from files
- Making the board a torus (geometric topology) (easy)
- Personalise option for the size of the board
