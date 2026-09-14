# The Feline Graph Chronicles

Proyecto del curso **Lenguajes y Compiladores** — Universidad EIA.
Aplicación de escritorio en Java 17 con interfaz Swing que resuelve cuatro
misiones sobre grafos, implementando seis algoritmos desde cero.

## Integrantes

| Persona | Nombre | Responsabilidad |
|---------|--------|-----------------|
| Persona 1 | Miguel Gómez Tobón | Núcleo, Misión 1 (BFS y DFS), interfaz gráfica, repositorio |
| Persona 2 | Alejandro Sanchez | Misión 3 (Floyd-Warshall, Bellman-Ford, verificación cruzada) |
| Persona 3 | Juan Jose Diaz | Misión 2 (Dijkstra), Misión 4 (Kruskal), dibujo de grafos |

## Cómo compilar y ejecutar

Un solo comando desde un clon limpio:

    mvn clean package && java -jar target/feline-graph-chronicles.jar

Requisitos: JDK 17 o superior y Maven 3.8+.

También se puede abrir la carpeta en IntelliJ IDEA y ejecutar la clase `ui.Main`.

## Estructura del proyecto

    src/
    ├── core/            Modelo de datos, sin dependencias externas
    │   ├── Graph.java          Lista de adyacencia + lista plana de aristas
    │   ├── Edge.java           Arista con peso en long
    │   ├── GridMap.java        Grilla de la Misión 1 (arreglo plano de bombas)
    │   ├── PathResult.java     Movimientos + camino que devuelven BFS y DFS
    │   └── Sentinels.java      Centinelas NO_ROUTE y UNBOUNDED
    │
    ├── io/              Lectura de la entrada, un parser por misión
    │   ├── TokenStream.java            Flujo de tokens separados por espacios
    │   ├── InputFormatException.java   Error legible para mostrar en la ventana
    │   ├── MinefieldReader.java        Misión 1
    │   ├── AccountsReader.java         Misión 2
    │   ├── StashReader.java            Misión 3
    │   └── NetworkReader.java          Misión 4
    │
    ├── algoritmos/      Los seis algoritmos. Sin imports de Swing.
    │   ├── search/      BreadthFirstSearch, IterativeDepthFirstSearch
    │   ├── shortest/    Dijkstra
    │   ├── maxwalk/     FloydWarshall, BellmanFord, CrossCheck
    │   └── mst/         Kruskal, UnionFind
    │
    ├── mission/         Coordina parser → algoritmo → línea "Case #k: ..."
    │   ├── Mission.java            Interfaz común de las cuatro misiones
    │   ├── MissionOutcome.java     Líneas de salida + datos de dibujo
    │   └── *Mission.java           Una por misión
    │
    └── ui/              Interfaz gráfica (Swing). Único paquete con javax.swing.
        ├── Main.java               Punto de entrada
        ├── ChroniclesWindow.java   Ventana y selector de misiones
        ├── MissionPanel.java       Entrada, salida, botones, manejo de errores
        ├── Theme.java              Colores y tipografías
        └── draw/                   Canvas de dibujo por tipo de misión

## Decisiones tomadas

**DFS iterativo con pila explícita, no con hilo de pila ampliada.**
El enunciado permite ambas. Se eligió la pila explícita porque no depende de
configurar el tamaño de pila de la JVM y funciona igual en cualquier máquina.
La pila son tres arreglos `int[]` que avanzan juntos: celda, profundidad y padre.

**Orden de expansión del DFS: arriba, abajo, izquierda, derecha.**
Como la pila es LIFO, los vecinos se empujan en orden inverso (derecha,
izquierda, abajo, arriba) para que "arriba" salga primero. Con la muestra del
enunciado esto produce 32 movimientos frente a los 18 del BFS.

**Grilla como arreglo plano `boolean[]` de tamaño R×C, no como matriz `[][]`.**
Con el máximo de 1000×1000 un `boolean[][]` crearía 1000 objetos-arreglo
separados. Además, al identificar cada celda con un solo entero
(`fila * C + columna`), la cola del BFS y la pila del DFS pueden ser arreglos
`int[]` en lugar de listas de objetos, sin autoboxing.

**Cola y pila implementadas con arreglos, no con `LinkedList` ni `ArrayDeque`.**
En BFS cada celda entra a la cola como máximo una vez, así que un arreglo de
tamaño R×C nunca se desborda y no necesita ser circular.

**Centinelas en lugar de excepciones o valores nulos.**
`NO_ROUTE` es el valor mínimo y `UNBOUNDED` el máximo, deliberadamente
distintos: en la Misión 3 se maximiza, así que "sin ruta" debe ser peor que
cualquier resultado real y "no acotado" mejor que cualquiera. La clase
`Sentinels` expone `safeAdd`, que impide la aritmética sobre ellos que prohíbe
la sección 2.1.

**Entrada leída como flujo de tokens, no línea por línea.**
Lo exige la sección 2.2 y es necesario: en la Misión 1 la cantidad de números
por línea depende de cuántas bombas tenga cada fila. El precio es que un token
faltante desplaza toda la lectura, por lo que los mensajes de error incluyen la
posición del token conflictivo.

**Ninguna librería de dibujo.** La visualización se hace con `Graphics2D` del
JDK. No se usa JGraphT, GraphStream, Guava Graphs ni ninguna otra librería de
grafos, ni para el núcleo algorítmico ni para el dibujo.

**Paquetes planos (`core`, `io`, `algoritmos`, `mission`, `ui`).**
No se usó un prefijo de dominio tipo `com.eia.chronicles` por simplicidad del
proyecto académico.

## Limitaciones conocidas

- El dibujo de la grilla se omite por encima de 50×50, como permite la sección
  2.3. En esos casos la aplicación calcula y muestra igual el resultado
  numérico, más un mensaje explicando que el dibujo se omitió.
- El área de visualización muestra un caso de prueba a la vez; cuando la entrada
  trae varios casos, se elige cuál dibujar con el desplegable "Caso a dibujar".
- `MissionPanel` usa `GridCanvas` de forma fija. Al integrar las misiones 2, 3 y
  4 habrá que elegir el canvas según el tipo de misión.