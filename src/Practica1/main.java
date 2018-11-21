package Practica1;
import Functions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws java.io.IOException {

        matricesDE matrix = new matricesDE("cnf01.dat");
        estacionario est = new estacionario(matrix.getDimension(), matrix.getFileName(), 77383310, 50, 100);
        System.out.print(est.estacionarioSolucion(matrix.getfluxMatrix(), matrix.getdistMatrix()));

        /*
        File dataFile = new File("src/Datos/config.txt");

        BufferedReader br = new BufferedReader(new FileReader(dataFile));

        if (br.readLine() == null) {
            throw new IOException("El archivo de configuración no existe. ");
        }

        String file = br.readLine();
        br.readLine();

        String alg = br.readLine();
        br.readLine();

        int seed = Integer.parseInt(br.readLine());
        br.readLine();

        int iteraciones = Integer.parseInt(br.readLine());
        br.readLine();

        int probMut = Integer.parseInt(br.readLine());
        br.readLine();

        int probCruce = Integer.parseInt(br.readLine());


        //TODO parametrizar las funciones

        br.close();
        */
    }

}
