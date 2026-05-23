CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   currency VARCHAR(3) NOT NULL DEFAULT 1,
   monthly_budget DECIMAL(12,2) DEFAULT NULL,
   tokens TEXT[] DEFAULT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE monthly_budgets (
     id BIGSERIAL PRIMARY KEY,

     user_id BIGINT NOT NULL,

     budget_month SMALLINT NOT NULL
         CHECK (budget_month BETWEEN 1 AND 12),

     amount DECIMAL(12,2) NOT NULL
         CHECK (amount >= 0),

     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

     CONSTRAINT fk_monthly_budgets_user
         FOREIGN KEY (user_id)
             REFERENCES users(id)
             ON DELETE CASCADE,

     CONSTRAINT uq_monthly_budget_user_month
         UNIQUE (user_id, budget_month)
);

CREATE INDEX idx_monthly_budgets_user_month
    ON monthly_budgets(user_id, budget_month DESC);


CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(10) NOT NULL DEFAULT '❓',

    user_id BIGINT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_categories_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);

CREATE INDEX idx_categories_user_id
    ON categories(user_id);

CREATE TABLE transactions (
      id BIGSERIAL PRIMARY KEY,

      amount DECIMAL(10,2) NOT NULL
          CHECK (amount > 0),

      type VARCHAR(7) NOT NULL,

      transaction_date DATE NOT NULL,

      note VARCHAR(255),

      user_id BIGINT NOT NULL,
      category_id BIGINT NOT NULL,

      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      deleted_at TIMESTAMP NULL,

      CONSTRAINT fk_transactions_user
          FOREIGN KEY (user_id)
              REFERENCES users(id)
              ON DELETE CASCADE,

      CONSTRAINT fk_transactions_category
          FOREIGN KEY (category_id)
              REFERENCES categories(id)
);

CREATE INDEX idx_transactions_user_date
    ON transactions(user_id, transaction_date DESC);

CREATE INDEX idx_transactions_user_category
    ON transactions(user_id, category_id);

CREATE INDEX idx_transactions_user_type
    ON transactions(user_id, type);

-- =========================================================
-- TRIGGER FUNCTION FOR updated_at
-- =========================================================

-- CREATE OR REPLACE FUNCTION update_updated_at_column()
-- RETURNS TRIGGER AS $$
-- BEGIN
--    NEW.updated_at = CURRENT_TIMESTAMP;
-- RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--
-- -- =========================================================
-- -- TRIGGERS
-- -- =========================================================
--
-- CREATE TRIGGER trg_users_updated_at
--     BEFORE UPDATE ON users
--     FOR EACH ROW
--     EXECUTE FUNCTION update_updated_at_column();
--
-- CREATE TRIGGER trg_transactions_updated_at
--     BEFORE UPDATE ON transactions
--     FOR EACH ROW
--     EXECUTE FUNCTION update_updated_at_column();
--
-- CREATE TRIGGER trg_monthly_budgets_updated_at
--     BEFORE UPDATE ON monthly_budgets
--     FOR EACH ROW
--     EXECUTE FUNCTION update_updated_at_column();