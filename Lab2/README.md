# Lab2-1173710229
Lab2-1173710229 created by GitHub Classroom

## P1 Poetic walk
All functions have been finished, including the tests.

## P2 Rewritten FriendshipGraph
I used ConcreteVertexGraph to implement this phase.  
All finished now(including tests).

## P3
I designed 5 interfaces and many other classes to implement this phase.  
3 interfaces may suitable for all Chess-like game besides chess and weiqi.  
The ChessAction interface is to specify the chess actions' framework, and the other one called WeiqiAction is for weiqi.  

### Chess
According to the specification, I've designed all kinds of pieces in a real chess game. Each one is a class. So there are Bishop, King, Knight, Pawn, Queen and Rook.  
But besides, there are two extra classes called Blank and ChessPiece. The former one is to represent the blank piece, the latter one is an abstract class, and it extends the abstract class Piece and implements interface ChessAction.  
To specify the status and the type of each kind of chess pieces, I created two more classes that contains only final variable(Enum is also ok but I'm not sure if I can use it in a right way).

### Weiqi
The framework is familiar with the Chess part, but Weiqi has only one class to represent piece.


