# Guía Técnica — Encuéntralo

## 1. Información general

Aplicación web de prospección comercial que busca negocios via API de Yelp Fusion, los almacena en MySQL y permite filtrar, ordenar, marcar como favoritos, etiquetar y exportar resultados. Panel de administración para gestión de usuarios.

- **Artefacto:** `app.senia:encuentralo:0.0.1-SNAPSHOT`
- **Java:** 21
- **Spring Boot:** 4.0.6
- **Build:** Maven (wrapper `./mvnw`)
- **BD:** MySQL puerto `5555`, BBDD `Encuentralo`, user `root`, pass `1234`
- **CSS:** Archivo único `src/main/resources/static/css/style.css`

---

## 2. Stack tecnológico

| Componente | Tecnología |
|---|---|
| Backend | Spring Boot 4, Spring MVC, Spring Data JPA, Spring Security 6 |
| Frontend | Thymeleaf, HTML, CSS plano (sin framework), Material Symbols |
| BD | MySQL (JPA/Hibernate, `ddl-auto: none`) |
| API externa | Yelp Fusion API v3 (mockeable con `mock.json=true`) |
| CSV | OpenCSV 5.9 |
| Tests | Spring Boot test slices (JPA, MVC, Security, Thymeleaf) |

---

## 3. Estructura del proyecto (Java)

```
src/main/java/app/senia/encuentralo/
├── config/
│   ├── CustomLoginSuccessHandler.java   — Redirige según rol tras login
│   ├── GlobalModelAdvice.java           — Inyecta nombreUsuario e inicialUsuario en todas las vistas
│   ├── SecurityConfig.java              — Cadenas de seguridad, BCrypt, rutas públicas/restringidas
│   └── YelpConfig.java                  — Cliente REST para Yelp API
├── controller/
│   ├── AdminController.java             — CRUD usuarios (admin)
│   ├── BusquedaController.java          — Página de búsqueda, historial
│   ├── CustomErrorController.java       — Página de error personalizada
│   ├── EtiquetaController.java          — CRUD y asignación de etiquetas
│   ├── ResultadosController.java        — Resultados, favoritos, filtros, ordenación, CSV
│   └── UsuarioController.java           — Login, registro, cambio contraseña
├── dto/
│   ├── SolicitudBusqueda.java           — Formulario de búsqueda
│   └── yelp/ (BusinessDTO, CategoryDTO, CoordinatesDTO, LocationDTO, YelpResponse)
├── model/
│   ├── Busqueda.java                    — Búsqueda realizada por un usuario
│   ├── Categoria.java                   — Categoría de negocio (ManyToMany con Resultado)
│   ├── Etiqueta.java                    — Etiqueta personal del usuario (ManyToMany con Resultado)
│   ├── Resultado.java                   — Resultado de negocio de Yelp
│   └── Usuario.java                     — Usuario de la aplicación
├── repository/                          — Interfaces Spring Data JPA
│   ├── BusquedaRepository.java
│   ├── CategoriaRepository.java
│   ├── EtiquetaRepository.java
│   ├── ResultadoRepository.java
│   └── UsuarioRepository.java
├── service/
│   ├── BusquedaService.java             — Historial y persistencia de búsquedas
│   ├── CategoriaService.java            — Deduplicación de categorías
│   ├── CustomUserDetailsService.java    — Bridge Spring Security ↔ MySQL
│   ├── EtiquetaService.java             — CRUD etiquetas, asignar/desasignar
│   ├── ExportService.java               — Exportación CSV
│   ├── ProviderService.java             — Interfaz de proveedor externo
│   ├── ResultadoService.java            — Lógica de filtrado y ordenación
│   ├── UsuarioService.java              — CRUD usuarios, cambio contraseña
│   └── YelpService.java                 — Integración Yelp API (o mock)
└── EncuentraloApplication.java          — Entry point
```

---

## 4. Base de datos

| Tabla | PK | FK | Notas |
|---|---|---|---|
| `Usuario` | `id_usuario` | — | `rol` = `USER` o `ADMIN` |
| `Busqueda` | `id_busqueda` | `id_usuario` | `termino_busqueda`, `fecha_busqueda`, `ciudad` |
| `Resultados` | `id_resultado` | `id_usuario`, `id_busqueda` | Datos de Yelp + `esFavorito` |
| `Categoria` | `id_categoria` | — | `nombre_categoria` |
| `Etiqueta` | `id_etiqueta` | `id_usuario` | `nombre` |
| `Resultados_Categoria` | — | `id_resultado`, `id_categoria` | Join N:M |
| `Etiqueta_Resultados` | — | `id_resultado`, `id_etiqueta` | Join N:M |

**IMPORTANTE:** `ddl-auto: none`. El esquema se gestiona externamente (SQL script o manual).

---

## 5. Vistas (templates Thymeleaf)

| Template | Ruta | Propósito |
|---|---|---|
| `frontend/login.html` | `GET /login` | Login |
| `busquedas.html` | `GET /buscar` | Formulario de búsqueda |
| `resultados.html` | `GET /resultados/{id}` | Resultados con filtros, ordenación, etiquetas |
| `favoritos.html` | `GET /favoritos` | Favoritos (misma estructura que resultados) |
| `historial.html` | `GET /historial` | Historial de búsquedas |
| `panel_admin.html` | `GET /admin` | Panel de administración de usuarios |
| `registro.html` | `GET /registrar_usuario` | Registro de usuarios (admin) |
| `editar_usuario.html` | `GET /editar_usuario` | Editar usuario (admin) |
| `password.html` | `GET /cambiar_contrasena` | Cambiar contraseña |
| `error.html` | `/error` | Página de error personalizada |

### 5.1. Patrón de doble vista (móvil / escritorio)

Cada template con vistas móvil y escritorio sigue esta estructura:

```html
<div class="vista-movil">
  <!-- Cabecera móvil, contenido, barra inferior fija -->
</div>
<div class="vista-escritorio">
  <!-- Barra lateral, cabecera superior, contenido -->
</div>
<!-- Modales globales (fuera de ambas vistas) -->
```

CSS controla qué vista se muestra según `@media`:
- Por defecto (móvil): `.vista-movil { display: block; } .vista-escritorio { display: none; }`
- A partir de 768px: `.vista-movil { display: none; } .vista-escritorio { display: block; }`

### 5.2. Modelos Thymeleaf comunes (inyectados por GlobalModelAdvice)

- `${nombreUsuario}` — Nombre completo del usuario autenticado
- `${inicialUsuario}` — Primera letra del nombre (para avatar)

---

## 6. Flujos principales

### 6.1. Búsqueda
1. `GET /buscar` → formulario con término, radio (range slider), geolocalización
2. `POST /buscar/nueva` → `YelpService.llamarApi()` → guarda resultados → redirect a resultados
3. `GET /resultados/{id}` → muestra resultados con filtros y ordenación

### 6.2. Filtros (GET parameters)
Los filtros se envían como query params GET y se procesan en `ResultadosController`:

| Parámetro | Tipo | Descripción |
|---|---|---|
| `categorias` | `List<String>` | Filtro OR por categorías |
| `etiquetas` | `List<String>` | Filtro OR por etiquetas |
| `soloFavoritos` | `boolean` | Solo favoritos |
| `valoracionMinima` | `int` | Valoración mínima (0-5) |
| `orden` | `String` | `default`, `valoracion`, `distancia`, `alfabeticamente` |
| `inverso` | `boolean` | Invertir orden |

**IMPORTANTE:** El filtro de categorías y etiquetas usa lógica **OR** via `Collections.disjoint()` en `ResultadoService`.

### 6.3. Ordenación (JS + formulario GET)
- Móvil: `document.querySelector('#contenedor-filtros-movil form').submit()`
- Escritorio: `document.querySelector('aside.filtros-laterales-contenedor form').submit()`

**⚠️ ATENCIÓN:** El selector de escritorio debe usar `aside.filtros-laterales-contenedor form`. Usar solo `aside form` es un bug conocido porque la barra lateral (`<aside class="barra-lateral">`) contiene el formulario de logout y aparece primero en el DOM.

### 6.4. Gestión de etiquetas (modal)
Cada tarjeta de resultado/favorito tiene un botón `etiqueta-sell-btn` con atributos `data-res-id` y `data-res-nombre`. Al hacer clic:

1. `abrirGestionEtiquetas(btn)` — abre el modal, lee las etiquetas de la tarjeta y muestra/oculta el botón "Quitar" según corresponda
2. El modal (`#modal-gestion-etiquetas`) está fuera de `.vista-movil` y `.vista-escritorio`
3. Tres botones por etiqueta: Añadir (verde +), Quitar (naranja −, solo si ya asignada), Borrar (rojo papelera, con confirmación modal)

### 6.5. Exportación CSV
- Endpoints: `/resultados/{id}/exportar/csv` y `/favoritos/exportar/csv`
- Respeta los mismos filtros y ordenación
- Usa OpenCSV, incluye BOM UTF-8

---

## 7. Seguridad

- CSRF desactivado
- BCrypt para contraseñas
- Rutas públicas: `/login`, `/css/**`, `/js/**`
- Requieren `ROLE_ADMIN`: `/admin/**`, `/registrar_usuario`, `/editar_usuario`, `/eliminar_usuario`
- Todo lo demás requiere autenticación
- Login redirige: ADMIN → `/admin`, USER → `/buscar`

---

## 8. CSS — Sistema de diseño

Archivo único `style.css` (~1784 líneas) con:

- **Tokens CSS** en `:root` (colores, sombras, radios, fuente)
- **Utilidades** tipo utility-first: `.flex`, `.gap-*`, `.px-*`, `.mt-*`, `.texto-*`, `.bg-*`
- **Componentes:** botones (`.boton-*`), tarjetas (`.tarjeta-*`), modales (`.modal-personalizado-*`), filtros (`.selector-filtros-*`, `.filtro-*`), etiquetas (`.etiqueta-*`)
- **Modales:** `.modal-personalizado-overlay` (gestión etiquetas), `.modal-overlay` / `.modal-box` (confirmación borrar)
- **Media queries:** 768px, 1024px, 1280px, 1536px
- **Sin framework CSS externo**

