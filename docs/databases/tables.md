# Expense Management App — Database Tables (MVP)

For the MVP of the expense management application, these are the essential tables with their columns, data types, and constraints. The design is intended for PostgreSQL (used with Spring Boot + JPA).

---

## 1. `users` Table

Stores user account information and basic settings.

| Column           | Data Type       | Constraints                          | Description                                                                    |
|------------------|-----------------| ------------------------------------ |--------------------------------------------------------------------------------|
| `id`             | `BIGSERIAL`     | `PRIMARY KEY`                        | Unique user identifier.                                                        |
| `name`           | `VARCHAR(100)`  | `NOT NULL`                           | First name or nickname.                                                        |
| `email`          | `VARCHAR(255)`  | `NOT NULL, UNIQUE`                   | Email used for login.                                                          |
| `password`       | `VARCHAR(255)`  | `NOT NULL`                           | Password encrypted with BCrypt.                                                |
| `currency`       | `VARCHAR(3)`    | `NOT NULL DEFAULT 'USD'`             | ISO 4217 currency code (e.g., ARS, USD, MXN).                                  |
| `monthly_budget` | `DECIMAL(12,2)` | `DEFAULT NULL`                       | Total monthly expense budget. If NULL, it has not been defined.                |
| `tokens`         | `TEXT[]`        | `DEFAULT NULL`                       | Array of token hashes.                                                         |
| `created_at`     | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Account creation date.                                                         |
| `updated_at`     | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Last profile update (should be updated through a trigger or application code). |

---

## 2. `categories` Table

Contains expense/income categories. Default categories are seeded (`user_id = NULL`), and users may optionally create their own categories.

| Column       | Data Type     | Constraints                                       | Description                                                                  |
| ------------ | ------------- | ------------------------------------------------- | ---------------------------------------------------------------------------- |
| `id`         | `BIGSERIAL`   | `PRIMARY KEY`                                     | Unique category identifier.                                                  |
| `name`       | `VARCHAR(50)` | `NOT NULL`                                        | Category name (e.g., Food, Transportation).                                  |
| `icon`       | `VARCHAR(10)` | `NOT NULL DEFAULT '❓'`                            | Emoji or representative icon code (you may use a CSS class string).          |
| `user_id`    | `BIGINT`      | `NULLABLE REFERENCES users(id) ON DELETE CASCADE` | If NULL, it is a default category; otherwise, it belongs to a specific user. |
| `created_at` | `TIMESTAMP`   | `NOT NULL DEFAULT CURRENT_TIMESTAMP`              | Creation date.                                                               |

* **Suggested index**: `idx_categories_user_id` on `user_id` (to quickly retrieve user-specific and default categories).

---

## 3. `transactions` Table

Stores each individual expense or income transaction.

| Column             | Data Type       | Constraints                                                               | Description                                                                   |
|--------------------|-----------------|---------------------------------------------------------------------------|-------------------------------------------------------------------------------|
| `id`               | `BIGSERIAL`     | `PRIMARY KEY`                                                             | Unique transaction identifier.                                                |
| `amount`           | `DECIMAL(10,2)` | `NOT NULL, CHECK (amount > 0)`                                            | Amount in the user’s configured currency.                                     |
| `type`             | `VARCHAR(7)`    | `NOT NULL, CHECK (type IN ('INCOME','EXPENSE') Enums managed by the app)` | Transaction type.                                                             |
| `transaction_date` | `DATE`          | `NOT NULL`                                                                | Date when the expense/income occurred (defaults to current date on creation). |
| `note`             | `VARCHAR(255)`  | `NULLABLE`                                                                | Optional description.                                                         |
| `user_id`          | `BIGINT`        | `NOT NULL REFERENCES users(id) ON DELETE CASCADE`                         | Transaction owner.                                                            |
| `category_id`      | `BIGINT`        | `NOT NULL REFERENCES categories(id)`                                      | Associated category.                                                          |
| `created_at`       | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP`                                      | Exact time the transaction was registered in the system.                      |
| `updated_at`       | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP`                                      | Last modification date.                                                       |
| `deleted_at`       | `TIMESTAMP`     | `NULLABLE`                                                                | Deleted date (soft deletes).                                                  |

* **Recommended indexes**:

  * `idx_transactions_user_date` on `(user_id, transaction_date DESC)` → for monthly listings and dashboards.
  * `idx_transactions_user_category` on `(user_id, category_id)` → for category aggregations.
  * `idx_transactions_user_type` on `(user_id, type)` → for fast filtering.

---

## 4. `monthly_budgets` Table

Stores budgets for past periods and also allows defining the current month’s budget.

| Column         | Data Type       | Constraints                          | Description                              |
| -------------- | --------------- | ------------------------------------ | ---------------------------------------- |
| `id`           | `BIGSERIAL`     | `PRIMARY KEY`                        | Unique budget identifier.                |
| `user_id`      | `VARCHAR(50)`   | `NOT NULL`                           | Budget owner.                            |
| `budget_month` | `SMALLINT`      | `NOT NULL`                           | Month index corresponding to the budget. |
| `amount`       | `DECIMAL(12,2)` | `NOT NULL`                           | Registered monthly budget amount.        |
| `created_at`   | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Creation date.                           |
| `updated_at`   | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Last modification date.                  |

* **Suggested index**: `idx_monthly_budgets_user_month` on `(user_id, budget_month DESC)` → for monthly listings and dashboards.

---

## Additional Notes

* **Separate tables for category budgets or savings goals were not included** because the MVP only uses a global monthly budget (`monthly_budget` in `users`). This simplifies both the schema and the business logic.
* **The `categories` table with an optional `user_id`** allows starting with fixed global categories (`user_id = NULL`) and later enabling users to create their own categories without requiring migrations.
* **Exchange rates are not modeled yet** because we assume each user operates in a single currency (configured in their profile). Future multi-currency versions would require either an exchange rate table or storing currency information per transaction.
* **Automatic `updated_at` updates**: it is recommended to use a PostgreSQL trigger or handle it with `@PreUpdate` in JPA.

Do you also need the complete DDL creation scripts or the JPA entities mapped in Java code?
