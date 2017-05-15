package segunda.entrega;

import static primeira.entrega.Tools.in;
import static primeira.entrega.Tools.out;

import primeira.entrega.Stopwatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.princeton.cs.algs4.In;

public class SumExecutionTimeTester {
	
	
	public static void EscreverEmExcel(int tamanho, double tempo, long repeticoes){
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try{
			
		//Verificar se ficheiro existe
		if(!new File("ResultadosOrdenação.xlsx").exists()) {
			//Workbook em branco
			workbook = new XSSFWorkbook(); 
					
			//Criar folha em branco com "Insertion" ou "QuickSort"
			sheet = workbook.createSheet("Insertion");
			sheet = workbook.createSheet("QuickSort");
			
			//Obter a folha de cálculo correta
			if (operacao.matches("insertion")){
			sheet = workbook.getSheetAt(0);
			}else{
				sheet = workbook.getSheetAt(1);
			}
		
		}
		else {
			
			//Buscar ficheiro
			FileInputStream file = new FileInputStream(new File("ResultadosOrdenação.xlsx"));
			
			//Criar Workbook com a referência para o ficheiro .xlsx
			workbook = new XSSFWorkbook(file);

			//Obter a folha de cálculo correta
			if (operacao.matches("insertion")){
			sheet = workbook.getSheetAt(0);
			}else{
				sheet = workbook.getSheetAt(1);
			}
		}
		
		//Numero de linhas preenchidas
		int numLinhas = sheet.getPhysicalNumberOfRows();
		
		sheet.setColumnWidth(0, 21*256);
		sheet.setColumnWidth(1, 39*256);
		sheet.setColumnWidth(2, 36*256);
		
		//Se o numero de linhas preenchidas for = 0 -> Criar cabeçalho
		if(numLinhas == 0){
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Tamanho da Amostra");
		header.createCell(1).setCellValue("Tempo decorrido por ordenação(Mediana)");
		header.createCell(2).setCellValue("Número de repetições por experiência");
		
		numLinhas = sheet.getPhysicalNumberOfRows();
		}
		
		//Registar resultado consoante numero de linhas preenchidas
		Row dataRow = sheet.createRow(numLinhas);
		
	    dataRow.createCell(0).setCellValue(tamanho);
	    dataRow.createCell(1).setCellValue(tempo);
	    dataRow.createCell(2).setCellValue(repeticoes);
	    
	    //Criar/Gravar ficheiro .xlsx
	    
	    FileOutputStream out =  new FileOutputStream(new File("ResultadosOrdenação.xlsx"));
	    workbook.write(out);
	    out.close();
	    System.out.println("Excel written successfully..");
	         
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
}
	
   
    public static final double timeBudgetPerExperiment = 5.0 /* seconds */;

    public static final double minimumTimePerContiguousRepetitions = 1e-1 /* seconds */;

    public static String operacao = "";
   
    public static double medianOf(final ArrayList<Double> values) {
        final int size = values.size();

        values.sort(null);

        if (size % 2 == 0)
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
        else
            return values.get(size / 2);
    }

    public static Double[] insertion(final Double[] sortedValues) {
       
    	Insertion.sort(sortedValues);
    	operacao = "insertion";
        return sortedValues;
    }
    
    public static Double[] quicksort(final Double[] sortedValues) {
        
    	Quicksort.sort(sortedValues);
    	operacao = "quicksort";
        return sortedValues;
    }

    private static Double[] sortedAlgorithm;

    public static int contiguousRepetitionsFor(final Double[] sortedValues) {
    	
    	if(operacao == "insertion"){
    		final Stopwatch stopwatch = new Stopwatch();
    		int contiguousRepetitions = 0;
    		do {
    			sortedAlgorithm = insertion(sortedValues);
    			contiguousRepetitions++;
    		} while (stopwatch.elapsedTime() < minimumTimePerContiguousRepetitions);

    		return contiguousRepetitions;
    	}
    	else{
    		final Stopwatch stopwatch = new Stopwatch();
            int contiguousRepetitions = 0;
            do {
            	sortedAlgorithm = quicksort(sortedValues);
                contiguousRepetitions++;
            } while (stopwatch.elapsedTime() < minimumTimePerContiguousRepetitions);

            return contiguousRepetitions;
    	}
    }

    public static double executionTimeFor(final Double[] sortedValues,
            final int contiguousRepetitions) {
    	if(operacao == "insertion"){
    		final Stopwatch stopwatch = new Stopwatch();
    		for (int i = 0; i != contiguousRepetitions; i++)
    			sortedAlgorithm = insertion(sortedValues);
    	
    		return stopwatch.elapsedTime() / contiguousRepetitions;
    	} 
    	else{
    		final Stopwatch stopwatch = new Stopwatch();
    	    for (int i = 0; i != contiguousRepetitions; i++)
    	    	sortedAlgorithm = quicksort(sortedValues);
    	    	
    	    return stopwatch.elapsedTime() / contiguousRepetitions;
    	}
    }

    public static void performExperimentsFor( final Double[] originalValues,
            final boolean isWarmup) {
    	
    	
        final Double[] sortedValues = originalValues.clone();
        /*final Stopwatch stopwatchinsmoves = new Stopwatch();
        InsertionWithMoves.sort(sortedValues);
        double tempoinsmoves = stopwatchinsmoves.elapsedTime();*/
    	
        final ArrayList<Double> executionTimes = new ArrayList<Double>();
        final int contiguousRepetitions = contiguousRepetitionsFor(sortedValues);
        long repetitions = 0;
        final Stopwatch stopwatch = new Stopwatch();
        do {
        	
            executionTimes.add(executionTimeFor(sortedValues, contiguousRepetitions));
            repetitions++;
        } while (stopwatch.elapsedTime() < timeBudgetPerExperiment);

        final double median = medianOf(executionTimes);

        if (!isWarmup)
        	EscreverEmExcel(originalValues.length, median, repetitions);
            out.println(
            		originalValues.length + "\t" + median + "\t" + repetitions + "\t"+ contiguousRepetitions);
        /*-
        out.println("Sum from 1 to " + limit + " = " + sum + " [" + median
                + "s median time based on " + repetitions
                + " repetitions of " + contiguousRepetitions
                + " contiguous repetitions]");
        */
    }

    public static void main(final String[] arguments)
            throws InterruptedException {
    	out.println("Escolha o tipo de ordenação (1 - Insertion || 2 - Quicksort)");
    	int op =  Integer.parseInt(in.nextLine());
    	if(op > 0 && op < 3){
    	if(op == 1)
    	{operacao = "insertion";}
    	else if(op == 2)
    	{operacao = "quicksort";}
    	}
    	else{out.println("Operação inválida"); System.exit(0);}
    	
    	Double[] originalValues = new Double[0];
    	
    	for(int i = 2, expoente = 0; expoente != 5; expoente++, i *= 2){
    		final In in = new In("dados_sort/shuffled_"+i+".txt");
    		originalValues = ArrayUtils.toObject(in.readAllDoubles());
    		
    		// performExperimentsFor(originalValues, true);

             performExperimentsFor(originalValues, false);
    	}
    	
    	
    	
       
           
    }

}