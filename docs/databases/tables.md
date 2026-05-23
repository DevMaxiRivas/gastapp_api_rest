Para el MVP de la aplicación de gestión de gastos, estas son las tablas esenciales con sus columnas, tipos de datos y restricciones. El diseño está pensado para PostgreSQL (usado con Spring Boot + JPA).

---

## 1. Tabla `users`

Almacena la información de la cuenta del usuario y su configuración básica.

| Columna          | Tipo de dato        | Restricciones                         | Descripción                                                                 |
|------------------|---------------------|---------------------------------------|-----------------------------------------------------------------------------|
| `id`             | `BIGSERIAL`         | `PRIMARY KEY`                         | Identificador único del usuario.                                            |
| `name`           | `VARCHAR(100)`      | `NOT NULL`                            | Nombre de pila o apodo.                                                     |
| `email`          | `VARCHAR(255)`      | `NOT NULL, UNIQUE`                    | Email usado para el inicio de sesión.                                       |
| `password`  | `VARCHAR(255)`      | `NOT NULL`                            | Contraseña cifrada con BCrypt.                                              |
| `currency`       | `VARCHAR(3)`        | `NOT NULL DEFAULT 'USD'`              | Código de moneda ISO 4217 (ej: ARS, USD, MXN).                             |
| `monthly_budget` | `DECIMAL(12,2)`     | `DEFAULT NULL`                        | Presupuesto mensual total de gastos. Si es NULL, no se ha definido.         |
| `created_at`     | `TIMESTAMP`         | `NOT NULL DEFAULT CURRENT_TIMESTAMP`  | Fecha de creación de la cuenta.                                             |
| `updated_at`     | `TIMESTAMP`         | `NOT NULL DEFAULT CURRENT_TIMESTAMP`  | Última actualización del perfil (se debe actualizar con un trigger o código). |

---

## 2. Tabla `categories`

Contiene las categorías de gasto/ingreso. Se siembran categorías por defecto (`user_id = NULL`) y opcionalmente el usuario puede crear las suyas propias.

| Columna      | Tipo de dato        | Restricciones                                      | Descripción                                                                 |
|--------------|---------------------|----------------------------------------------------|-----------------------------------------------------------------------------|
| `id`         | `BIGSERIAL`         | `PRIMARY KEY`                                      | Identificador único de la categoría.                                        |
| `name`       | `VARCHAR(50)`       | `NOT NULL`                                         | Nombre (ej: Alimentación, Transporte).                                      |
| `icon`       | `VARCHAR(10)`       | `NOT NULL DEFAULT '❓'`                            | Emoji o código de icono representativo (puedes usar un string de clase CSS).|
| `user_id`    | `BIGINT`            | `NULLABLE REFERENCES users(id) ON DELETE CASCADE`  | Si es NULL, es una categoría por defecto; si tiene valor, pertenece a ese usuario. |
| `created_at` | `TIMESTAMP`         | `NOT NULL DEFAULT CURRENT_TIMESTAMP`               | Fecha de creación.                                                          |

- **Índice sugerido**: `idx_categories_user_id` en `user_id` (para obtener rápido las categorías de un usuario + las por defecto).

---

## 3. Tabla `transactions`

Registra cada gasto o ingreso individual.

| Columna           | Tipo de dato    | Restricciones                                         | Descripción                                                                 |
|-------------------|-----------------|-------------------------------------------------------|-----------------------------------------------------------------------------|
| `id`              | `BIGSERIAL`     | `PRIMARY KEY`                                         | Identificador único de la transacción.                                      |
| `amount`          | `DECIMAL(10,2)` | `NOT NULL, CHECK (amount > 0)`                        | Monto en la moneda configurada por el usuario.                              |
| `type`            | `SMALLINT`      | `NOT NULL, CHECK (type IN ('INCOME','EXPENSE') Enums managed by the app)` | Tipo de movimiento.                                                         |
| `transaction_date`| `DATE`          | `NOT NULL`                                            | Fecha en que ocurrió el gasto/ingreso (por defecto hoy al crear).           |
| `note`            | `VARCHAR(255)`  | `NULLABLE`                                            | Descripción opcional.                                                       |
| `user_id`         | `BIGINT`        | `NOT NULL REFERENCES users(id) ON DELETE CASCADE`     | Dueño de la transacción.                                                    |
| `category_id`     | `BIGINT`        | `NOT NULL REFERENCES categories(id)`                  | Categoría asociada.                                                         |
| `created_at`      | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP`                  | Momento exacto en que se registró en el sistema.                            |
| `updated_at`      | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP`                  | Última edición.                                                             |

- **Índices recomendados**:
    - `idx_transactions_user_date` en `(user_id, transaction_date DESC)` → para listados mensuales y dashboard.
    - `idx_transactions_user_category` en `(user_id, category_id)` → para agregaciones por categoría.
    - `idx_transactions_user_type` en `(user_id, type)` → para filtros rápidos.

---

## 4. Tabla `monthly_budgets`

Mantiene el presupuesto de cualquier período pasado y también establecer el del mes en curso

| Columna              | Tipo de dato | Restricciones                       | Descripción                                      |
|----------------------|--------------|-------------------------------------|--------------------------------------------------|
| `id`                 | `BIGSERIAL`  | `PRIMARY KEY`                       | Identificador único del presupuesto.             |
| `user_id`            | `VARCHAR(50)` | `NOT NULL`                          | Dueño del presupuesto.                           |
| `budget_month`       | `SMALLINT`   | `NOT NULL`               | Indice del Mes al que corresponde el presupuesto |
| `amount`             | `DECIMAL(12,2)`     | `NOT NULL`                          | Presupuesto del mes registrado.                  |
| `created_at`         | `TIMESTAMP`  | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Fecha de creación.                               |
| `updated_at`      | `TIMESTAMP`  | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Última edición.                                  |

- **Índice sugerido**: `idx_monthly_budgets_user_month` en `(user_id, budget_mont DESC)` → para listados mensuales y dashboard.

---


## 📌 Observaciones adicionales

- **No incluí tablas separadas para presupuestos por categoría ni metas de ahorro** porque el MVP usa solo un presupuesto global mensual (`monthly_budget` en `users`). Esto simplifica el esquema y la lógica.
- **La tabla `categories` con `user_id` opcional** te permite empezar con categorías globales fijas (user_id NULL) y después, sin migraciones, permitir que los usuarios agreguen las suyas.
- **El tipo de cambio no se modela aún** porque asumimos que cada usuario opera en una sola moneda (la que configura en su perfil). Para futuras versiones multimoneda, se necesitaría una tabla de tasas o registrar moneda por transacción.
- **Actualización automática de `updated_at`**: recomiendo usar un trigger en PostgreSQL o manejarlo con `@PreUpdate` en JPA.

¿Necesitas los scripts DDL completos de creación o las entidades JPA mapeadas en código Java?