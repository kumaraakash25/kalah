Kalah Springboot implementation

Kalah is a two player board game, the current implementation allows the player to play against the computer 

To run the application run `./gradlew bootRun `in the terminal

The user creates the Game by the following cURL 

`curl -X POST \
http://localhost:8080/games \
  -H 'cache-control: no-cache' \
  -H 'postman-token: <YOUR TOKEN>'`

The game initialisation creates a new Game, Board and 2 Players
(1. HUMAN 2. COMPUTER) 
and saves them to the in memory H2 database, properties read from application.yaml file 

The HUMAN player is expected to make the first move. That can be done by 

`curl -X PUT \
  http://localhost:8080/games/{gameId}/pits/{pitNumber} \
  -H 'cache-control: no-cache' \
  -H 'postman-token: <YOUR_TOKEN>'  `
  
which happens in a transaction that sets the current game state to MOVE_IN_PROGRESS. After the move is completed the game status is set to WAITING_FOR_COMPUTER_MOVE and the transaction is completed.

The COMPUTER moves are done via spring scheduled job that runs every 10 seconds, it finds all the games with the status where a computer move is expected, generates a random number 
and makes the moves. After the transaction is complete, the game status is set to WAITING_FOR_HUMAN_MOVE which enables the HUMAN player to make the next move in 8-13 range and makes the move
If any player tries to make the move out of turn an exception is thrown 

After the last move is done by either of the players, the game status is set to FINISHED

