# bity-tech-challenge

------------------------------------------------------

# Android Developer Technical Challenge

Welcome to the Android Developer Technical Challenge! This project is designed to assess your Android development skills, focusing on UI design with Jetpack Compose, API integration, and basic navigation.

Please carefully read the instructions and requirements below. You have **1 week** to complete the project and submit your solution.

---

## Project Requirements

### Functional Requirements

1. **Home Page:**
   - Allow the user to paste an Ethereum address into a text field.
   - Fetch and display the **balance** of the provided address using the [Etherscan API](https://docs.etherscan.io/).
   - Display a list of [normal transactions](https://docs.etherscan.io/api-endpoints/accounts#get-a-list-of-normal-transactions-by-address) associated with the address.
   - The user should be able to click on a transaction to view its details on a separate screen.

2. **Transaction Details:**
   - On the transaction details screen, display the following information:
     - **Transaction Hash**
     - **Timestamp**
     - **To Address:** The recipient's Ethereum address.

3. **No Validation Required:**
   - Address validation is not required. The API will handle any errors related to invalid addresses.

### UI Requirements

- The UI must be built using **Jetpack Compose**.
- There are no strict requirements for design; feel free to:
  - Add animations.
  - Choose a custom color scheme or theme.

### Extra Credit (Optional)

If time permits, implementing the following will earn extra points:

1. **Testing:**
   - Include **UI tests** for Compose components.
   - Add **unit tests**.

2. **Gas Tracker:**
   - Poll the [Etherscan Gas Oracle API](https://docs.etherscan.io/etherscan-v2/api-endpoints/gas-tracker#get-gas-oracle) every **30 seconds**.
   - Display the gas prices (low, average, and high) on the UI.

---

## Deliverables

1. **Source Code:**
   - Submit the complete Android project in a **zip file**.
   - Ensure the project is buildable and runnable using Android Studio.

2. **APK:**
   - Provide a compiled APK file for quick testing.

---

## Submission Instructions

1. Package your project in a **zip file** (including the APK).
2. Name the file using the following format: `yourname_android_challenge.zip`.
3. Submit the file to thomas.constantin@bity.com.

---

## Evaluation Criteria

1. **Code Quality:**
   - Clean, readable, and modular code.
   - Proper use of Jetpack Compose.

2. **Functionality:**
   - Correct implementation of the core features.

3. **Bonus Implementation (Optional):**
   - Quality and thoroughness of tests.
   - Effective implementation of the Gas Tracker feature.

---

## Etherscan API Setup

To fetch data from the Etherscan API, you’ll need to:

1. **Create an Etherscan Account:**  
   Sign up for an account on [Etherscan.io](https://etherscan.io/).

2. **Generate an API Key:**  
   After logging in, navigate to the API section and create a new API key. This key will be required to access Etherscan’s API endpoints.

3. **Add the API Key to Your Project:**  
   Securely store the API key in your project. For example, use a configuration file or environment variable to manage the API key.

### NOTE: there is no need for API Pro subscription 
---

## Resources

- [Etherscan API Documentation](https://docs.etherscan.io/)

---
