# Starling Challenge
## Candidate : Vinesh Raju

> Add your Access Token to the local.properties file.

## Summary
 This app allows users to select an account, view outbound transactions since a selected date.
 Round up the amount of each transaction and transfer the total round up amount to a goal.
 
 This app has 3 screens:
 - Accounts  : It displays all the accounts available for the user.
 - Round Ups : It allows the user to pick a date. It displays all the transactions since the selected date.
               It also calculates and displays the round up amount for each transaction and the total round up amount.
               It allows the user to transfer the total round up amount to the a goal.
 - Goals     : It displays all the goals created by the user.
               It allows the user to create new goals.
               On selecting a goal, it transfers the round up amount to the selected goal.
 
## General Overview:
 - ViewModels define 2 states
   1. clientState: defined as a MutableStateFlow. It holds all the data required to represent the client's state.
   2. uiState: defined as a StateFlow. It holds the UI state. It is derived from the clientState.
               The View observes the uiState and renders the UI based on the state.
 - Reducers are used to convert the clientState to the uiState.
 - Usecases encapsulate the business logic. It fires api calls using the repository and updates the clientState.
