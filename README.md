# Koha Simulator API

Monolito Spring Boot 4 / Java 25 que simula los endpoints REST de Koha ILS necesarios para **login** y **autopréstamo**. Diseñado para desplegarse como Azure Web App con MySQL.

---

## Stack

| Capa | Tecnología |
|---|---|
| Runtime | Java 25 |
| Framework | Spring Boot 4.0.4 |
| Seguridad | Spring Security 7 + JWT (JJWT 0.12.6) |
| Persistencia | Spring Data JPA + MySQL |
| Migraciones | Flyway |
| Docs interactivas | SpringDoc OpenAPI 2.7 / Swagger UI |

---

## Arranque rápido

```bash
# 1. Crear base de datos
mysql -u root -p -e "CREATE DATABASE koha_simulator CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 2. Variables de entorno (o editar application.yml)
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=koha_simulator
export DB_USER=root
export DB_PASSWORD=tu_password
export JWT_SECRET=CambiaEstoEnProduccionMinimo32Caracteres

# 3. Ejecutar
mvn spring-boot:run
```

Flyway crea el esquema e inserta datos de prueba automáticamente en el primer arranque.

**Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## Autenticación

La API usa **JWT Bearer token**. El flujo es:

1. Llamar a `POST /api/v1/auth/session` con `userid` y `password`
2. El servidor devuelve el token en el cuerpo (`token`) **y** en la cookie `CGISESSID` (compatibilidad Koha)
3. Incluir el token en todas las llamadas siguientes:
   ```
   Authorization: Bearer <token>
   ```

Los tokens expiran en **8 horas** (configurable con `JWT_EXPIRATION_HOURS`).

---

## Endpoints

### Authentication

#### `POST /api/v1/auth/session` — Login

**Request:**
```json
{
  "userid": "koha_library",
  "password": "password123"
}
```

**Response `200 OK`:**
```json
{
  "patron_id": 1,
  "cardnumber": "LIB0000001",
  "surname": "García",
  "firstname": "Ana",
  "email": "ana.garcia@biblioteca.edu",
  "categorycode": "PT",
  "branchcode": "CPL",
  "flags": 0,
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Errores:**

| Status | Código | Descripción |
|--------|--------|-------------|
| `401` | `UNAUTHORIZED` | Credenciales incorrectas |
| `403` | `FORBIDDEN` | Cuenta desactivada |

---

#### `DELETE /api/v1/auth/session` — Logout

Limpia la cookie `CGISESSID`. No requiere cuerpo.

**Response `204 No Content`**

---

### Patrons

> Todos los endpoints requieren `Authorization: Bearer <token>`

#### `GET /api/v1/patrons/{patron_id}` — Obtener patron

**Response `200 OK`:**
```json
{
  "patron_id": 1,
  "cardnumber": "LIB0000001",
  "userid": "koha_library",
  "firstname": "Ana",
  "surname": "García",
  "email": "ana.garcia@biblioteca.edu",
  "phone": "809-555-0001",
  "address": "Santiago, RD",
  "categorycode": "PT",
  "branchcode": "CPL",
  "flags": 0,
  "active": true,
  "date_enrolled": "2024-01-15",
  "expiry_date": "2026-01-15"
}
```

---

#### `GET /api/v1/patrons/{patron_id}/checkouts` — Préstamos activos del patron

**Response `200 OK`:** array de `CheckoutResponse` (ver sección Checkouts).

---

### Items

#### `GET /api/v1/items?external_id={barcode}` — Buscar ítem por código de barras

El parámetro `external_id` es el barcode del ítem (nomenclatura Koha).

**Response `200 OK`:**
```json
[
  {
    "item_id": 1,
    "biblio_id": 1,
    "external_id": "9780000000001",
    "barcode": "9780000000001",
    "location": "STACKS",
    "callnumber": "005.133 JAV",
    "itype": "BK",
    "branchcode": "CPL",
    "available": true,
    "damaged": false,
    "lost": false,
    "withdrawn": false
  }
]
```

Si no se pasa `external_id` devuelve `[]`.

---

#### `GET /api/v1/items/{item_id}` — Obtener ítem por ID

Misma estructura que el objeto anterior.

---

### Biblios

#### `GET /api/v1/biblios/{biblio_id}` — Obtener registro bibliográfico

**Response `200 OK`:**
```json
{
  "biblio_id": 1,
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "isbn": "978-0-13-468599-1",
  "publisher": "Addison-Wesley",
  "publication_year": 2018,
  "language": "en"
}
```

---

### Checkouts (Autopréstamo)

#### `POST /api/v1/checkouts` — Crear préstamo

**Request:**
```json
{
  "patron_id": 1,
  "item_id": 1,
  "library_id": "CPL",
  "due_date": "2026-04-16T23:59:00"
}
```

`library_id` y `due_date` son opcionales. Si no se pasa `due_date`, se asignan **21 días** a partir de hoy (configurable con `DEFAULT_LOAN_DAYS`).

**Response `201 Created`:**
```json
{
  "checkout_id": 1,
  "patron_id": 1,
  "item_id": 1,
  "biblio_id": 1,
  "library_id": "CPL",
  "checkout_date": "2026-03-26T10:00:00",
  "due_date": "2026-04-16T23:59:00",
  "return_date": null,
  "renewals_count": 0,
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "barcode": "9780000000001"
}
```

**Errores:**

| Status | Descripción |
|--------|-------------|
| `400` | Patron inactivo, ítem no disponible, datos inválidos |
| `404` | Patron o ítem no encontrado |

---

#### `GET /api/v1/checkouts/{checkout_id}` — Obtener préstamo por ID

Misma estructura que `CheckoutResponse`.

---

#### `DELETE /api/v1/checkouts/{checkout_id}` — Devolver ítem (por checkout ID)

**Response `200 OK`:** `CheckoutResponse` con `return_date` poblado.

---

#### `POST /api/v1/returns` — Devolver ítem por código de barras

**Request:**
```json
{
  "barcode": "9780000000001",
  "library_id": "CPL"
}
```

`library_id` es opcional.

**Response `200 OK`:** `CheckoutResponse` con `return_date` poblado.

---

## Flujo de autopréstamo completo

```
1. POST  /api/v1/auth/session          → obtener token
2. GET   /api/v1/items?external_id=XXX → buscar ítem por barcode
3. POST  /api/v1/checkouts             → registrar préstamo
4. GET   /api/v1/patrons/{id}/checkouts → ver préstamos activos
5. POST  /api/v1/returns               → devolver por barcode
```

---

## Variables de entorno

| Variable | Default | Descripción |
|----------|---------|-------------|
| `PORT` | `8080` | Puerto del servidor |
| `DB_HOST` | `localhost` | Host MySQL |
| `DB_PORT` | `3306` | Puerto MySQL |
| `DB_NAME` | `koha_simulator` | Nombre de la base de datos |
| `DB_USER` | `root` | Usuario MySQL |
| `DB_PASSWORD` | — | Contraseña MySQL |
| `JWT_SECRET` | *(dev key)* | Clave HMAC-SHA256, mínimo 32 chars |
| `JWT_EXPIRATION_HOURS` | `8` | Expiración del token en horas |
| `DEFAULT_LOAN_DAYS` | `21` | Días de préstamo por defecto |
| `SPRING_PROFILES_ACTIVE` | — | `azure` para producción |

---

## Despliegue en Azure

```bash
# Build
mvn clean package -DskipTests

# Deploy al Web App
az webapp deploy \
  --name <app-name> \
  --resource-group <rg> \
  --src-path target/koha-simulator.jar \
  --type jar
```

Configurar en **Azure Portal → Web App → Configuration → Application settings** las variables de entorno listadas arriba, con `SPRING_PROFILES_ACTIVE=azure`.

---

## Datos de prueba

Flyway inserta automáticamente en `V2__sample_data.sql`:

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `koha_library` | `password123` | Admin (flags=1) |
| `patron01` | `password123` | Patron normal |
| `patron02` | `password123` | Patron normal |
