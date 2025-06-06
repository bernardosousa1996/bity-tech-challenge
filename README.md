# Ethereum Wallet Explorer

A lightweight Android app that fetches and displays data for any Ethereum wallet using the **Etherscan API**. 

Built with Jetpack Compose, it allows users to check wallet balances, browse transaction history, and view real-time gas prices.

---

## Project Scope

1. **Home Page**

   [ ✅ ] Allow the user to paste an Ethereum address into a text field.
   
   [ ✅ ] Fetch and display the **balance** of the provided address using the [Etherscan API](https://docs.etherscan.io/).
   
   [ ✅ ] Display a list of [normal transactions](https://docs.etherscan.io/api-endpoints/accounts#get-a-list-of-normal-transactions-by-address) associated with the address.
   
   [ ✅ ] The user should be able to click on a transaction to view its details on a separate screen.

2. **Transaction Details**
   On the transaction details screen, display the following information:
   
   [ ✅ ] **Transaction Hash**
   
   [ ✅ ] **Timestamp**
   
   [ ✅ ] **To Address:** The recipient's Ethereum address.   

3. **Gas Tracker**

   [ ✅ ] Poll the [Etherscan Gas Oracle API](https://docs.etherscan.io/etherscan-v2/api-endpoints/gas-tracker#get-gas-oracle) every **30 seconds**.
   
   [ ✅ ] Display the gas prices (low, average, and high) on the UI.

4. **UI Details**

   [ ✅ ] Add animations.
   
   [ ✅ ] Choose a custom color scheme or theme.

5. **Testing**
   
   [ ✅ ] Include **UI tests** for Compose components.
   
   [ ✅ ] Add **unit tests**.

---

## Etherscan API Setup

To fetch data from the Etherscan API, you’ll need to:

1. **Create an Etherscan Account:**  
   Sign up for an account on [Etherscan.io](https://etherscan.io/).

2. **Generate an API Key:**  
   After logging in, navigate to the API section and create a new API key. This key will be required to access Etherscan’s API endpoints.

3. **Add the API Key to Your Project:**  
   Securely store the API key in your project. For example, use a configuration file or environment variable to manage the API key.

## Resources

- [Etherscan API Documentation](https://docs.etherscan.io/)

## Tech Stack
**Jetpack Compose** – Declarative UI toolkit.

**Kotlin + Coroutines** – Asynchronous programming.

**Retrofit + OkHttp** – API client.

**MVVM** – Scalable architecture pattern.

**Hilt (DI)** – Dependency injection.

**JUnit & Compose UI Tests** – Unit and UI testing.

---
