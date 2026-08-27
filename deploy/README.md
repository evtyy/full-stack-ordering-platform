# Admin Frontend (pre-built)

`admin-frontend/` is a compiled Vue/Element UI single-page app for managing
dishes, categories, combos, employees, and orders. It's a production build —
minified JS/CSS with hashed filenames — because the original Vue source
project isn't available separately from the build output it shipped with.

Several fixes were made directly against the compiled bundle, since there's
no source to rebuild from:

- fixed dish/category/combo name validation to allow longer names and spaces
- fixed the orders-page tab bar wrapping onto two lines
- fixed an order fee breakdown bug tied to dish/combo flavor customization

## Running it locally

Serve `admin-frontend/` with any static file server and reverse-proxy API
calls to the Spring Boot backend (`palette-server`). `nginx.conf.example` in this
folder shows a minimal config: static files at `/`, `/api/` and `/user/`
proxied to the backend, and `/ws/` proxied for the order-notification
WebSocket.

```
nginx -c $(pwd)/deploy/nginx.conf.example -p $(pwd)/deploy
```

(or point any nginx/Caddy/etc. config at the same routes).
