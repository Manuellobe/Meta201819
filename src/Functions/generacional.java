package Functions;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class generacional {
    private int dimension;
    private int maxIteraciones;
    private int randomSeed;
    private int nPopulation;
    private ArrayList<Integer> bestSol;
    private int bestPos;
    private int bestCost;
    private int worstCost;
    private int worstPos;
    private int maxIteSinMejora;
    private float probMut;
    private float probCruce;
    private ArrayList<String> logger;
    private ArrayList<ArrayList<Integer>> poblacion;
    private ArrayList<Integer> costList;
    private String file;

    public generacional(int nDimension, String fileName, int nIte, int arandomSeed, int population, int maxIteracionesSinMejora, float nProbMut, float nProbCruce ){

        dimension = nDimension;
        maxIteraciones = nIte;
        randomSeed = arandomSeed;
        nPopulation = population;
        bestCost = 99999;
        bestPos = -1;
        worstCost = 0;
        worstPos = -1;
        bestSol = new ArrayList<>();
        maxIteSinMejora = maxIteracionesSinMejora;
        probCruce = nProbCruce;
        probMut = nProbMut;

        poblacion = new ArrayList<>();
        logger = new ArrayList<>();
        file = fileName;

        costList = new ArrayList<>();

    }

    public int generacionalSolucion(int[][] fluxMatrix, int[][] distMatrix){

        ArrayList<Integer> availableTerminals;
        Random rand = new Random(randomSeed);
        ArrayList<Integer> colisions;
        int cost, cost2;

        // INICIO GENERACION POBLACION INICIAL
        for(int j = 0; j < nPopulation;j++){

            availableTerminals = new ArrayList<>();

            // Generación de la solución inicial
            for (int i = 0; i < dimension; i++) {

                // Añadimos a cada posicion su valor
                availableTerminals.add(i);

            }

            // Barajamos las posiciones (Mezcla)
            Collections.shuffle(availableTerminals, rand);

            colisions = new ArrayList<>();

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

            poblacion.add(availableTerminals);

            cost = 0;

            // Calculamos el coste
            for (int i = 0; i < dimension; i++) {

                for (int k = 0; k < dimension; k++) {

                    if (i != k) {
                        cost += fluxMatrix[i][k] * distMatrix[availableTerminals.get(i)][availableTerminals.get(k)];
                    }

                }

            }

            costList.add(cost);

            if (cost < bestCost) {

                bestCost = cost;
                bestPos = j;
            }

            if(cost > worstCost){

                worstCost = cost;
                worstPos = j;

            }

            for(int i = 0; i < dimension; i++){

                bestSol.add(poblacion.get(bestPos).get(i));

            }

        }
        // FIN GENERACION POBLACION INICIAL



        int iteraciones = 0;
        int generacionesSinMejora = 0;
        ArrayList<Integer> elitesInNewGen;

        ArrayList<ArrayList<Integer>> nextGen;
        ArrayList<Integer> nextGenCostList;

        boolean bestCostUpdated;

        // INICIO BUCLE PRINCIPAL
        while(iteraciones < maxIteraciones && generacionesSinMejora != maxIteSinMejora){

            int match;
            elitesInNewGen = new ArrayList<>();
            nextGen = new ArrayList<>();
            nextGenCostList = new ArrayList<>();
            bestCostUpdated = false;

            for(int i = 0; i < nPopulation; i++){

                do{

                    match = Math.abs(rand.nextInt(nPopulation));

                }while(match != i);

                if(costList.get(i) <= costList.get(match)){

                    nextGen.add(poblacion.get(i));
                    nextGenCostList.add(costList.get(i));
                    if(i == bestPos){
                        elitesInNewGen.add(i);
                    }

                }else{

                    nextGen.add(poblacion.get(match));
                    nextGenCostList.add(costList.get(match));
                    if(match == bestPos){
                        elitesInNewGen.add(match);
                    }

                }

            }

            float crossProb;

            for(int i = 0; i < nPopulation; i++){

                crossProb = rand.nextFloat();

                if(crossProb < probMut){

                    do{
                        match = Math.abs(rand.nextInt(nPopulation));
                    }while(match == i);

                    for(int m = 0; m < elitesInNewGen.size(); m++){

                        if(elitesInNewGen.isEmpty())
                            break;

                        if(elitesInNewGen.get(m) == i)
                            elitesInNewGen.remove(m);

                        if(elitesInNewGen.isEmpty())
                            break;

                        if(elitesInNewGen.get(m) == match)
                            elitesInNewGen.remove(m);

                    }

                    // Operador de cruce

                    ArrayList<Integer> firstSon, secondSon, firstUsedValues, secondUsedValues;
                    firstSon = new ArrayList<>(dimension);
                    secondSon = new ArrayList<>(dimension);
                    firstUsedValues = new ArrayList<>(dimension);
                    secondUsedValues = new ArrayList<>(dimension);

                    for(int n = 0; n < dimension; n++){

                        firstSon.add(-1);
                        secondSon.add(-1);

                    }

                    int firstPos, secondPos;
                    do{
                        firstPos = Math.abs(rand.nextInt(dimension))%dimension;
                        secondPos = Math.abs(rand.nextInt(dimension))%dimension;
                        if(firstPos > secondPos){
                            int aux = firstPos;
                            firstPos = secondPos;
                            secondPos = aux;
                        }

                    }while(((secondPos - firstPos) >= dimension-1)); // En el caso de que el rango contenga toda la dimensión, se repite

                    for(int n = firstPos; n <= secondPos; n++){

                        firstSon.set(n, nextGen.get(i).get(n));
                        firstUsedValues.add(nextGen.get(i).get(n));

                    }

                    int alocPos = secondPos+1;

                    for(int n = firstPos; n < dimension; n++){

                        if(!firstUsedValues.contains(nextGen.get(match).get(n))){

                            firstSon.set(alocPos%dimension, nextGen.get(match).get(n));
                            firstUsedValues.add(nextGen.get(match).get(n));
                            alocPos++;

                        }


                    }

                    for(int n = 0; n < firstPos; n++){

                        if(!firstUsedValues.contains(nextGen.get(match).get(n))){

                            firstSon.set(alocPos%dimension, nextGen.get(match).get(n));
                            firstUsedValues.add(nextGen.get(match).get(n));
                            alocPos++;

                        }

                    }



                    // Segundo hijo
                    for(int n = firstPos; n <= secondPos; n++){

                        secondSon.set(n, nextGen.get(match).get(n));
                        secondUsedValues.add(nextGen.get(match).get(n));

                    }

                    alocPos = secondPos+1;

                    for(int n = firstPos; n < dimension; n++){

                        if(!secondUsedValues.contains(nextGen.get(i).get(n))){

                            secondSon.set(alocPos%dimension, nextGen.get(i).get(n));
                            secondUsedValues.add(nextGen.get(i).get(n));
                            alocPos++;

                        }


                    }

                    for(int n = 0; n < firstPos; n++){

                        if(!secondUsedValues.contains(nextGen.get(i).get(n))){

                            secondSon.set(alocPos%dimension, nextGen.get(i).get(n));
                            secondUsedValues.add(nextGen.get(i).get(n));
                            alocPos++;

                        }

                    }

                    // Operador de mutacion

                    float mutProb;

                    // Mutacion en el primer hijo
                    for(int n = 0; n < dimension; n++){

                        mutProb = rand.nextFloat();
                        if(mutProb <= probMut* dimension){

                            int mutPos;
                            do{
                                mutPos = Math.abs(rand.nextInt(dimension));

                            }while(mutPos == n);

                            int aux = firstSon.get(n);
                            firstSon.set(n, firstSon.get(mutPos));
                            firstSon.set(mutPos, aux);

                        }

                    }

                    // Mutacion en el segundo hijo
                    for(int n = 0; n < dimension; n++){

                        mutProb = rand.nextFloat();
                        if(mutProb <= 0.001* dimension){

                            int mutPos;
                            do{
                                mutPos = Math.abs(rand.nextInt(dimension));

                            }while(mutPos == n);

                            int aux = firstSon.get(n);
                            secondSon.set(n, secondSon.get(mutPos));
                            secondSon.set(mutPos, aux);

                        }

                    }

                    // Recalcular coste

                    cost = 0;
                    cost2 = 0;

                    // Calculamos el coste
                    for (int n = 0; n < dimension; n++) {

                        for (int k = 0; k < dimension; k++) {

                            if (n != k) {
                                cost += fluxMatrix[n][k] * distMatrix[nextGen.get(i).get(n)][nextGen.get(i).get(k)];
                                cost2 += fluxMatrix[n][k] * distMatrix[nextGen.get(match).get(n)][nextGen.get(match).get(k)];
                            }

                        }

                    }

                    //TODO: Comprobar si el nuevo coste es mejor que el mejor o peor que el peor y reemplazar con su respectivo
                    //TODO: Logger en ambos algoritmos
                    //TODO: Coger parametros del fichero y gg :D

                    // Reemplazo respecto a la poblacion actual

                    nextGen.set(i, firstSon);
                    nextGenCostList.set(i, cost);

                    nextGen.set(match, firstSon);
                    nextGenCostList.set(match, cost2);

                    // Comprobamos si el mejor de los dos es mejor que el mejor coste
                    // Asimismo, tambien comprobamos si el peor es peor que el peor coste
                    if(cost <= cost2){
                        if(cost < bestCost){
                            bestCost = cost;
                            bestPos = i;
                            bestSol = nextGen.get(i);
                            generacionesSinMejora = -1;
                            bestCostUpdated = true;
                        }
                        if(cost2 > worstCost){
                            worstCost = cost2;
                            worstPos = match;
                        }
                    }else{
                        if(cost2 < bestPos){
                            bestCost = cost2;
                            bestPos = match;
                            generacionesSinMejora = -1;
                            bestCostUpdated = true;
                            bestSol = nextGen.get(match);
                        }
                        if(cost > worstCost){
                            worstCost = cost;
                            worstPos = i;
                        }
                    }

                }

            }

            if(elitesInNewGen.size() == 0 && !bestCostUpdated){

                nextGen.set(worstPos, bestSol);
                nextGenCostList.set(worstPos, bestCost);

            }

            for(int i = 0; i < nPopulation; i++){

                poblacion.set(i, nextGen.get(i));
                costList.set(i, nextGenCostList.get(i));

            }

            generacionesSinMejora++;
            iteraciones++;

        }

        return bestCost;
    }

}
