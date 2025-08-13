# FoodShare-SQL
# FoodShare Database

This repository contains the SQL schema and sample data for the **FoodShare** project, a food donation management system built using Java, JavaFX, JDBC, and MySQL.

## Database Overview

The database consists of two main tables:

### 1. `users`
Stores details of both donors and volunteers.
- **user_id**: Primary key
- **name**: User's full name
- **email**: Unique email for login
- **password**: Hashed password
- **mobile**: Contact number
- **user_type**: Either 'Donor' or 'Volunteer'

### 2. `donations`
Stores donation details and links donors with volunteers.
- **donation_id**: Primary key
- **food_item**: Name of the donated food
- **quantity**: Quantity description
- **location**: Pickup location
- **status**: 'Available' or 'Claimed'
- **donor_id**: Foreign key referencing `users(user_id)`
- **volunteer_id**: Foreign key referencing `users(user_id)` (nullable)
- **created_at**: Timestamp of donation

## Sample Data
The `INSERT` statements provide example donor, volunteer, and donation records to help test the application quickly.

## Usage
1. Create the database in MySQL:
   ```sql
   CREATE DATABASE foodshare;
   USE foodshare;
2.Run the foodshare_database.sql script to create tables and insert sample data.
3.Connect to the database in the Java application using JDBC.
