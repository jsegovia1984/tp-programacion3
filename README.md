# ALGO3-TPO
# Proyecto final de la materia Diseño de Algoritmos(Progra 3)

**Determinación de instalación de Centros de Distribución:**

Una compañía de logística debe analizar y determinar si debe efectuar la construcción o no de un
conjunto de Centros de Distribución (CDs), para almacenar y posteriormente transportar la materia
prima suministrada por sus Clientes.
La compañía tiene 50 clientes, a los cuales les compra la materia prima agrícola. Los clientes están
localizados en las distintas provincias del país. Cada cliente produce una determinada cantidad anual
fija, que es vendida a la compañía.
Para efectuar una optimización de sus costos de operación, la compañía está analizando la posible
ubicación de 8 Centros de Distribución distribuidos a lo largo de todo el país. Está previsto que cada
Centro de Distribución esté localizado sobre una ruta o vía férrea, de tal forma que pueda enviarse
posteriormente la materia prima a los puertos para exportación. Cada Centro de Distribución tiene
un costo anual previsto para su operación, independiente del volumen anual de materias primas que
administre.
Los Clientes y los posibles Centros de Distribución están conectados entre sí, por distintas rutas.
Las rutas conectan Clientes con otros Clientes y con algunos Centros de Distribución (no todos los
Clientes están conectados en forma directa con un Centro de Distribución, estando conectados con
otros Clientes).
En la figura, todos los puntos numerados del 0 al 49 representan distintos clientes; 
los potenciales Centros de Distribución están numerados del 50 al 57. 
Todos los Centros de Distribución están conectados por vías férreas al Puerto desde donde se exportan las materias primas. 
El costo de transportar la materia prima desde un Cliente a un Centro de Distribución está determinado por el 
costo mínimo unitario de transportar la materia prima entre dicho Cliente y dicho Centro de Distribución, 
más el costo unitario de transportarla entre el Centro de Distribución y el Puerto, todo ello multiplicado por el volumen 
de producción anual del Cliente. El objetivo del problema es: - Determinar cuáles Centros de Distribución deben construirse, 
de tal forma que se minimice el costo total anual - Determinar a qué Centro de Distribución debe enviar 
cada Cliente su materia prima.


**Estrategia de Resolución:**

La estrategia de resolución aplicada ha sido enfocarnos en Dijkstra el cual es un algoritmo de búsqueda de caminos 
mínimos en grafos ponderados, especialmente útil en grafos dirigidos y no negativos. Esto nos permite tomar los caminos
adecuados no solo directamente a un Centro de Distribución, sino de contar con los trayectos de varios clientes hacia el
Centro que permita el menor costo de transporte a puerto con la cantidad de materia prima acumulada.


**Análisis de Complejidad Temporal**


