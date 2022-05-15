# General ideas to implement and about the implementation
The objective of this markdown file is to write down the general ideas that i have during the process of making the game.
The game to be implemented is [Conways game of life] (https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).
The initial idea is to present it to the user through a java swing generated window.
Nevertheless, the logic of the game should be implemented before. In this part, i initially want to make a thread for each square of the game. 
This threads should be synchronised by a barrier, which would update the interface every time that is reached by every thread/square.
This last idea represents the steps being done in the game. As it was implicitly said, i'm going to use the
MVC (Model-View-Controller) architecture to structure all the program.
Therefore, some other software patterns such as the Observer design pattern may be implemented.

Having stated the general porpuse of this project, any other idea (mainly more specific ideas) will be classified either as an *abstract idea* or as an *implementation idea*.

## Abstract ideas

- idea1
- idea3

## Implementation ideas

- idea2
- idea4
