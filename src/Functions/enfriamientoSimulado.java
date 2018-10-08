package Functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.BitSet;

public class enfriamientoSimulado {

    private int dimension;
    private int randomSeed;
    private int temperature;

    public enfriamientoSimulado(int nDimension){

        temperature = 500;
        dimension = nDimension;
        randomSeed = 77383310;
    }

    public int enfriamientoSolucion(int[][] fluxMatrix, int[][] distMatrix){

        ArrayList<Integer> availableTerminals = new ArrayList<>();


        // Generación de la solución inicial
        for(int i = 0; i < dimension; i++){

            // Añadimos a casa posicion su valor
            availableTerminals.add(i);

        }

        // Barajamos las posiciones
        Collections.shuffle(availableTerminals, new Random(randomSeed));
        // Fin generación solución inicial

        ArrayList<Integer> bestSolution = new ArrayList<>();
        ArrayList<Integer> tempSolution = new ArrayList<>();

        int bestCost = 0;

        // Añadimos la solución inicial a las estructuras de evolucion
        for(int i = 0; i < dimension; i++){

            bestSolution.add(availableTerminals.get(i));
            tempSolution.add(availableTerminals.get(i));

        }

        // Calculamos el coste
        for(int i = 0; i < dimension; i++){

            for(int j = 0; j < dimension; j++){

                if(i != j)
                    bestCost += fluxMatrix[i][j] * distMatrix[bestSolution.get(i)][bestSolution.get(j)];

            }

        }

        int iterations = 0;
        BitSet DLB = new BitSet(dimension);


        // Bucle global
        while(iterations < 50000 && DLB.cardinality() != dimension){


            for(int i = 0; i < dimension; i++){

                // Comprobamos la mascara para evitar comprobar soluciones sin mejora
                if(!DLB.get(i)){

                    // Probamos todas las combinaciones con la i actual
                    for(int j = 0; j < dimension; j++){

                        // Permutamos posiciones || operador de intercambio
                        tempSolution.set(i, bestSolution.get(j));
                        tempSolution.set(j, bestSolution.get(i));

                        int tempCost = 0;


                        // Calculamos el nuevo coste
                        for(int k = 0; k < dimension; k++){

                            for(int h = 0; h < dimension; h++){

                                //TODO: Factorizar la solucion (hacerlo eficiente)
                                if(k != h)
                                    tempCost += fluxMatrix[k][h] * distMatrix[tempSolution.get(k)][tempSolution.get(h)];

                            }

                        }


                        // Si el coste es menor
                        if (tempCost < bestCost){

                            // Reemplazamos la mejor solucion con la nueva mejor solucion
                            bestSolution.set(i, tempSolution.get(i));
                            bestSolution.set(j, tempSolution.get(j));
                            bestCost = tempCost;

                            // Rehabilitamos la mascara en las posiciones i y j
                            DLB.set(i, true);
                            DLB.set(j, true);
                            break;

                        }

                        // En caso de que la solucion sea peor
                        else{

                            // Restablecemos la solucion temporal a la mejor solucion
                            tempSolution.set(i, bestSolution.get(i));
                            tempSolution.set(j, bestSolution.get(j));

                        }

                        // Aumentamos las iteraciones y comprobamos si hemos llegado al limite
                        iterations++;
                        if(iterations == 50000)
                            break;

                        // Si hemos comprobado todas las combinaciones con el valor i y no encontramos mejora
                        // La marcamos para no volver a comprobarla
                        DLB.set(i, false);

                    }

                }

            }

        }

        // Devolvemos el mejor coste obtenido
        return bestCost;
    }




}
