# CMSC137-PROJECT

## GROUP NAME: I 1-2 PASS

## GROUP MEMBERS:

### Abarro, Jethro
### Samarista, Rainier
### Tapia, Orlando
### Villanueva, Ezra

**Programming Language:** *Java*

**Integrated Development Environment (IDE):** *IntelliJ, Netbeans 11*

**Github Repository:** https://github.com/jetabarro/CMSC137-PROJECT

**Communication Protocol:**

*The client and the server will communicate using the following string as a packet*
  
  ### CC:PP:TT
  
  **_Message Code_ (CC)** Corresponds to the type of packet

*The following codes will be used as packet codes*

Codes | Description
------------ | -------------
00 | Ready
01 | Start/Count
02 | Pass
03 | Cards Matched
  
  **_Player ID_ (PP)** Corresponds to the identity of the sender
  
  **_Turn/Card/Ranking_ (TT)** Corresponds to the turn passed by the players or rank if won.
  
  *The following codes will be used for the cards to be passed*
  
  Ranks | Suits
------------ | -------------
A - Ace | D - Diamonds
2-0 - Cards 2-10 | H - Hearts
J - Jack | S - Spades
Q - Queen | C - Clubs
K - King


