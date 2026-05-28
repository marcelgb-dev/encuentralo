# Análisis de Código — Encuéntralo

> Generado por Opencode Agent tras escanear el proyecto completo.
> Fecha: 2026-05-28
> Archivos analizados: 34 Java, 5 HTML, 1 CSS, 1 properties

---

## 🔴 CRÍTICO — Bugs activos o vulnerabilidades

### 1. `ResultadoService.guardarResultados()` está vacío
**Archivo:** `src/main/java/app/senia/encuentralo/service/ResultadoService.java:34-36`
```java
public void guardarResultados(Integer idBusqueda, Integer idUsuario, List<Resultado> resultados) {
    // Lógica de guardado
}
```
`YelpService.llamarApi()` invoca este método para persistir los resultados obtenidos de la API de Yelp, pero el cuerpo está completamente vacío. Los resultados **nunca se guardan en base de datos**.

**Impacto:** Tras una búsqueda exitosa, la redirección a `/resultados/{id}` carga una `Busqueda` sin resultados persistidos. La vista se renderiza con lista vacía.

---

### 2. CSRF desactivado globalmente
**Archivo:** `src/main/java/app/senia/encuentralo/config/SecurityConfig.java:18`
```java
.csrf(AbstractHttpConfigurer::disable)
```
Spring Security tiene CSRF protección desactivada para todas las rutas. Los endpoints POST como `/favoritos/toggle/{id}`, `/buscar/nueva`, y cualquier formulario son vulnerables a ataques de falsificación de petición entre sitios.

**Impacto:** Un atacante podría engañar a un usuario autenticado para que ejecute acciones no deseadas (marcar/desmarcar favoritos, lanzar búsquedas, etc.).

---

### 3. Contraseñas en texto plano
**Archivo:** `src/main/java/app/senia/encuentralo/service/UsuarioService.java:41-42`
```java
if (!usuario.getPassword().equals(password)) {
    throw new Exception("Credenciales incorrectas.");
}
```
Las contraseñas se almacenan y comparan sin ningún tipo de hashing (bcrypt, argon2, PBKDF2...).

**Impacto:** Cualquier acceso a la tabla `Usuario` —por SQLi, acceso físico a BD, dump— expone credenciales reales de los usuarios.

---

### 4. `llamarApi()` retorna `null` sin manejo aguas arriba
**Archivo:** `src/main/java/app/senia/encuentralo/service/YelpService.java:89-90` y `BusquedaController.java:66`
```java
// YelpService.java
System.out.println("ERROR: Respuesta de Yelp vacía");
return null;

// BusquedaController.java
Busqueda busquedaGuardada = bs.guardarBusqueda(busqueda);  // NPE aquí
```
Si la API de Yelp falla (timeout de red, API key inválida, rate limit, respuesta vacía), el método devuelve `null`. El controlador no lo verifica y llama a `bs.guardarBusqueda(null)` → `NullPointerException`.

**Impacto:** Cualquier error de la API externa produce un 500 sin mensaje útil para el usuario. No hay fallback ni reintento.

---

### 5. `getReferenceById()` + `FetchType.LAZY` = `LazyInitializationException` potencial
**Archivo:** `src/main/java/app/senia/encuentralo/service/BusquedaService.java:31`
```java
return busquedaRepo.getReferenceById(busquedaId);
```
`ResultadosController.java:56` accede a `busqueda.getResultados()` fuera de la transacción original, y la colección `resultados` está configurada con `FetchType.LAZY` en `Busqueda.java:33`.

**Impacto:** Si `spring.jpa.open-in-view` está desactivado (recomendado en producción), se lanza `LazyInitializationException`. Actualmente funciona porque Spring Boot lo activa por defecto, pero es frágil.

---

## 🟠 ALTA — Bugs funcionales

### 6. `UsuarioService.editarUsuario()` y `obtenerListaUsuarios()` no implementados
**Archivo:** `src/main/java/app/senia/encuentralo/service/UsuarioService.java:48-54`
```java
public void editarUsuario(Integer usuarioId, Usuario usuario) {
    // Stub
}
public List<Usuario> obtenerListaUsuarios() {
    return null;
}
```
`AdminController.java:27` llama a `us.obtenerListaUsuarios()` (recibe `null`) y `AdminController.java:72` llama a `us.editarUsuario()`. La página de administración no funciona.

**Impacto:** Panel de administración completamente roto. La lista de usuarios no se muestra, y las ediciones/creaciones de usuario no persisten.

---

### 7. `EtiquetaService` completo vacío
**Archivo:** `src/main/java/app/senia/encuentralo/service/EtiquetaService.java:11-31`
Todos los métodos (`obtenerEtiquetasUsuario`, `obtenerEtiquetasResultado`, `guardarEtiqueta`, `borrarEtiqueta`, `etiquetarResultado`) son stubs vacíos o retornan `null`.

**Impacto:** El sistema de etiquetas no existe funcionalmente. Los modales de "Gestionar Etiquetas" en `resultados.html` y `favoritos.html` son solo HTML decorativo — ningún botón funciona.

---

### 8. Export CSV: posible NPE por campos null
**Archivo:** `src/main/java/app/senia/encuentralo/service/ExportService.java:41,46`
```java
r.getId().toString()           // NPE si id es null
r.getNumReviews().toString()   // NPE si numReviews es null
```
Si algún `Resultado` tiene `id` o `numReviews` como `null` (posible en resultados recién creados sin persistir), se lanza NPE al exportar.

**Impacto:** El botón "Exportar resultados" puede producir error 500 en lugar de descargar el CSV.

---

### 9. Input hidden `limite` sin valor en escritorio
**Archivo:** `src/main/resources/templates/busquedas.html:337`
```html
<input type="hidden" id="limite" th:field="*{limite}" placeholder="10">
```
El `placeholder` no funciona en `type="hidden"`. La versión móvil (línea 100) sí tiene `value="50"`. El escritorio envía `null`. Además, el objeto `SolicitudBusqueda` inicializado en `BusquedaController.java:42` usa `limite=50`, pero al hacer submit, Thymeleaf sobreescribe con el valor del input hidden (null).

**Impacto:** Búsquedas desde escritorio se envían con `limite=null` (0), devolviendo 0 resultados de Yelp.

---

### 10. `th:field` en hidden inputs + `required` bloquean el formulario
**Archivo:** `src/main/resources/templates/busquedas.html:98-99,335-336`
```html
<input type="hidden" ... th:field="*{latitud}" required>
<input type="hidden" ... th:field="*{longitud}" required>
```
Si el usuario no concede permiso de geolocalización y el JS de fallback no rellena estos campos (porque el usuario explícitamente los dejó vacíos tras denegar), HTML5 nativo bloquea el submit con un mensaje poco claro.

**Impacto:** El usuario puede quedar atrapado sin poder enviar el formulario, sin entender por qué.

---

## 🟡 MEDIA — Rendimiento y mantenibilidad

### 11. `System.out.println` generalizado
**Archivos:** `YelpService.java`, `BusquedaController.java`, `BusquedaService.java`
Decenas de `System.out.println()` para depuración. En producción:
- No hay niveles de log (debug/info/error).
- No rotan ni persisten.
- Mezclan datos sensibles (coordenadas, parámetros).

**Solución:** Usar `Logger` de SLF4J.

---

### 12. `obtenerCiudad()` ordena toda la lista para obtener el primer elemento
**Archivo:** `src/main/java/app/senia/encuentralo/service/YelpService.java:172-174`
```java
return resultadoService.ordenarPorDistancia(busqueda.getResultados(), false).getFirst().getCiudad();
```
Ordenar O(n log n) para obtener solo `getFirst()`. Innecesario cuando un simple min O(n) bastaría.

**Impacto:** En listas grandes (>1000 resultados), cada búsqueda ejecuta un sort completo solo para la ciudad de portada.

---

### 13. Bloque `:root` CSS duplicado
**Archivo:** `src/main/resources/static/css/style.css:1592-1597`
```css
:root {
  --color-primario: #091426;
  --color-superficie-alta: #e6e8ea;
  --color-borde-variante: #c5c6cd;
  --redondeado-completo: 9999px;
}
```
Re-declara variables que ya existen en `:root` (líneas 6-36). No causa bugs (CSS gana la última), pero si alguien modifica las de arriba y no las de abajo, o viceversa, crea inconsistencias.

---

### 14. Import no utilizado
**Archivo:** `src/main/java/app/senia/encuentralo/service/YelpService.java:10`
```java
import ch.qos.logback.core.net.SyslogOutputStream;
```
Import innecesario. No causa error de compilación (logback está en classpath por Spring Boot), pero es código muerto.

---

### 15. Nuevas categorías no se persisten explícitamente
**Archivo:** `src/main/java/app/senia/encuentralo/service/CategoriaService.java:63-64`
```java
Categoria categoriaFinal = categoriaRepo.findByNombre(nombre)
    .orElseGet(() -> new Categoria(nombre));
```
El objeto `Categoria` no se persiste con `categoriaRepo.save()`. Confía en `CascadeType.PERSIST` desde `Resultado.categorias`. Si el `Resultado` no se persiste (véase punto 1), la categoría se pierde sin errores.

---

### 16. Enlace "Panel" muerto en historial.html
**Archivo:** `src/main/resources/templates/historial.html:24`
```html
<a ... href="#">Panel</a>
```
Dentro del `nav.hidden.md:flex`. Enlace roto a `#`.

---

### 17. `idUsuario = 1` hardcodeado en tres lugares
**Archivos:** `BusquedaController.java:54,77`, `ResultadosController.java:176,228`
Sin autenticación ni sesión real, el sistema siempre opera como el usuario 1. El multiusuario no existe realmente.

---

## 🟢 BAJA — Calidad de código y UX

### 18. JS duplicado entre `resultados.html` y `favoritos.html`
El sistema de estrellas de valoración mínima, toggle de orden, y manejo de dropdowns de categorías está copiado casi idénticamente en ambas páginas (~50 líneas cada una). Refactorizar a un archivo `.js` compartido reduciría errores y facilitaría cambios.

### 19. Modal de etiquetas sin botón que lo abra
`resultados.html:557-582` y `favoritos.html:512-537` contienen un modal funcional completo, pero no hay ningún `onclick` ni listener en ninguna tarjeta que lo muestre. Código muerto.

### 20. Clases CSS `redondeado-*` con nombres desalineados de los tokens
```css
--redondeado-sm: 0.25rem;
--redondeado-md: 0.5rem;
--redondeado-lg: 0.75rem;
--redondeado-xl: 1rem;

.redondeado-lg { border-radius: var(--redondeado-md); }  /* 0.5rem, no 0.75rem */
.redondeado-xl { border-radius: var(--redondeado-lg); }  /* 0.75rem, no 1rem */
```
Los nombres de clase están desplazados un nivel respecto a los tokens. No causa bugs pero es confuso para nuevos desarrolladores.

### 21. `hh:mm a` (formato 12h) en aplicación española
**Archivo:** `historial.html:61`
```html
<span th:text="' - ' + ${#temporals.format(bus.fecha, 'hh:mm a')}">
```
AM/PM no es el formato habitual en España. Usar `HH:mm` (24h) sería más natural.

### 22. Sin tests unitarios
`src/test/` solo contiene `EncuentraloApplicationTests.java` con un test de contexto vacío. Servicios con lógica como `ResultadoService` (filtrado, ordenación) no tienen tests.

---

## Resumen de prioridades

| Prioridad | Ítems | Acción recomendada |
|-----------|-------|-------------------|
| 🔴 Crítico | 1-5 | Corregir antes de cualquier deploy |
| 🟠 Alto | 6-10 | Corregir antes de release |
| 🟡 Medio | 11-17 | Planificar para siguiente sprint |
| 🟢 Bajo | 18-22 | Mejora continua / tech debt |
