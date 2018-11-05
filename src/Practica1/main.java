package Practica1;
import Functions.*;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        matricesDE matrix = new matricesDE("cnf01.dat");
        estacionario est = new estacionario(matrix.getDimension(), matrix.getFileName(), 77383310);
        System.out.print(est.estacionarioSolucion(matrix.getfluxMatrix(), matrix.getdistMatrix()));

    }

}
