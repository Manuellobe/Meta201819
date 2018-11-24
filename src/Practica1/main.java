package Practica1;
import Functions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws java.io.IOException {

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

        int pop = Integer.parseInt(br.readLine());
        br.readLine();

        int iteSinMejora = Integer.parseInt(br.readLine());
        br.readLine();

        float probMut = Float.parseFloat(br.readLine());
        br.readLine();

        float probCruce = Float.parseFloat(br.readLine());
        br.close();

        matricesDE matrix = new matricesDE(file);

        if(alg == "est"){

            estacionario est = new estacionario(matrix.getDimension(), matrix.getFileName(), seed, iteraciones, pop, iteSinMejora, probMut);
            System.out.print(est.estacionarioSolucion(matrix.getfluxMatrix(), matrix.getdistMatrix()));

        }else{

            generacional gen = new generacional(matrix.getDimension(), matrix.getFileName(), seed, iteraciones, pop, iteSinMejora, probMut, probCruce);
            System.out.print(gen.generacionalSolucion(matrix.getfluxMatrix(), matrix.getdistMatrix()));

        }

    }

}
