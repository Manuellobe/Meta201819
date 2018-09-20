package Functions;
import java.io.*;
import java.util.StringTokenizer;

public class matricesDE {

    private int dimension;

    private int[][] fluxMatrix;
    private int[][] distMatrix;

    public matricesDE(String fileName){

        try{
            loadData(fileName);
        }catch (java.io.IOException exc){

            System.out.print (exc.getMessage());

        }

    }

    public int[][] getfluxMatrix(){
        return fluxMatrix;
    }

    public int[][] getdistMatrix(){
        return distMatrix;
    }

    public int getDimension(){
        return dimension;
    }

    private void loadData(String file) throws java.io.IOException{

        File dataFile = new File("src/Datos/" + file);

        BufferedReader br = new BufferedReader(new FileReader(dataFile));

        String currentLine;

        if((currentLine = br.readLine()) == null) throw new IOException("El archivo de datos indicado no existe. " + file);

        StringTokenizer splitLine = new StringTokenizer(currentLine);

        dimension = Integer.parseInt(splitLine.nextToken());

        fluxMatrix = new int[dimension][dimension];
        distMatrix = new int[dimension][dimension];

        br.readLine();

        for(int i = 0; i < dimension; i++){

            currentLine = br.readLine();

            splitLine = new StringTokenizer(currentLine);

            int j = 0;
            while(splitLine.hasMoreTokens()){
                fluxMatrix[i][j] = Integer.parseInt(splitLine.nextToken());
                j++;
            }

        }

        br.readLine();

        for(int i = 0; i < dimension; i++){

            currentLine = br.readLine();

            splitLine = new StringTokenizer(currentLine);

            int j = 0;
            while(splitLine.hasMoreTokens()){
                distMatrix[i][j] = Integer.parseInt(splitLine.nextToken());
                j++;
            }

        }

        //DEBUG
        /*

        System.out.printf("Dimension: %d%n", dimension);
        System.out.print("Matriz de flujo:");
        System.out.println();

        for(int i = 0; i <dimension; i++){

            for(int j = 0; j < dimension; j++){

                System.out.printf("%d ", fluxMatrix[i][j]);

            }

            System.out.println();

        }

        System.out.println();

        System.out.print("Matriz de distancia:");
        System.out.println();

        for(int i = 0; i <dimension; i++){

            for(int j = 0; j < dimension; j++){

                System.out.printf("%d ", distMatrix[i][j]);

            }

            System.out.println();

        }

        */
    }


}
