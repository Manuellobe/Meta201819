package Functions;
import java.io.*;
import java.util.StringTokenizer;

public class matricesDE {


    // Atributos basicos para el almacenamiento de los datos
    private int dimension;
    private int[][] fluxMatrix;
    private int[][] distMatrix;

    // Constructor
    public matricesDE(String fileName){

        try{
            loadData(fileName);
        }catch (java.io.IOException exc){

            System.out.print (exc.getMessage());

        }

    }

    // Getters & Setters
    public int[][] getfluxMatrix(){
        return fluxMatrix;
    }

    public int[][] getdistMatrix(){
        return distMatrix;
    }

    public int getDimension(){
        return dimension;
    }


    // Carga los datos desde fichero y los almacenamos en matrices
    private void loadData(String file) throws java.io.IOException{

        // Ruta origen de datos
        File dataFile = new File("src/Datos/" + file);

        BufferedReader br = new BufferedReader(new FileReader(dataFile));

        String currentLine;

        if((currentLine = br.readLine()) == null) throw new IOException("El archivo de datos indicado no existe. " + file);

        StringTokenizer splitLine = new StringTokenizer(currentLine);


        // Inicializacion de parametros
        dimension = Integer.parseInt(splitLine.nextToken());
        fluxMatrix = new int[dimension][dimension];
        distMatrix = new int[dimension][dimension];

        // Leemos los datos de la matriz de flujo

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

        // Leemos los datos de la matriz de distancias

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

    }


}
