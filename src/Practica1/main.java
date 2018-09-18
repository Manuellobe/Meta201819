package Practica1;
import Functions.*;

public class main {

    public static void main (String[] args) throws InterruptedException{

        matricesDE matrices = new matricesDE("cnf03.dat");
        greedy Greedy = new greedy(matrices.getDimension());

        System.out.print(Greedy.greedySolution(matrices.getfluxMatrix(), matrices.getdistMatrix()));

    }

}