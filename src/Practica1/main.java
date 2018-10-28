package Practica1;
import Functions.*;

public class main {

    public static void main (String[] args) throws InterruptedException{

        matricesDE matrices = new matricesDE("cnf07.dat");
        enfriamientoSimulado Greedy = new enfriamientoSimulado(matrices.getDimension(), 1);

        System.out.print(Greedy.enfriamientoSolucion(matrices.getfluxMatrix(), matrices.getdistMatrix()));

    }

}