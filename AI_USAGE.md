# Uso de IA

## Herramientas y en qué se usaron

- **Claude (Anthropic)** — Persona 1: diseño de paquetes, BFS y DFS iterativo,
  lectura de la entrada, interfaz Swing y configuración de Maven.
- (Persona 2: completar)
- (Persona 3: completar)

Todo el código fue revisado, corregido y probado por los integrantes.

## Prompts decisivos

1. **DFS iterativo con orden arriba, abajo, izquierda, derecha.** Hubo que pedirlo
   explícitamente: la implementación obvia recorre las direcciones en orden directo
   y, al ser la pila LIFO, eso hace que se procese "derecha" primero. Con el orden
   equivocado el resultado es un camino válido pero de longitud distinta a la esperada.

2. **Separación entre algoritmos e interfaz.** Se pidió un diseño donde el paquete de
   algoritmos no pudiera importar Swing, para cumplir la sección 7.2. De ahí salieron
   la interfaz `Mission` y la clase `MissionOutcome`.

3. **Representar "no hay ruta" sin hacer aritmética sobre ese valor**, como prohíbe la
   sección 2.1. De ahí salió la clase `Sentinels` con `safeAdd`.

## Casos donde la salida generada estuvo mal

1. **`parent[cur] = neighbor` invertido en el BFS.** Debía ser `parent[neighbor] = cur`.
   Compilaba sin advertencias, pero dejaba ciclos en el arreglo de padres y el
   `while (cursor != -1)` de `buildPath` nunca terminaba: la aplicación se congelaba.
   Se corrigió entendiendo que `parent[X]` es la celda desde la cual se llegó a `X`.

2. **`while (head < total)` en vez de `while (head < tail)` en el BFS.** Usaba el número
   total de celdas en lugar de cuántas hay en la cola, y procesaba posiciones del
   arreglo que nunca se llenaron. No se nota cuando el destino es alcanzable, pero da
   resultados incorrectos en mapas sin solución.

3. **`NO_ROUTE` y `UNBOUNDED` con el mismo valor `Long.MAX_VALUE`.** Al ser el mismo
   número, el programa no podía distinguir "no hay ruta" de "infinito", lo que habría
   roto la Misión 3. Se corrigió dejando `NO_ROUTE = Long.MIN_VALUE`: como en esa misión
   se maximiza, el valor que representa "sin respuesta" debe perder contra cualquier
   resultado real.

(Personas 2 y 3: agregar los suyos)

## Qué aprendió cada integrante

**Persona 1 (Miguel Gómez Tobón).** La diferencia práctica entre BFS y DFS sobre el
mismo grafo: recorren las mismas celdas, pero BFS es óptimo en un grafo sin pesos porque
avanza por niveles, mientras el DFS llega a Nina en 32 movimientos en vez de 18. También
cómo reemplazar la recursión por una pila explícita y por qué aquí es obligatorio: con
un millón de celdas, un DFS recursivo desborda la pila de la JVM.

(Persona 2: completar)

(Persona 3: completar)