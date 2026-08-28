# Palette

A full-stack online ordering system for Palette, a kitchen and juice bar — a customer-facing web app for browsing the menu and checking out with Stripe, and an admin dashboard for managing dishes, categories, combos, employees, and incoming orders in real time.

**Tech stack:** Spring Boot 2.7 · MySQL · MyBatis · Redis · WebSocket · Stripe · React + TypeScript (customer web) · Vue 3 + Element UI (admin dashboard)

## Screenshots

### Customer Web

| Menu | Dish customization | Checkout |
| --- | --- | --- |
| ![Customer menu page](./image/customer-page.png) | ![Dish customization](./image/customer-customization.png) | ![Shopping cart checkout](./image/shopping-cart.png) |

### Admin Dashboard

| Dishes | Categories | Combos |
| --- | --- | --- |
| ![Dish management](./image/dish-management.png) | ![Category management](./image/category-management.png) | ![Combo management](./image/combo-management.png) |

## Architecture

```
palette-server    Spring Boot REST API (admin + customer endpoints, JWT auth, WebSocket order notifications)
palette-pojo      Shared entities/DTOs/VOs
palette-common    Shared utilities, constants, exception handling
customer-web      React + TypeScript SPA — public ordering site
deploy/admin-frontend   Pre-built Vue 3 / Element UI SPA — staff dashboard
```

Customers place orders through `customer-web`, which pays via Stripe Checkout and talks to `palette-server` over `/user/**`. Staff manage the menu and fulfill orders through the admin dashboard, which talks to the same backend over `/admin/**` and gets pushed live order notifications over a WebSocket.

## Prerequisites

- JDK 11 (the build pins to this version via a Maven toolchain, regardless of your machine's default JDK)
- Maven
- Node.js 18+
- MySQL and Redis running locally (or reachable)
- A Stripe test account (for checkout) and, optionally, Aliyun OSS credentials (for image uploads)

## Running it locally

### 1. Database

```
mysql -u root -p < palette.sql
```

This creates the `palette` schema and tables. There's no seed admin account, so create one manually — the app hashes passwords with MD5, and `e10adc3949ba59abbe56e057f20f883e` is the MD5 hash of `123456`:

```sql
INSERT INTO employee (name, username, password, phone, sex, id_number, status)
VALUES ('Admin', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13800000000', '1', '110101199001010000', 1);
```

Log into the admin dashboard with username `admin` / password `123456`.

### 2. Backend (`palette-server`)

Register the JDK 11 toolchain once (needed for Lombok annotation processing to work correctly, even if JDK 11 isn't your default):

```
./scripts/setup-toolchain.sh          # macOS/Linux
scripts\setup-toolchain.ps1           # Windows (powershell -ExecutionPolicy Bypass -File ...)
```

Copy the config template and fill in your database, Redis, OSS, and Stripe credentials:

```
cp palette-server/src/main/resources/application-dev.yml.example palette-server/src/main/resources/application-dev.yml
```

Then build and run from the repo root (`install` first so `palette-server`'s sibling modules, `palette-common` and `palette-pojo`, are in the local repo):

```
mvn install -DskipTests
mvn spring-boot:run -pl palette-server
```

The API starts on `http://localhost:8080`. Interactive API docs (Knife4j/Swagger) are at `http://localhost:8080/doc.html`.

### 3. Customer Web (`customer-web`)

```
cd customer-web
npm install
npm run dev
```

Opens at `http://localhost:5173`, talking to the backend at the URL set in `.env.development`. Customers log in with just a phone number and name — no password or seed data required.

### 4. Admin Dashboard (`deploy/admin-frontend`)

This is a pre-built static bundle (the original Vue source project wasn't available to check in, so several bugfixes were patched directly into the compiled JS/CSS — see `deploy/README.md`). Serve it with any static file server that reverse-proxies API and WebSocket traffic to the backend; `deploy/nginx.conf.example` has a ready-to-use config:

```
nginx -c $(pwd)/deploy/nginx.conf.example -p $(pwd)/deploy
```

Opens at `http://localhost:8081`.

## Notes

- `application-dev.yml` and any real credentials are gitignored — only the `.example` template is committed.
- Payments run through Stripe's test mode; use [Stripe's test card numbers](https://stripe.com/docs/testing) at checkout.
