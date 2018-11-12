package Functions;

import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class estacionario {

    private int dimension;
    private int randomSeed;
    private int nPopulation;
    private ArrayList<String> logger;
    private ArrayList<ArrayList<Integer>> poblacion;
    private ArrayList<Integer> costList;
    private ArrayList<Pair<Integer, Integer>> buffer;
    private String file;

    public estacionario(int nDimension, String fileName, int arandomSeed){

        dimension = nDimension;
        randomSeed = arandomSeed;
        nPopulation = 50;

        poblacion = new ArrayList<>();
        logger = new ArrayList<>();
        file = fileName;

        costList = new ArrayList<>();
        buffer = new ArrayList<>();

    }

    public int estacionarioSolucion(int[][] fluxMatrix, int[][] distMatrix){

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

            if(cost > minCostBuffer){

                for(int i = 4; i >= 0; i--){

                    if(cost > buffer.get(i).getValue()){
                        buffer.set(i, new Pair<Integer, Integer>(i, cost));
                        minCostBuffer = buffer.get(0).getValue();
                        break;
                    }

                }

            }

        }
        // FIN GENERACION POBLACION INICIAL

        // INICIO BUCLE PRINCIPAL

        int iteraciones = 0;
        int pos1, pos2, pos3, pos4;
        while(iteraciones < 50000){

            // Generar 4 posiciones aleatorias distintas para el torneo binario

            pos1 = rand.nextInt(dimension);

            do{
                pos2 = rand.nextInt(dimension);
            }while(pos1 == pos2);

            do{
                pos3 = rand.nextInt(dimension);
            }while(pos3 == pos2 || pos3 == pos1);

            do{
                pos4 = rand.nextInt(dimension);
            }while(pos4 == pos3 || pos4 == pos2 || pos4 == pos1);


            // Torneo binario

            if(costList.get(pos1) > costList.get(pos2))
                firstWinner = poblacion.get(pos1);
            else
                firstWinner = poblacion.get(pos2);

            if(costList.get(pos3) > costList.get(pos4))
                secondWinner = poblacion.get(pos3);
            else
                secondWinner = poblacion.get(pos4);


            // Operador de cruce

            //Cruce de orden
            //generar posicion inicial
            //generar posicion final
            ArrayList<Integer> firstSon, secondSon, firstUsedValues, secondUsedValues;

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

            for(int i = firstPos; i <= secondPos; i++){

                //firstSon.add(firstWinner.)

            }


            //TODO Cruce PMX

            // Operador de mutacion

            //TODO Operador de mutacion

            // Recalcular coste

            cost = 0;
            cost2 = 0;

            // Calculamos el coste
            for (int i = 0; i < dimension; i++) {

                for (int k = 0; k < dimension; k++) {

                    if (i != k) {
                        cost += fluxMatrix[i][k] * distMatrix[firstWinner.get(i)][firstWinner.get(k)];
                        cost2 += fluxMatrix[i][k] * distMatrix[secondWinner.get(i)][secondWinner.get(k)];
                    }

                }

            }

            // Reemplazo respecto a la poblacion actual

            // Comprobacion con el primer ganador
            if(cost < buffer.get(buffer.size()-1).getValue()){
                int posToInsert = buffer.get(buffer.size()-1).getKey();
                poblacion.set(posToInsert, firstWinner);
                costList.set(posToInsert, cost);
                buffer.remove(buffer.size()-1);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost > buffer.get(i).getValue())
                        buffer.add(i+1, new Pair<Integer, Integer>(posToInsert, cost));
                }
            }else if(cost < buffer.get(buffer.size()-2).getValue()){
                int posToInsert = buffer.get(buffer.size()-2).getKey();
                poblacion.set(posToInsert, firstWinner);
                costList.set(posToInsert, cost);
                buffer.remove(buffer.size()-2);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost > buffer.get(i).getValue())
                        buffer.add(i+1, new Pair<Integer, Integer>(posToInsert, cost));
                }
            }

            // Comprobacion con el segundo ganador
            if(cost2 < buffer.get(buffer.size()-1).getValue()){
                int posToInsert = buffer.get(buffer.size()-1).getKey();
                poblacion.set(posToInsert, secondWinner);
                costList.set(posToInsert, cost2);
                buffer.remove(buffer.size()-1);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost2 > buffer.get(i).getValue())
                        buffer.add(i+1, new Pair<Integer, Integer>(posToInsert, cost2));
                }
            }else if(cost2 < buffer.get(buffer.size()-2).getValue()){
                int posToInsert = buffer.get(buffer.size()-2).getKey();
                poblacion.set(posToInsert, secondWinner);
                costList.set(posToInsert, cost2);
                buffer.remove(buffer.size()-2);
                for(int i = 0; i < buffer.size()-1; i++){
                    if(cost2 > buffer.get(i).getValue())
                        buffer.add(i+1, new Pair<Integer, Integer>(posToInsert, cost2));
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

            iteraciones++;
        }


        return 0;
    }


}