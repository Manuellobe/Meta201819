package Functions;

import java.io.FileWriter;
import java.io.PrintWriter;
import javafx.util.Pair;

import java.util.Collections;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.Arrays;

// Comparador para ordenar pares de enteros de mayor a menor
class SortGreaterToLesser implements Comparator<Pair<Integer, Integer>> {

    public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        if (a.getValue() < b.getValue()) {
            return 1;
        } else if (a.getValue() > b.getValue()) {
            return -1;
        } else {
            return 0;
        }
    }
}

// Comparador para ordenar pares de enteros de menor a mayor
class SortLesserToGreater implements Comparator<Pair<Integer, Integer>> {

    public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        if (a.getValue() < b.getValue()) {
            return -1;
        } else if (a.getValue() > b.getValue()) {
            return 1;
        } else {
            return 0;
        }
    }
}

public class greedy {

    // Dimension de las matrices cuadradas
    private int dimension;

    // Vectores de flujo y discancia acumulados ordenados
    private ArrayList<Pair<Integer, Integer>> orderedFluxValueVector;
    private ArrayList<Pair<Integer, Integer>> orderedDistValueVector;

    // Vector de representacion de la solucion
    private int[] solutionRepresentation;
    private ArrayList<String> logger;
    private String file;

    // Constructor
    public greedy(int nDimension, String afile) {

        dimension = nDimension;
        orderedFluxValueVector = new ArrayList<Pair<Integer, Integer>>();
        orderedDistValueVector = new ArrayList<Pair<Integer, Integer>>();
        solutionRepresentation = new int[nDimension];
        file = afile;
        logger = new ArrayList<>();
    }

    public int greedySolution(int[][] fluxMatrix, int[][] distMatrix) {

        long startTime = System.nanoTime();
        //Calculamos el flujo y la distancia acumulada para cada terminal
        int tempFlux, tempDist;

        logger.add("Calculando flujos y distancias acumuladas para cada terminal. \n");
        for (int i = 0; i < dimension; i++) {

            tempFlux = 0;
            tempDist = 0;

            // Calculamos el valor acumulado correspondiente
            for (int j = 0; j < dimension; j++) {

                tempFlux += fluxMatrix[i][j];
                tempDist += distMatrix[i][j];

            }

            // Añadimos el valor acumulado correspondiente a cada terminal
            orderedFluxValueVector.add(new Pair<Integer, Integer>(i, tempFlux));
            orderedDistValueVector.add(new Pair<Integer, Integer>(i, tempDist));

        }

        logger.add("Ordenando vectores obtenidos. \n");
        // Una vez obtenemos todos los valores acumulados, estos se ordenan
        Collections.sort(orderedFluxValueVector, new SortGreaterToLesser());
        Collections.sort(orderedDistValueVector, new SortLesserToGreater());

        // Asignamos al vector solucion la primera solucion
        int currentTerminal;

        int solutionValue = 0;

        for (int i = 0; i < dimension; i++) {

            currentTerminal = orderedFluxValueVector.get(i).getKey();

            solutionRepresentation[currentTerminal] = orderedDistValueVector.get(i).getKey();

        }
        
        logger.add("Asignación finalizada, realizando comprobacion de requisitos. \n");
        // Comprobamos que ningun terminal sea emparejado con si mismo
        int tempSwap;

        for (int i = 0; i < dimension; i++) {

            // En caso de que un terminal sea conectado a si mismo, lo intercambiamos con el anterior
            if (solutionRepresentation[i] == i) {

                tempSwap = solutionRepresentation[i];

                // En caso de que ocurra en la posicion inicial, se intercambia con la posicion posterior
                if (i == 0) {

                    solutionRepresentation[i] = solutionRepresentation[i + 1];
                    solutionRepresentation[i + 1] = tempSwap;

                } else {

                    solutionRepresentation[i] = solutionRepresentation[i - 1];
                    solutionRepresentation[i - 1] = tempSwap;
                    i--;

                }

            }

        }

        logger.add("Revisión finalizada, calculando coste. \n");
        //Calculo de costes
        for (int i = 0; i < dimension; i++) {

            for (int j = 0; j < dimension; j++) {

                if (i != j) {
                    solutionValue += fluxMatrix[i][j] * distMatrix[solutionRepresentation[i]][solutionRepresentation[j]];
                }

            }

        }
        
        logger.add("Algoritmo finalizado. Coste obtenido: "+solutionValue+ ".\n");
        long endTime = System.nanoTime() - startTime;
        logger.add("Tiempo de ejecucion del algoritmo: "+endTime+" ns.\n");

        // Escritura del logger a fichero
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("src/Datos/LogGreedy" + "-" + file + "-" + ".txt");
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
        
        return solutionValue;
    }

}
