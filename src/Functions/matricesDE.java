package Functions;
import java.io.*;
import java.util.StringTokenizer;

public class matricesDE {

    private int dimension;

    private int[][] matrizFlujo;
    private int[][] matrizDistancia;

    public matricesDE(String nombreFichero){

        try{
            loadData(nombreFichero);
        }catch (java.io.IOException exc){

            System.out.print (exc.getMessage());

        }

    }

    public int[][] getMatrizFlujo(){
        return matrizFlujo;
    }

    public int[][] getMatrizDistancia(){
        return matrizDistancia;
    }

    public int getDimension(){
        return dimension;
    }

    private void loadData(String file) throws java.io.IOException{

        File archivoDatos = new File("src/Datos/" + file);

        BufferedReader br = new BufferedReader(new FileReader(archivoDatos));

        String currentLine;

        if((currentLine = br.readLine()) == null) throw new IOException("El archivo de datos indicado no existe. " + file);

        dimension = Integer.parseInt(currentLine);

        matrizFlujo = new int[dimension][dimension];
        matrizDistancia = new int[dimension][dimension];

        br.skip(1);

        for(int i = 0; i < dimension; i++){

            currentLine = br.readLine();

            StringTokenizer lineaDividida = new StringTokenizer(currentLine);

            int j = 0;
            while(lineaDividida.hasMoreTokens()){
                matrizFlujo[i][j] = Integer.parseInt(lineaDividida.nextToken());
                j++;
            }

        }

        br.skip(1);

        for(int i = 0; i < dimension; i++){

            currentLine = br.readLine();

            StringTokenizer lineaDividida = new StringTokenizer(currentLine);

            int j = 0;
            while(lineaDividida.hasMoreTokens()){
                matrizDistancia[i][j] = Integer.parseInt(lineaDividida.nextToken());
                j++;
            }

        }

        //DEBUG


        System.out.printf("Dimension: %d%n", dimension);
        System.out.print("Matriz de flujo: %n");
        System.out.println();

        for(int i = 0; i <dimension; i++){

            for(int j = 0; j < dimension; j++){

                System.out.printf("%d ", matrizFlujo[i][j]);

            }

            System.out.println();

        }

        System.out.println();

        System.out.print("Matriz de distancia: %n");
        System.out.println();

        for(int i = 0; i <dimension; i++){

            for(int j = 0; j < dimension; j++){

                System.out.printf("%d ", matrizDistancia[i][j]);

            }

            System.out.println();

        }


    }


}
