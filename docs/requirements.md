# 🐱 Pet Nutrition Tracker API
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![License](https://img.shields.io/badge/License-MIT-lightgrey)
---

## 📑 Table of Contents

- [🎯 Project Goal](#-1-project-goal)
- [👤 Target Users](#-2-target-users)
- [🚀 MVP Functionality](#-3-mvp-functionality)
    - [Authentication](#authentication)
    - [Pets](#pets)
    - [Foods](#foods)
    - [Feeding Records](#feeding-records)
    - [Optional Weight Recording](#optional-weight-recording)
    - [Daily Summary](#daily-summary)
    - [Weight History](#weight-history)
- [📋 Main Business Rules](#-4-main-business-rules)
- [❕ Features Excluded from MVP](#-5-features-excluded-from-mvp)

---
## 🎯 Project goal

Pet Nutrition Tracker is a backend application for tracking pets’ food intake, feeding history, and weight changes.

The application allows pet owners to record what and when their pets ate, monitor daily food intake, and optionally add the pet’s current weight.

## 👤 Target users

The application is intended for pet owners who want to:

* track their pet’s daily food intake;
* avoid overfeeding or underfeeding;
* keep a history of feedings;
* monitor weight changes;
* manage information about several pets.

## 🚀 MVP functionality

### Authentication

The user can:

* register an account;
* log in;
* access only their own data.

### Pets

The user can:

* add a pet;
* view all their pets;
* view one pet;
* update pet information;
* delete a pet.

Each pet has:

* name;
* species;
* optional breed;
* optional birth date;
* sex;
* daily food target in grams.

The pet’s current weight is retrieved from the latest weight record and is not stored directly in the pet entity.

### Foods

The user can:

* add a food product;
* view their food products;
* update a food product;
* delete a food product.

Each food product has:

* name;
* optional brand;
* food type.

### Feeding records

The user can:

* record a feeding;
* select the pet and food;
* enter the amount in grams;
* enter the feeding date and time;
* add an optional note;
* view feeding history;
* update an incorrect feeding record;
* delete an incorrect feeding record.

### Optional weight recording

When recording a feeding, the user can optionally enter the pet’s current weight.

If the weight is provided:

* a feeding record is created;
* a separate weight record is created.

If the weight is not provided:

* only the feeding record is created.

The user can also add a weight record separately without creating a feeding record.

### Daily summary

The user can view a daily summary for a pet.

The summary includes:

* daily food target;
* total amount consumed;
* remaining amount.

### Weight history

The user can:

* add a weight record;
* view weight history;
* view the latest recorded weight;
* delete an incorrect weight record.

## 📋 Main business rules

* A user can access only their own pets, foods, feeding records, and weight records.
* A feeding record must belong to an existing pet.
* A feeding record must use an existing food product.
* The selected pet and food product must belong to the authenticated user.
* Feeding amount must be greater than zero.
* Weight must be greater than zero when provided.
* A pet’s daily food target must be greater than zero.
* Weight is optional when creating a feeding entry.
* Feeding and optional weight must be saved in one transaction when submitted through one combined API request.
* If saving the optional weight fails, the feeding record must also be rolled back.

## ❕ Features excluded from MVP

The first version will not include:

* artificial intelligence;
* image uploads;
* a mobile or web frontend.

These features may be added in later versions.