package Functions;

import java.util.ArrayList;

public class estacionario {

    private int dimension;
    private int randomSeed;
    private int nPopulation;
    private ArrayList<String> logger;
    private String file;

    public estacionario(int nDimension, String fileName){

        dimension = nDimension;
        randomSeed = 77383310;
        nPopulation = 50;

        logger = new ArrayList<>();
        file = fileName;

    }


}


// Poblacion = 50 individuos
// Poblacion aleatoria completamente
// Soluciones factibles
// Parada 50000 iteraciones de la funcion objetivo
// Torneo binario aleatorio