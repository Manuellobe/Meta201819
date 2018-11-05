package Functions;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.BitSet;

public class enfriamientoSimulado {

    private int dimension;
    private int randomSeed;
    private String annType;
    private float temperature;
    private float init_temperature;
    private ArrayList<String> logger;
    private float alpha;
    private String file;

    // Selección del Mecanismo de Enfriamiento:  0 -> Boltzmann | 1 -> Geométrico
    private int cooling;

    public enfriamientoSimulado(int nDimension, int coolingType, String fileName, int aRandomSeed) {

        dimension = nDimension;
        randomSeed = aRandomSeed;
        cooling = coolingType;

        file = fileName;

        // Ajuste del Mecanismo de Enfriamiento
        if (cooling == 0) {
            annType = "Boltzman";
        } else {
            alpha = 0.9f;
            annType = "Geometrico con alfa = " + alpha;
        }

        logger = new ArrayList<>();

    }

    public long enfriamientoSolucion(int[][] fluxMatrix, int[][] distMatrix) {

        ArrayList<Integer> availableTerminals = new ArrayList<>();
        long startTime = System.nanoTime();
        Random rand = new Random(randomSeed);
        float acceptance;
        float probability;
        
        logger.add("Semilla utilizada: "+randomSeed+"\n");
        logger.add("Logger\n \n Generando solucion aleatoria inicial: \n - Vector solucion: ");

        // Generación de la solución inicial
        for (int i = 0; i < dimension; i++) {

            // Añadimos a cada posicion su valor
            availableTerminals.add(i);

        }

        // Barajamos las posiciones (Mezcla)
        Collections.shuffle(availableTerminals, rand);

        ArrayList<Integer> colisions = new ArrayList<>();

        for (int i = 0; i < dimension; i++) {

            if (availableTerminals.get(i) == i) {
                colisions.add(i);
            }

        }

        if (colisions.size() > 1) {

            for (int i = 0; i < colisions.size() - 1; i++) {

                availableTerminals.set(colisions.get(i), colisions.get(i + 1));

            }

            availableTerminals.set(colisions.get(colisions.size() - 1), colisions.get(0));

        } else if (colisions.size() == 1) {

            int randPos;
            do {
                randPos = rand.nextInt(dimension);
            } while (randPos == colisions.get(0));

            availableTerminals.set(colisions.get(0), availableTerminals.get(randPos));
            availableTerminals.set(randPos, colisions.get(0));

        }

        // Fin generación solución inicial
        ArrayList<Integer> bestSolution = new ArrayList<>();
        ArrayList<Integer> annSolution = new ArrayList<>();
        ArrayList<Integer> tempSolution = new ArrayList<>();

        long bestCost = 0;
        long annCost = 0;
        long tempCost;

        // Añadimos la solución inicial a las estructuras de evolucion
        for (int i = 0; i < dimension; i++) {

            logger.add(i + " | " + availableTerminals.get(i));
            bestSolution.add(availableTerminals.get(i));
            annSolution.add(availableTerminals.get(i));
            tempSolution.add(availableTerminals.get(i));

        }

        // Calculamos el coste
        for (int i = 0; i < dimension; i++) {

            for (int j = 0; j < dimension; j++) {

                if (i != j) {
                    bestCost += fluxMatrix[i][j] * distMatrix[bestSolution.get(i)][bestSolution.get(j)];
                }

            }

        }

        annCost += bestCost;

        logger.add("\n - Coste: " + bestCost + "\n\nInicio bucle Enfriamiento Simulado (SE):\n");

        init_temperature = bestCost * 1.5f;
        temperature = bestCost * 1.5f;

        logger.add("Temperatura inicial: " + init_temperature + "\n");

        int iterations = 1;
        BitSet DLB = new BitSet(dimension);

        // Bucle global
        while (iterations < 50001 && (dimension - DLB.cardinality()) * 0.1f > 0) {

            for (int i = 0; i < dimension; i++) {

                // Comprobamos la mascara para evitar comprobar soluciones sin mejora
                if (DLB.get(i) == false) {

                    // Probamos todas las combinaciones con la i actual
                    for (int j = 1; j < dimension; j++) {

                        // Enfriamiento
                        if (cooling == 0) {
                            temperature = (float) (init_temperature / (1 + Math.log(iterations)));
                        } else {
                            temperature = (alpha * temperature);
                        }

                        // Permutamos posiciones || operador de intercambio
                        tempSolution.set(i, annSolution.get(j));
                        tempSolution.set(j, annSolution.get(i));

                        tempCost = 0;

                        for (int k = 0; k < dimension; k++) {

                            // Coste de la mejor solucion
                            tempCost += 2 * (fluxMatrix[k][i] * distMatrix[annSolution.get(k)][annSolution.get(i)]);
                            tempCost += 2 * (fluxMatrix[k][j] * distMatrix[annSolution.get(k)][annSolution.get(j)]);

                            // Coste de la permutacion
                            tempCost -= 2 * (fluxMatrix[k][i] * distMatrix[tempSolution.get(k)][tempSolution.get(i)]);
                            tempCost -= 2 * (fluxMatrix[k][j] * distMatrix[tempSolution.get(k)][tempSolution.get(j)]);
                        }


                        // Si el coste es mayor, y por tanto la solucion es mejor
                        if (tempCost > 0) {

                            logger.add("La diferencia de coste tras la permutacion es: " + tempCost + "\n");
                            logger.add("La solucion actual es mejor que la global, intercambio los valores " + i + ", " + j + ".\n");

                            annSolution.set(i, tempSolution.get(i));
                            annSolution.set(j, tempSolution.get(j));
                            annCost -= tempCost;

                            if (annCost < bestCost) {
                                // Reemplazamos la mejor solucion con la nueva mejor solucion

                                for (int a = 0; a < dimension; a++) {
                                    bestSolution.set(a, tempSolution.get(a));
                                }

                                bestCost = annCost + 0;

                            }

                            iterations++;

                            // Rehabilitamos la mascara en las posiciones i y j
                            DLB.set(i, false);
                            DLB.set(j, false);

                            break;

                        } // En caso de que la solucion sea peor
                        else {

                            acceptance = (float) Math.exp(tempCost / temperature);
                            probability = rand.nextFloat();

                            if (probability <= acceptance) {

                                // Reemplazamos la solucion aceptada en la solucion de enfriamiento actual
                                annSolution.set(i, tempSolution.get(i));
                                annSolution.set(j, tempSolution.get(j));
                                annCost -= tempCost;

                                // Rehabilitamos la mascara en las posiciones i y j
                                DLB.set(i, false);
                                DLB.set(j, false);

                            } else {
                                // Restablecemos la solucion temporal a la mejor solucion
                                tempSolution.set(i, annSolution.get(i));
                                tempSolution.set(j, annSolution.get(j));
                            }

                        }

                        // Aumentamos las iteraciones y comprobamos si hemos llegado al limite
                        if (iterations == 50001) {

                            logger.add("50000 iteraciones realizadas, finalizando el bucle de busqueda.\n");
                            break;
                        }

                        // Si la temperatura llega a un estado estable (congelado) se para
                        if (temperature == 0) {
                            logger.add("La temperatura ha llegado a cero en la iteracion: " + (iterations) + "\n");
                            break;
                        }

                        // Si hemos comprobado todas las combinaciones con el valor i y no encontramos mejora
                        // La marcamos para no volver a comprobarla
                        if (j == dimension - 1) {

                            DLB.set(i, true);

                        }

                    }

                }
            }

        }

        logger.add("Se ha explorado todo el espacio de busqueda.\n");
        logger.add("Solucion final:\n");
        for (int i = 0; i < dimension; i++) {

            logger.add(i + " | " + bestSolution.get(i) + "\n");

        }

        logger.add("\nCoste final: " + bestCost + ".\n");
        long endTime = System.nanoTime() - startTime;
        logger.add("Tiempo de ejecucion del algoritmo: "+endTime+" ns.\n");
        // Escritura del logger a fichero
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("src/Datos/LogES" + "-" + file + "-" + annType + "-" + randomSeed + ".txt");
            pw = new PrintWriter(fichero);

            for (String linea : logger) {
                pw.print(linea);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Comprobamos que el fichero se ha cerrado correctamente
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        // Devolvemos el mejor coste obtenido
        return bestCost;
    }

}
