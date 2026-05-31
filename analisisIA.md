# Análisis de Código — Encuéntralo

> Generado por Opencode Agent tras escanear el proyecto completo.
> Fecha: 2026-05-28 (actualizado: 2026-05-31)
> Archivos analizados: 34 Java, 5 HTML, 1 CSS, 1 properties

---

## ✅ RESUELTO — Bugs corregidos desde el análisis original

### Contraseñas en texto plano — CORREGIDO
`UsuarioService` ahora usa `BCryptPasswordEncoder`. Las contraseñas se almacenan hasheadas y se verifican con `passwordEncoder.matches()`.

### `UsuarioService.editarUsuario()` y `obtenerListaUsuarios()` no implementados — CORREGIDOS
Ambos métodos están implementados y funcionales.

### `EtiquetaService` completo vacío — CORREGIDO
Todos los métodos (`obtenerEtiquetasUsuario`, `guardarEtiqueta`, `borrarEtiqueta`, `etiquetarResultado`, `desetiquetarResultado`) están implementados.

### Sin tests unitarios — CORREGIDO
78 tests distribuidos en Service (Mockito), Repository (DataJpaTest) y Controller (WebMvcTest).

### 🔐 Eliminar usuario sin limpiar dependencias — CORREGIDO
`UsuarioService.eliminarUsuario()` borra etiquetas → búsquedas (cascade ALL a resultados) → usuario, en `@Transactional`. Además, el admin no puede eliminarse a sí mismo (lanza `ResponseStatusException` capturado por `error.html`).

### `llamarApi()` retornaba `null` sin manejo — CORREGIDO
`YelpService.llamarApi()` lanza `RuntimeException` si la API no devuelve resultados. `BusquedaController.nuevaBusqueda()` lo captura y redirige a `/buscar` con un mensaje de error visible en el formulario.

### Import `SyslogOutputStream` no utilizado — CORREGIDO
Eliminado de `YelpService.java`.

### `guardarResultados()` vacío — CÓDIGO MUERTO ELIMINADO
El método estaba vacío porque los resultados se persisten automáticamente vía `CascadeType.ALL` desde `Busqueda → Resultado`. Se ha eliminado el método y sus llamadas.

---

## 🔴 CRÍTICO — Bugs activos o vulnerabilidades

### 1. CSRF desactivado globalmente
**Archivo:** `src/main/java/app/senia/encuentralo/config/SecurityConfig.java:30`
```java
.csrf(AbstractHttpConfigurer::disable)
```
Spring Security tiene CSRF desactivado para todas las rutas. Endpoints POST como `/favoritos/toggle/{id}` o `/buscar/nueva` son vulnerables a falsificación de petición.

**Impacto:** Un atacante podría engañar a un usuario autenticado para que ejecute acciones no deseadas.

---

### 2. `getReferenceById()` + `FetchType.LAZY` = `LazyInitializationException` potencial
**Archivo:** `src/main/java/app/senia/encuentralo/service/BusquedaService.java:31`
```java
return busquedaRepo.getReferenceById(busquedaId);
```
`ResultadosController` accede a `busqueda.getResultados()` fuera de la transacción original con `FetchType.LAZY`.

**Impacto:** Si `spring.jpa.open-in-view` se desactiva, lanza `LazyInitializationException`. Actualmente funciona por el default de Spring Boot, pero es frágil.

---

## 🟠 ALTA — Bugs funcionales

### 3. Export CSV: posible NPE por campos null
**Archivo:** `src/main/java/app/senia/encuentralo/service/ExportService.java:41,46`
```java
r.getId().toString()           // NPE si id es null
r.getNumReviews().toString()   // NPE si numReviews es null
```

**Impacto:** El botón "Exportar resultados" puede producir error 500.

---

### 4. Input hidden `limite` sin valor en escritorio
**Archivo:** `src/main/resources/templates/busquedas.html:379`
```html
<input type="hidden" id="limite" th:field="*{limite}" placeholder="10">
```
El `placeholder` no funciona en `type="hidden"`. La versión móvil sí tiene `value="50"`. El escritorio envía `null`.

**Impacto:** Búsquedas desde escritorio se envían con `limite=null` (0 resultados).

---

### 5. `required` en hidden inputs de latitud/longitud bloquea el submit
**Archivo:** `src/main/resources/templates/busquedas.html:138-139,377-378`
```html
<input type="hidden" ... th:field="*{latitud}" required>
<input type="hidden" ... th:field="*{longitud}" required>
```
Si el usuario deniega geolocalización y el JS no rellena los campos, HTML5 bloquea el submit.

**Impacto:** El usuario puede quedar atrapado sin poder enviar el formulario.

---

## 🟡 MEDIA — Rendimiento y mantenibilidad

### 6. `System.out.println` generalizado
**Archivos:** `YelpService.java`, `BusquedaController.java`, `BusquedaService.java`
Decenas de `System.out.println()` sin niveles de log, rotación, ni persistencia.

**Solución:** Usar `Logger` de SLF4J.

---

### 7. `obtenerCiudad()` ordena toda la lista para obtener el primer elemento
**Archivo:** `src/main/java/app/senia/encuentralo/service/YelpService.java:161-163`
Ordenar O(n log n) para solo `getFirst()`. Un simple min O(n) bastaría.

---

### 8. Bloque `:root` CSS duplicado
**Archivo:** `src/main/resources/static/css/style.css:1592-1597`
Re-declara variables que ya existen en el `:root` principal (líneas 6-36). Crea posibles inconsistencias.

---

### 9. Nuevas categorías no se persisten explícitamente
**Archivo:** `src/main/java/app/senia/encuentralo/service/CategoriaService.java:63-64`
```java
categoriaRepo.findByNombre(nombre)
    .orElseGet(() -> new Categoria(nombre));
```
No hay `categoriaRepo.save()`. Confía en `CascadeType.PERSIST` desde `Resultado.categorias`.

---

### 10. Enlace "Panel" muerto en historial.html
**Archivo:** `src/main/resources/templates/historial.html:49`
```html
<a ... href="#">Panel</a>
```

---

### 11. `idUsuario` hardcodeado como fallback
**Archivo:** `src/main/java/app/senia/encuentralo/service/YelpService.java:141`
```java
usuario.setId(idUsuario != null ? idUsuario : 1);
```
En `llamarApiMock()`, si `idUsuario` es null se cae a `1`. Oculta el problema.

---

## 🟢 BAJA — Calidad de código y UX

### 12. JS duplicado entre `resultados.html` y `favoritos.html`
~50 líneas copiadas de lógica de estrellas, orden y dropdowns. Refactorizar a un `.js` compartido.

### 13. Modal de etiquetas sin botón que lo abra
El modal HTML es funcional pero no hay ningún `onclick` que lo muestre. Código muerto.

### 14. Clases CSS `redondeado-*` desalineadas de los tokens
```css
.redondeado-lg { border-radius: var(--redondeado-md); }  /* 0.5rem, no 0.75rem */
.redondeado-xl { border-radius: var(--redondeado-lg); }  /* 0.75rem, no 1rem */
```

### 15. `hh:mm a` (formato 12h) en aplicación española
**Archivo:** `historial.html:61`
Usar `HH:mm` (24h) sería más natural para España.

---

## Resumen de prioridades

| Prioridad | Ítems | Acción recomendada |
|-----------|-------|-------------------|
| ✅ Resuelto | Contraseñas, UsuarioService, EtiquetaService, tests, cascada usuario, null en llamarApi, import muerto, guardarResultados | Verificar en próxima iteración |
| 🔴 Crítico | 1-2 | Corregir antes de cualquier deploy |
| 🟠 Alto | 3-5 | Corregir antes de release |
| 🟡 Medio | 6-11 | Planificar para siguiente sprint |
| 🟢 Bajo | 12-15 | Mejora continua / tech debt |
