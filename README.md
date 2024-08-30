# APITesting

---
## Description
### Developed by: Helen Alves

---

### Objective
This repository contains a set of automated tests to validate the functionalities of an API. The tests are written in Java using the Rest-Assured library.

## Project Structure

- **src/main/java/**: Contains the test files.
- **pom.xml**: Maven configuration file, including the necessary dependencies for the project.

## Executed Test Cases

### 1. Get Product List
- **Description**: Validates that the `/productsList` endpoint returns the list of products with a successful status code.
- **Method**: `GET /productsList`
- **Expected Result**: Response contains `"responseCode": 200` and a list of products.

### 2. Should Not Post Product List
- **Description**: Ensures that posting to the `/productsList` endpoint is not allowed.
- **Method**: `POST /productsList`
- **Expected Result**: Response contains `"responseCode": 405`.

### 3. Get All Brands List
- **Description**: Validates that the `/brandsList` endpoint returns the list of brands.
- **Method**: `GET /brandsList`
- **Expected Result**: Response contains `"responseCode": 200` and a list of brands.

### 4. Put All Brands List
- **Description**: Ensures that updating the `/brandsList` endpoint is not supported.
- **Method**: `PUT /brandsList`
- **Expected Result**: Response contains `"responseCode": 405` and a message indicating the request method is not supported.

### 5. Search Products
- **Description**: Tests searching for products using the `/searchProduct` endpoint with a valid search parameter.
- **Method**: `POST /searchProduct`
- **Expected Result**: Response contains `"responseCode": 200` and the search results with products matching the search term.

### 6. Search Products Without Search Parameter
- **Description**: Validates that the `/searchProduct` endpoint returns an error when the search parameter is missing.
- **Method**: `POST /searchProduct`
- **Expected Result**: Response contains `"responseCode": 400` and an error message indicating the missing parameter.

### 7. Verify Login With Valid Credentials
- **Description**: Tests the `/verifyLogin` endpoint with valid login credentials.
- **Method**: `POST /verifyLogin`
- **Expected Result**: Response contains `"responseCode": 200` and a message confirming that the user exists.

### 8. Verify Login Without Email
- **Description**: Ensures that the `/verifyLogin` endpoint returns an error when the email parameter is missing.
- **Method**: `POST /verifyLogin`
- **Expected Result**:Response contains `"responseCode": 400` and an error message about the missing email or password parameter.

### 9. Delete Verify Login
- **Description**: Ensures that deleting the `/verifyLogin` endpoint is not supported.
- **Method**: `DELETE /verifyLogin`
- **Expected Result**: Response contains `"responseCode": 405` and a message indicating the request method is not supported.

### 10. Verify Login With Invalid Credentials
- **Description**: Tests the `/verifyLogin` endpoint with invalid login credentials.
- **Method**: `POST /verifyLogin`
- **Expected Result**: Response contains `"responseCode": 404` and a message indicating that the user was not found.

### 11. Create User Account
- **Description**: Tests the creation of a new user account using the `/createAccount` endpoint.
- **Method**: `POST /createAccount`
- **Expected Result**: Response contains `"responseCode": 201` and a message indicating that the user was created.

### 12. Delete User Account
- **Description**: Attempts to delete a user account using the `/deleteAccount` endpoint.
- **Method**: `DELETE /deleteAccount`

### 13. Update User Account
- **Description**: Attempts to update a user account using the `/updateAccount` endpoint.
- **Method**: `PUT /updateAccount`

### 14. Get User by Email
- **Description**: Retrieves user details by email using the `/getUserDetailByEmail` endpoint.
- **Method**: `GET /getUserDetailByEmail`
- **Expected Result**: Response contains `"responseCode": 200` and user details matching the provided email.

## How to Run the Tests

1. Clone this repository.
   ```bash
   git clone https://github.com/helenalvs/APITesting.git
