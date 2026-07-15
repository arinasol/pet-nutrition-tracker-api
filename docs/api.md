# 🌐 REST API Design

REST API structure of the Pet Nutrition Tracker API.

## 📑 Table of Contents

* [General Conventions](#general-conventions)
* [Authentication](#authentication)
* [Current User](#current-user)
* [Pets](#pets)
* [Foods](#foods)
* [Daily Entries](#daily-entries)
* [Feeding Records](#feeding-records)
* [Weight Records](#weight-records)
* [Daily Summary](#daily-summary)
* [Error Responses](#error-responses)
* [HTTP Status Codes](#http-status-codes)

---
## API Overview


| Method   | Endpoint                                                              | Description                           |
| -------- | --------------------------------------------------------------------- | ------------------------------------- |
| `POST`   | [`/api/auth/register`](#register-user)                                | Register a new user                   |
| `POST`   | [`/api/auth/login`](#login)                                           | Authenticate a user                   |
| `GET`    | [`/api/users/me`](#get-current-user)                                  | Get the authenticated user            |
| `POST`   | [`/api/pets`](#create-pet)                                            | Create a pet                          |
| `GET`    | [`/api/pets`](#get-all-pets)                                          | Get all user pets                     |
| `GET`    | [`/api/pets/{petId}`](#get-pet)                                       | Get one pet                           |
| `PUT`    | [`/api/pets/{petId}`](#update-pet)                                    | Update a pet                          |
| `DELETE` | [`/api/pets/{petId}`](#delete-pet)                                    | Delete a pet                          |
| `POST`   | [`/api/foods`](#create-food)                                          | Create a food product                 |
| `GET`    | [`/api/foods`](#get-all-foods)                                        | Get all food products                 |
| `GET`    | [`/api/foods/{foodId}`](#get-food)                                    | Get one food product                  |
| `PUT`    | [`/api/foods/{foodId}`](#update-food)                                 | Update a food product                 |
| `DELETE` | [`/api/foods/{foodId}`](#delete-food)                                 | Delete a food product                 |
| `POST`   | [`/api/pets/{petId}/daily-entries`](#create-daily-entry)              | Create a feeding with optional weight |
| `GET`    | [`/api/pets/{petId}/feedings`](#get-feeding-history)                  | Get feeding history                   |
| `GET`    | [`/api/pets/{petId}/feedings/{feedingId}`](#get-feeding-record)       | Get one feeding record                |
| `PUT`    | [`/api/pets/{petId}/feedings/{feedingId}`](#update-feeding-record)    | Update a feeding record               |
| `DELETE` | [`/api/pets/{petId}/feedings/{feedingId}`](#delete-feeding-record)    | Delete a feeding record               |
| `POST`   | [`/api/pets/{petId}/weights`](#create-weight-record)                  | Create a weight record                |
| `GET`    | [`/api/pets/{petId}/weights`](#get-weight-history)                    | Get weight history                    |
| `GET`    | [`/api/pets/{petId}/weights/latest`](#get-latest-weight)              | Get the latest weight                 |
| `DELETE` | [`/api/pets/{petId}/weights/{weightRecordId}`](#delete-weight-record) | Delete a weight record                |
| `GET`    | [`/api/pets/{petId}/daily-summary`](#get-daily-summary)               | Get a daily food summary              |

## General Conventions

### Base URL

```text
/api
```

### Content Type

All request and response bodies use JSON.

```http
Content-Type: application/json
```

### Authentication

Protected endpoints require a JWT access token.

```http
Authorization: Bearer <access-token>
```

### Date and Time Formats

Dates use the ISO 8601 format.

```text
Date:
2026-07-15

Date and time:
2026-07-15T08:30:00
```

### Ownership

An authenticated user can access only their own:

* pets;
* food products;
* feeding records;
* weight records.

If a resource does not belong to the authenticated user, the API should not return its data.

---

# Authentication

## Register User

Creates a new user account.

```http
POST /api/auth/register
```

### Request

```json
{
  "name": "User",
  "email": "user@example.com",
  "password": "StrongPassword123"
}
```

### Response

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "name": "User",
    "email": "user@example.com"
  }
}
```

### Status Codes

* `201 Created` — user account created;
* `400 Bad Request` — invalid request data;
* `409 Conflict` — email already exists.

---

## Login

Authenticates a user.

```http
POST /api/auth/login
```

### Request

```json
{
  "email": "user@example.com",
  "password": "StrongPassword123"
}
```

### Response

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "name": "User",
    "email": "user@example.com"
  }
}
```

### Status Codes

* `200 OK` — authentication successful;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — invalid email or password.

---

# Current User

## Get Current User

Returns information about the authenticated user.

```http
GET /api/users/me
```

### Response

```json
{
  "id": 1,
  "name": "User",
  "email": "user@example.com",
  "createdAt": "2026-07-15T10:00:00"
}
```

### Status Codes

* `200 OK` — user returned;
* `401 Unauthorized` — authentication token missing or invalid.

---

# Pets

## Create Pet

Creates a new pet for the authenticated user.

```http
POST /api/pets
```

### Request

```json
{
  "name": "Milo",
  "species": "CAT",
  "breed": "British Shorthair",
  "birthDate": "2020-04-12",
  "sex": "MALE",
  "dailyFoodTargetGrams": 60
}
```

Optional fields:

* `breed`;
* `birthDate`.

### Response

```json
{
  "id": 1,
  "name": "Milo",
  "species": "CAT",
  "breed": "British Shorthair",
  "birthDate": "2020-04-12",
  "sex": "MALE",
  "dailyFoodTargetGrams": 60,
  "currentWeightKg": null,
  "createdAt": "2026-07-15T10:15:00"
}
```

### Status Codes

* `201 Created` — pet created;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — user is not authenticated.

---

## Get All Pets

Returns all pets owned by the authenticated user.

```http
GET /api/pets
```

### Response

```json
[
  {
    "id": 1,
    "name": "Milo",
    "species": "CAT",
    "breed": "British Shorthair",
    "birthDate": "2020-04-12",
    "sex": "MALE",
    "dailyFoodTargetGrams": 60,
    "currentWeightKg": 6.2,
    "createdAt": "2026-07-15T10:15:00"
  }
]
```

### Status Codes

* `200 OK` — pets returned;
* `401 Unauthorized` — user is not authenticated.

---

## Get Pet

Returns one pet owned by the authenticated user.

```http
GET /api/pets/{petId}
```

### Response

```json
{
  "id": 1,
  "name": "Milo",
  "species": "CAT",
  "breed": "British Shorthair",
  "birthDate": "2020-04-12",
  "sex": "MALE",
  "dailyFoodTargetGrams": 60,
  "currentWeightKg": 6.2,
  "createdAt": "2026-07-15T10:15:00"
}
```

### Status Codes

* `200 OK` — pet returned;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet does not exist or does not belong to the user.

---

## Update Pet

Updates pet information.

```http
PUT /api/pets/{petId}
```

### Request

```json
{
  "name": "Milo",
  "species": "CAT",
  "breed": "British Shorthair",
  "birthDate": "2020-04-12",
  "sex": "MALE",
  "dailyFoodTargetGrams": 55
}
```

### Response

```json
{
  "id": 1,
  "name": "Milo",
  "species": "CAT",
  "breed": "British Shorthair",
  "birthDate": "2020-04-12",
  "sex": "MALE",
  "dailyFoodTargetGrams": 55,
  "currentWeightKg": 6.2,
  "createdAt": "2026-07-15T10:15:00"
}
```

### Status Codes

* `200 OK` — pet updated;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet does not exist or does not belong to the user.

---

## Delete Pet

Deletes a pet.

```http
DELETE /api/pets/{petId}
```

### Response

No response body.

### Status Codes

* `204 No Content` — pet deleted;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet does not exist or does not belong to the user.

---

# Foods

## Create Food

Creates a food product for the authenticated user.

```http
POST /api/foods
```

### Request

```json
{
  "name": "Dental Dry Cat Food",
  "brand": "Canagan",
  "foodType": "DRY"
}
```

The `brand` field is optional.

### Response

```json
{
  "id": 1,
  "name": "Dental Dry Cat Food",
  "brand": "Canagan",
  "foodType": "DRY",
  "createdAt": "2026-07-15T10:30:00"
}
```

### Status Codes

* `201 Created` — food product created;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — user is not authenticated.

---

## Get All Foods

Returns all food products created by the authenticated user.

```http
GET /api/foods
```

### Response

```json
[
  {
    "id": 1,
    "name": "Dental Dry Cat Food",
    "brand": "Canagan",
    "foodType": "DRY",
    "createdAt": "2026-07-15T10:30:00"
  }
]
```

### Status Codes

* `200 OK` — food products returned;
* `401 Unauthorized` — user is not authenticated.

---

## Get Food

Returns one food product.

```http
GET /api/foods/{foodId}
```

### Response

```json
{
  "id": 1,
  "name": "Dental Dry Cat Food",
  "brand": "Canagan",
  "foodType": "DRY",
  "createdAt": "2026-07-15T10:30:00"
}
```

### Status Codes

* `200 OK` — food product returned;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — food product does not exist or does not belong to the user.

---

## Update Food

Updates a food product.

```http
PUT /api/foods/{foodId}
```

### Request

```json
{
  "name": "Dental Cat Food",
  "brand": "Canagan",
  "foodType": "DRY"
}
```

### Response

```json
{
  "id": 1,
  "name": "Dental Cat Food",
  "brand": "Canagan",
  "foodType": "DRY",
  "createdAt": "2026-07-15T10:30:00"
}
```

### Status Codes

* `200 OK` — food product updated;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — food product does not exist or does not belong to the user.

---

## Delete Food

Deletes a food product.

```http
DELETE /api/foods/{foodId}
```

### Response

No response body.

### Status Codes

* `204 No Content` — food product deleted;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — food product does not exist or does not belong to the user;
* `409 Conflict` — food product is referenced by existing feeding records.

---

# Daily Entries

A daily entry creates a feeding record and may also create a weight record.

The weight is optional.

## Create Daily Entry

```http
POST /api/pets/{petId}/daily-entries
```

### Request Without Weight

```json
{
  "foodId": 1,
  "amountGrams": 25,
  "fedAt": "2026-07-15T08:30:00",
  "notes": "Ate normally"
}
```

### Request With Weight

```json
{
  "foodId": 1,
  "amountGrams": 25,
  "fedAt": "2026-07-15T08:30:00",
  "weightKg": 6.2,
  "notes": "Ate normally"
}
```

### Response Without Weight

```json
{
  "feeding": {
    "id": 1,
    "petId": 1,
    "foodId": 1,
    "foodName": "Dental Dry Cat Food",
    "amountGrams": 25,
    "fedAt": "2026-07-15T08:30:00",
    "notes": "Ate normally",
    "createdAt": "2026-07-15T08:31:00"
  },
  "weightRecord": null
}
```

### Response With Weight

```json
{
  "feeding": {
    "id": 1,
    "petId": 1,
    "foodId": 1,
    "foodName": "Dental Dry Cat Food",
    "amountGrams": 25,
    "fedAt": "2026-07-15T08:30:00",
    "notes": "Ate normally",
    "createdAt": "2026-07-15T08:31:00"
  },
  "weightRecord": {
    "id": 1,
    "petId": 1,
    "weightKg": 6.2,
    "measuredAt": "2026-07-15T08:30:00",
    "notes": null,
    "createdAt": "2026-07-15T08:31:00"
  }
}
```

### Business Logic

* A feeding record is always created.
* A weight record is created only when `weightKg` is provided.
* The pet and food product must belong to the authenticated user.
* Feeding and optional weight are saved in one transaction.
* If weight creation fails, the feeding record is rolled back.

### Status Codes

* `201 Created` — daily entry created;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet or food product does not exist or does not belong to the user.

---

# Feeding Records

## Get Feeding History

Returns feeding records for one pet.

```http
GET /api/pets/{petId}/feedings
```

Optional filters:

```http
GET /api/pets/{petId}/feedings?date=2026-07-15
```

```http
GET /api/pets/{petId}/feedings?from=2026-07-01&to=2026-07-15
```

### Response

```json
[
  {
    "id": 1,
    "petId": 1,
    "foodId": 1,
    "foodName": "Dental Dry Cat Food",
    "amountGrams": 25,
    "fedAt": "2026-07-15T08:30:00",
    "notes": "Ate normally",
    "createdAt": "2026-07-15T08:31:00"
  }
]
```

### Status Codes

* `200 OK` — feeding records returned;
* `400 Bad Request` — invalid date range;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet does not exist or does not belong to the user.

---

## Get Feeding Record

Returns one feeding record.

```http
GET /api/pets/{petId}/feedings/{feedingId}
```

### Status Codes

* `200 OK` — feeding record returned;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet or feeding record does not exist or does not belong to the user.

---

## Update Feeding Record

Updates an existing feeding record.

```http
PUT /api/pets/{petId}/feedings/{feedingId}
```

### Request

```json
{
  "foodId": 1,
  "amountGrams": 30,
  "fedAt": "2026-07-15T08:30:00",
  "notes": "Corrected amount"
}
```

### Response

```json
{
  "id": 1,
  "petId": 1,
  "foodId": 1,
  "foodName": "Dental Dry Cat Food",
  "amountGrams": 30,
  "fedAt": "2026-07-15T08:30:00",
  "notes": "Corrected amount",
  "createdAt": "2026-07-15T08:31:00"
}
```

### Status Codes

* `200 OK` — feeding record updated;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet, food product, or feeding record does not exist or does not belong to the user.

---

## Delete Feeding Record

Deletes an existing feeding record.

```http
DELETE /api/pets/{petId}/feedings/{feedingId}
```

### Response

No response body.

### Status Codes

* `204 No Content` — feeding record deleted;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet or feeding record does not exist or does not belong to the user.

---

# Weight Records

## Create Weight Record

Creates a weight record without creating a feeding record.

```http
POST /api/pets/{petId}/weights
```

### Request

```json
{
  "weightKg": 6.2,
  "measuredAt": "2026-07-15T09:00:00",
  "notes": "Measured before breakfast"
}
```

The `notes` field is optional.

### Response

```json
{
  "id": 1,
  "petId": 1,
  "weightKg": 6.2,
  "measuredAt": "2026-07-15T09:00:00",
  "notes": "Measured before breakfast",
  "createdAt": "2026-07-15T09:01:00"
}
```

### Status Codes

* `201 Created` — weight record created;
* `400 Bad Request` — invalid request data;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet does not exist or does not belong to the user.

---

## Get Weight History

Returns all weight records for one pet.

```http
GET /api/pets/{petId}/weights
```

### Response

```json
[
  {
    "id": 2,
    "petId": 1,
    "weightKg": 6.0,
    "measuredAt": "2026-08-01T09:00:00",
    "notes": null,
    "createdAt": "2026-08-01T09:01:00"
  },
  {
    "id": 1,
    "petId": 1,
    "weightKg": 6.2,
    "measuredAt": "2026-07-15T09:00:00",
    "notes": "Measured before breakfast",
    "createdAt": "2026-07-15T09:01:00"
  }
]
```

Weight records are returned from newest to oldest by `measuredAt`.

### Status Codes

* `200 OK` — weight records returned;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet does not exist or does not belong to the user.

---

## Get Latest Weight

Returns the most recent weight record.

```http
GET /api/pets/{petId}/weights/latest
```

### Response

```json
{
  "id": 2,
  "petId": 1,
  "weightKg": 6.0,
  "measuredAt": "2026-08-01T09:00:00",
  "notes": null,
  "createdAt": "2026-08-01T09:01:00"
}
```

### Status Codes

* `200 OK` — latest weight record returned;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet or weight record does not exist.

---

## Delete Weight Record

Deletes a weight record.

```http
DELETE /api/pets/{petId}/weights/{weightRecordId}
```

### Response

No response body.

### Status Codes

* `204 No Content` — weight record deleted;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet or weight record does not exist or does not belong to the user.

---

# Daily Summary

## Get Daily Summary

Calculates the daily food intake summary for one pet.

```http
GET /api/pets/{petId}/daily-summary?date=2026-07-15
```

### Response

```json
{
  "petId": 1,
  "petName": "Milo",
  "date": "2026-07-15",
  "dailyFoodTargetGrams": 60,
  "totalConsumedGrams": 45,
  "remainingGrams": 15
}
```

### Calculation

```text
remainingGrams = dailyFoodTargetGrams - totalConsumedGrams
```

`totalConsumedGrams` is calculated by summing all feeding records for the selected pet and date.

The daily summary is calculated dynamically and is not stored in a database table.

### Status Codes

* `200 OK` — daily summary returned;
* `400 Bad Request` — date is missing or invalid;
* `401 Unauthorized` — user is not authenticated;
* `404 Not Found` — pet does not exist or does not belong to the user.

---

# Error Responses

All API errors use a consistent response structure.

## Validation Error

```json
{
  "timestamp": "2026-07-15T10:45:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/pets",
  "fieldErrors": {
    "name": "Name must not be blank",
    "dailyFoodTargetGrams": "Daily food target must be greater than zero"
  }
}
```

## Resource Not Found

```json
{
  "timestamp": "2026-07-15T10:45:00",
  "status": 404,
  "error": "Not Found",
  "message": "Pet not found",
  "path": "/api/pets/99"
}
```

## Authentication Error

```json
{
  "timestamp": "2026-07-15T10:45:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication is required",
  "path": "/api/pets"
}
```

## Conflict Error

```json
{
  "timestamp": "2026-07-15T10:45:00",
  "status": 409,
  "error": "Conflict",
  "message": "An account with this email already exists",
  "path": "/api/auth/register"
}
```

---

# HTTP Status Codes

| Status                      | Meaning                              | Usage                                               |
| --------------------------- | ------------------------------------ | --------------------------------------------------- |
| `200 OK`                    | Request successful                   | Successful GET or PUT request                       |
| `201 Created`               | Resource created                     | Successful POST request                             |
| `204 No Content`            | Resource deleted                     | Successful DELETE request                           |
| `400 Bad Request`           | Invalid input                        | Validation or malformed request                     |
| `401 Unauthorized`          | Authentication required              | Missing or invalid token                            |
| `404 Not Found`             | Resource unavailable                 | Resource does not exist or is not owned by the user |
| `409 Conflict`              | Request conflicts with existing data | Duplicate email or resource dependency              |
| `500 Internal Server Error` | Unexpected server error              | Unhandled application failure                       |
