package Functions;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class generacional {
    private int dimension;
    private int randomSeed;
    private int nPopulation;
    private ArrayList<Integer> bestSol;
    private int bestPos;
    private int bestCost;
    private int maxIteSinMejora;
    private ArrayList<String> logger;
    private ArrayList<ArrayList<Integer>> poblacion;
    private ArrayList<Integer> costList;
    private ArrayList<Pair<Integer, Integer>> buffer;
    private String file;

    public generacional(int nDimension, String fileName, int arandomSeed, int population, int maxIteracionesSinMejora){

        dimension = nDimension;
        randomSeed = arandomSeed;
        nPopulation = population;
        bestCost = 99999;
        bestPos = -1;
        bestSol = new ArrayList<>();
        maxIteSinMejora = maxIteracionesSinMejora;

        poblacion = new ArrayList<>();
        logger = new ArrayList<>();
        file = fileName;

        costList = new ArrayList<>();
        buffer = new ArrayList<>();

    }

    public int generacionalSolucion(int[][] fluxMatrix, int[][] distMatrix){

        ArrayList<Integer> availableTerminals;
        Random rand = new Random(randomSeed);
        ArrayList<Integer> colisions;
        int cost, cost2;
        int minCostBuffer = 0;

        ArrayList<Integer> firstWinner, secondWinner;

        for(int i = 0; i < 5; i++){

            buffer.add(new Pair<Integer, Integer>(0,0));

        }

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

            if(cost > minCostBuffer){

                for(int i = 4; i >= 0; i--){

                    if(cost > buffer.get(i).getValue()){
                        buffer.set(i, new Pair<>(j, cost));
                        minCostBuffer = buffer.get(0).getValue();
                        break;
                    }

                }

            }

            for(int i = 0; i < dimension; i++){

                bestSol.add(poblacion.get(bestPos).get(i));

            }

        }
        // FIN GENERACION POBLACION INICIAL



        int iteraciones = 0;
        int generacionesSinMejora = 0;
        int pos1, pos2, pos3, pos4;
        ArrayList<Integer> elitesInNewGen;

        ArrayList<ArrayList<Integer>> nextGen;
        ArrayList<Integer> nextGenCostList;

        // INICIO BUCLE PRINCIPAL
        while(iteraciones < 50000 && generacionesSinMejora != maxIteSinMejora){

            int match;
            elitesInNewGen = new ArrayList<>();
            nextGen = new ArrayList<>();
            nextGenCostList = new ArrayList<>();

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

                if(crossProb < 0.7f){

                    do{
                        match = Math.abs(rand.nextInt(nPopulation));
                    }while(match == i);

                    for(int m = 0; m < elitesInNewGen.size(); m++){

                        if(elitesInNewGen.get(m) == i)
                            elitesInNewGen.remove(m);

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
                        if(mutProb <= 0.001* dimension){

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
                                cost += fluxMatrix[n][k] * distMatrix[poblacion.get(i).get(n)][poblacion.get(i).get(k)];
                                cost2 += fluxMatrix[n][k] * distMatrix[poblacion.get(match).get(n)][poblacion.get(match).get(k)];
                            }

                        }

                    }

                    //TODO: Comprobar si el nuevo coste es mejor que el mejor o peor que el peor y reemplazar con su respectivo
                    //TODO: Eliminar el buffer, puesto que es innecesario
                    //TODO: Logger en ambos algoritmos
                    //TODO: Coger parametros del fichero y gg :D

                }

            }





            // Reemplazo respecto a la poblacion actual

            // Comprobacion con el primer ganador
            if(buffer.size() >= 1 && cost < buffer.get(buffer.size()-1).getValue()){
                int posToInsert = buffer.get(buffer.size()-1).getKey();
                poblacion.set(posToInsert, firstWinner);
                costList.set(posToInsert, cost);
                buffer.remove(buffer.size()-1);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost > buffer.get(i).getValue()) {
                        buffer.add(i + 1, new Pair<Integer, Integer>(posToInsert, cost));
                        break;
                    }
                }
                if(cost < bestCost){
                    bestCost = cost;
                    bestPos = posToInsert;
                    generacionesSinMejora = -1;
                }
            }else if(buffer.size() >= 2 && cost < buffer.get(buffer.size()-2).getValue()){
                int posToInsert = buffer.get(buffer.size()-2).getKey();
                poblacion.set(posToInsert, firstWinner);
                costList.set(posToInsert, cost);
                buffer.remove(buffer.size()-2);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost > buffer.get(i).getValue()) {
                        buffer.add(i + 1, new Pair<Integer, Integer>(posToInsert, cost));
                        break;
                    }
                }
                if(cost < bestCost){
                    bestCost = cost;
                    bestPos = posToInsert;
                    generacionesSinMejora = -1;
                }
            }

            // Comprobacion con el segundo ganador
            if(buffer.size() >= 1 && cost2 < buffer.get(buffer.size()-1).getValue()){
                int posToInsert = buffer.get(buffer.size()-1).getKey();
                poblacion.set(posToInsert, secondWinner);
                costList.set(posToInsert, cost2);
                buffer.remove(buffer.size()-1);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost2 > buffer.get(i).getValue()) {
                        buffer.add(i + 1, new Pair<Integer, Integer>(posToInsert, cost2));
                        break;
                    }
                }
                if(cost2 < bestCost){
                    bestCost = cost2;
                    bestPos = posToInsert;
                    generacionesSinMejora = -1;
                }
            }else if(buffer.size() >= 2 && cost2 < buffer.get(buffer.size()-2).getValue()){
                int posToInsert = buffer.get(buffer.size()-2).getKey();
                poblacion.set(posToInsert, secondWinner);
                costList.set(posToInsert, cost2);
                buffer.remove(buffer.size()-2);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost2 > buffer.get(i).getValue()) {
                        buffer.add(i + 1, new Pair<Integer, Integer>(posToInsert, cost2));
                        break;
                    }
                }
                if(cost2 < bestCost){
                    bestCost = cost2;
                    bestPos = posToInsert;
                    generacionesSinMejora = -1;
                }
            }

            if(buffer.size() <= 1){

                minCostBuffer = 0;
                int actualCost;


                for(int i = 0; i < costList.size(); i++){

                    actualCost = costList.get(i);

                    if(buffer.size() < 5){

                        buffer.add(new Pair<>(i, actualCost));
                        if(actualCost > minCostBuffer)
                            minCostBuffer = actualCost;

                    }

                    else if(actualCost > minCostBuffer){

                        for(int j = 4; j >= 0; j--){

                            if(actualCost > buffer.get(j).getValue()){
                                buffer.set(j, new Pair<Integer, Integer>(i, actualCost));
                                minCostBuffer = buffer.get(0).getValue();
                                break;
                            }

                        }

                    }

                }

            }

            generacionesSinMejora++;
            iteraciones++;

        }

        return bestCost;
    }

}
