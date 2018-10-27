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
    
    // Selección del Mecanismo de Enfriamiento:  0 -> Boltzmann | 1 -> Geométrico
    private int cooling;

    public enfriamientoSimulado(int nDimension,int coolingType){

        dimension = nDimension;
        randomSeed = 77383310;
        
        // Ajuste del Mecanismo de Enfriamiento
        if(coolingType == 0){
            annType = "Boltzman";
        }else{
            alpha = 0.9f;
            annType = "Geometrico con alfa = "+alpha;
        }

        logger = new ArrayList<>();

    }

    public int enfriamientoSolucion(int[][] fluxMatrix, int[][] distMatrix){

        ArrayList<Integer> availableTerminals = new ArrayList<>();

        logger.add("Logger\n \n Generando solucion aleatoria inicial: \n - Vector solucion: ");


        // Generación de la solución inicial
            for(int i = 0; i < dimension; i++){

                // Añadimos a cada posicion su valor
                availableTerminals.add(i);

            }

            // Barajamos las posiciones (Mezcla)
            Collections.shuffle(availableTerminals, new Random(randomSeed));

        // Fin generación solución inicial

        ArrayList<Integer> bestSolution = new ArrayList<>();
        ArrayList<Integer> tempSolution = new ArrayList<>();

        int bestCost = 0;

        // Añadimos la solución inicial a las estructuras de evolucion
        for(int i = 0; i < dimension; i++){

            logger.add( i + " | " + availableTerminals.get(i));
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

        logger.add("\n - Coste: " + bestCost + "\n\nInicio bucle Enfriamiento Simulado (SE):\n");

        
        init_temperature = bestCost * 1.5f;
        temperature = init_temperature;
        
        logger.add("Temperatura inicial: " + init_temperature + "\n");

        int iterations = 0;
        BitSet DLB = new BitSet(dimension);

        // Bucle global
        while(iterations < 50000 && DLB.cardinality() != dimension){

            int num_vecinos = 0;
            int num_exitos = 0;
            int max_vecinos = 0;
            int max_exitos = (max_vecinos/10);
            
            for(int i = 0; i < dimension; i++){

                // Comprobamos la mascara para evitar comprobar soluciones sin mejora
                if(DLB.get(i) == false){

                    // Probamos todas las combinaciones con la i actual
                    for(int j = 0; j < dimension; j++){

                        // Permutamos posiciones || operador de intercambio
                                               
                        tempSolution.set(i, bestSolution.get(j));
                        tempSolution.set(j, bestSolution.get(i));

                        int tempCost = 0;
                        
                        for(int k = 0; k < dimension; k++){

                            // Coste de la mejor solucion
                            tempCost += 2 * (fluxMatrix[k][i] * distMatrix[bestSolution.get(k)][bestSolution.get(i)]);
                            tempCost += 2 * (fluxMatrix[k][j] * distMatrix[bestSolution.get(k)][bestSolution.get(j)]);

                            // Coste de la permutacion
                            tempCost -= 2 * (fluxMatrix[k][i] * distMatrix[tempSolution.get(k)][tempSolution.get(i)]);
                            tempCost -= 2 * (fluxMatrix[k][j] * distMatrix[tempSolution.get(k)][tempSolution.get(j)]);
                        }
                        
                        // Si el coste es menor
                        if (tempCost > 0){

                            logger.add("La diferencia de coste tras la permutacion es: " + tempCost + "\n");
                            logger.add("La solucion actual es mejor que la global, intercambio los valores " + i + ", " + j + ".\n");

                            // Reemplazamos la mejor solucion con la nueva mejor solucion
                            bestSolution.set(i, tempSolution.get(i));
                            bestSolution.set(j, tempSolution.get(j));
                            bestCost -= tempCost;


                            // Rehabilitamos la mascara en las posiciones i y j
                            DLB.set(i, false);
                            DLB.set(j, false);
                            
                            break;

                        }

                        // En caso de que la solucion sea peor
                        else {

                            
                            Random rnd = new Random();
                            float acceptance = (float) Math.exp(((-1)*tempCost) / ((iterations) * temperature));
                            float probability = rnd.nextFloat();

                            if (probability <= acceptance) {

                                // Rehabilitamos la mascara en las posiciones i y j
                                DLB.set(i, false);
                                DLB.set(j, false);

                            } else {
                                // Restablecemos la solucion temporal a la mejor solucion
                                tempSolution.set(i, bestSolution.get(i));
                                tempSolution.set(j, bestSolution.get(j));
                            }

                        }

                        // Enfriamiento
                        if (cooling == 0) {
                            temperature = (float) (init_temperature / (1 + Math.log(iterations)));
                        } else {
                            temperature = (alpha * temperature);
                        }
                        
                        // Aumentamos las iteraciones y comprobamos si hemos llegado al limite
                        iterations++;
                        if(iterations == 50000){

                            logger.add("50000 iteraciones realizadas, finalizando el bucle de busqueda.\n");
                            break;
                        }
                        
                        // Si la temperatura llega a un estado estable (congelado) se para
                        if(temperature == 0){
                            logger.add("La temperatura ha llegado a cero en la iteracion: "+(iterations+1)+"\n");
                            break;
                        }

                        // Si hemos comprobado todas las combinaciones con el valor i y no encontramos mejora
                        // La marcamos para no volver a comprobarla
                        if(j == dimension-1){

                            DLB.set(i, true);

                        }

                    }

                }
            }

        }

        logger.add("Se han realizado las 50000 iteraciones o se ha explorado todo el espacio de busqueda.\n");
        logger.add("Solucion final:\n");
        for(int i = 0; i < dimension; i++){

            logger.add(i + " | " + bestSolution.get(i) + "\n");

        }

        logger.add("\nCoste final: " + bestCost + ".\n");

        // Escritura del logger a fichero

        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("src/Datos/LogES"+"-"+annType+"-"+randomSeed+".txt");
            pw = new PrintWriter(fichero);

            for (String linea: logger) {
                pw.print(linea);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Comprobamos que el fichero se ha cerrado correctamente
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        // Devolvemos el mejor coste obtenido
        return bestCost;
    }

}
