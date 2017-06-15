package segunda.entrega;

import static primeira.entrega.Tools.out;

import primeira.entrega.Stopwatch;
import terceira.entrega.hashtable.BinarySearchOrderedTable;

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

public class AlorithmExecutionTime {

	public static void ExcelWrite(int size, double time, long repetitions, long contiguousRepetitions,
			String typeOperation) {
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {

			// Verificar se ficheiro existe
			if (!new File("ResultadosOrdena��o.xlsx").exists()) {
				// Workbook em branco
				workbook = new XSSFWorkbook();

				// Criar folha em branco com "Insertion" ou "QuickSort"
				sheet = workbook.createSheet("Insertion");
				sheet = workbook.createSheet("QuickSort");

				// Obter a folha de c�lculo correta
				if (operation.matches("insertion")) {
					sheet = workbook.getSheetAt(0);
				} else {
					sheet = workbook.getSheetAt(1);
				}

			} else {

				// Buscar ficheiro
				FileInputStream file = new FileInputStream(new File("ResultadosOrdena��o.xlsx"));

				// Criar Workbook com a refer�ncia para o ficheiro .xlsx
				workbook = new XSSFWorkbook(file);

				// Obter a folha de c�lculo correta
				if (operation.matches("insertion")) {
					sheet = workbook.getSheetAt(0);
				} else {
					sheet = workbook.getSheetAt(1);
				}
			}

			// Numero de linhas preenchidas
			int numLinhas = sheet.getPhysicalNumberOfRows();

			sheet.setColumnWidth(0, 24 * 256);
			sheet.setColumnWidth(1, 24 * 256);
			sheet.setColumnWidth(2, 39 * 256);
			sheet.setColumnWidth(3, 36 * 256);
			sheet.setColumnWidth(4, 55 * 256);

			// Se o numero de linhas preenchidas for = 0 -> Criar cabe�alho
			if (numLinhas == 0) {

				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Tipo da Amostra");
				header.createCell(1).setCellValue("Tamanho da Amostra");
				header.createCell(2).setCellValue("Tempo decorrido por ordena��o(Mediana)");
				header.createCell(3).setCellValue("N�mero de experi�ncias");
				header.createCell(4).setCellValue("N�mero de repeti��es por experi�ncia");

				numLinhas = sheet.getPhysicalNumberOfRows();
			}

			// Registar resultado consoante numero de linhas preenchidas
			Row dataRow = sheet.createRow(numLinhas);
			dataRow.createCell(0).setCellValue(typeOperation);
			dataRow.createCell(1).setCellValue(size);
			dataRow.createCell(2).setCellValue(time);
			dataRow.createCell(3).setCellValue(repetitions);
			dataRow.createCell(4).setCellValue(contiguousRepetitions);

			// Criar/Gravar ficheiro .xlsx

			FileOutputStream out = new FileOutputStream(new File("ResultadosOrdena��o.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Or�amento de time por experi�ncia. Cada experi�ncia � repetida at� ser
	// gasto o or�amento.
	public static final double timeBudgetPerExperiment = 5.0 /* seconds */;

	// time minimo por repeti��o cont�gua
	public static final double minimumTimePerContiguousRepetitions = 1e-3 /* seconds */;

	// Guarda qual o algoritmo que vai ser executada
	public static String operation = "";

	// Calcula a mediana dos valores num array. Neste caso a mediana dos times
	// de execu��o
	public static double medianOf(final ArrayList<Double> values) {
		final int size = values.size();

		values.sort(null);

		if (size % 2 == 0)
			return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
		else
			return values.get(size / 2);
	}

	// Algortimo de ordena��o por inser��o
	public static Double[] insertion(final Double[] sortedValues) {

		Insertion.sort(sortedValues);
		operation = "insertion";
		return sortedValues;
	}

	// Algoritmo de ordena��o r�pida
	public static Double[] quicksort(final Double[] sortedValues) {

		Quicksort.sort(sortedValues);
		operation = "quicksort";
		return sortedValues;
	}

	// Guarda o array de valores j� ordenados
	private static Double[] sortedAlgorithm;

	// EStima o n�mero de repeti��es cont�guas necess�rias
	public static int contiguousRepetitionsFor(final Double[] originalValues) {
		double before = 0;
		double after = 0;
		double remaining = 0;
		Double[] sortedValues = originalValues.clone();
		// Se Inser��o
		if (operation == "insertion") {
			final Stopwatch stopwatch = new Stopwatch();
			int contiguousRepetitions = 0;
			do {
				sortedAlgorithm = insertion(sortedValues);
				before = stopwatch.elapsedTime();
				sortedValues = originalValues.clone();
				after = stopwatch.elapsedTime();
				remaining = after - before;
				contiguousRepetitions++;
			} while ((stopwatch.elapsedTime() - remaining) < minimumTimePerContiguousRepetitions);

			return contiguousRepetitions;
		}
		// Se Quicksort
		else {
			final Stopwatch stopwatch = new Stopwatch();
			int contiguousRepetitions = 0;
			do {
				sortedAlgorithm = quicksort(sortedValues);
				before = stopwatch.elapsedTime();
				sortedValues = originalValues.clone();
				after = stopwatch.elapsedTime();
				remaining = after - before;
				contiguousRepetitions++;
			} while ((stopwatch.elapsedTime() - remaining) < minimumTimePerContiguousRepetitions);

			return contiguousRepetitions;
		}
	}

	// Executa repeti��es cont�guas de uma experi�ncia para
	// obter os times de execu��o do algoritmo a ser testado
	public static double executionTimeFor(final Double[] originalValues, final int contiguousRepetitions) {

		Double[] sortedValues = originalValues.clone();
		double before = 0;
		double after = 0;
		double remaining = 0;
		if (operation == "insertion") {
			final Stopwatch stopwatch = new Stopwatch();
			for (int i = 0; i != contiguousRepetitions; i++) {
				sortedAlgorithm = insertion(sortedValues);
				before = stopwatch.elapsedTime();
				sortedValues = originalValues.clone();
				after = stopwatch.elapsedTime();
				remaining = after - before;
			}
			return (stopwatch.elapsedTime() - remaining) / contiguousRepetitions;
		} else {
			final Stopwatch stopwatch = new Stopwatch();
			for (int i = 0; i != contiguousRepetitions; i++) {
				sortedAlgorithm = quicksort(sortedValues);
				before = stopwatch.elapsedTime();
				sortedValues = originalValues.clone();
				after = stopwatch.elapsedTime();
				remaining = after - before;
			}
			return (stopwatch.elapsedTime() - remaining) / contiguousRepetitions;
		}
	}

	// Executa experi�ncias para obter uam estimativa de times de execu��o dos
	// algoritmos
	// at� o or�amento de time ser esgotado
	public static void performExperimentsFor(final Double[] originalValues, final boolean isWarmup,
			String typeOperation) {

		final ArrayList<Double> executionTimes = new ArrayList<Double>();
		final int contiguousRepetitions = contiguousRepetitionsFor(originalValues);
		long repetitions = 0;
		final Stopwatch stopwatch = new Stopwatch();
		do {

			executionTimes.add(executionTimeFor(originalValues, contiguousRepetitions));
			repetitions++;
		} while (stopwatch.elapsedTime() < timeBudgetPerExperiment);

		final double median = medianOf(executionTimes);

		if (!isWarmup) {
			ExcelWrite(originalValues.length, median, repetitions, contiguousRepetitions, typeOperation);
			out.println(originalValues.length + "\t" + median + "\t" + repetitions + "\t" + contiguousRepetitions);
		}
	}

	public static void main(final String[] arguments) throws InterruptedException {

		// Arrays que v�o guardar os valores contidos nos ficheiros pr� criados
		// com amostras aleat�rias, parcialmente ordenadas e ordenadas
		Double[] originalValuesShuffled = new Double[0];
		Double[] originalValuesPartiallySorted = new Double[0];
		Double[] originalValuesSorted = new Double[0];

		// Executa v�rias vezes cada algoritmo
		/*for (int ins = 0; ins <= 5; ins++) {
			if (ins == 0 || ins == 1 || ins == 2) {*/
				operation = "insertion";
			/*} else if (ins == 3 || ins == 4 || ins == 5) {
				operation = "quicksort";
			}*/

			// Executar o algoritmo a ser estudado nos v�rios sizes de amostras
			// de dados aleat�rios
			// Warmup
			/*for (int i = 2, exponent = 0; exponent != 24; exponent++, i *= 2) {

				final In inShuffled = new In("dados_sort/shuffled_" + i + ".txt");
				originalValuesShuffled = ArrayUtils.toObject(inShuffled.readAllDoubles());

				performExperimentsFor(originalValuesShuffled, true, "shuffled");

			}
			// Experi�ncias
			for (int i = 2, exponent = 0; exponent != 24; exponent++, i *= 2) {

				// Carregar os dados do respectivo ficheiro e guardar no array
				final In inShuffled = new In("dados_sort/shuffled_" + i + ".txt");
				originalValuesShuffled = ArrayUtils.toObject(inShuffled.readAllDoubles());

				performExperimentsFor(originalValuesShuffled, false, "shuffled");
			}

			// Executar o algoritmo a ser estudado nos v�rios sizes de amostras
			// de dados parcialmente ordenados
			for (int i = 2, exponent = 0; exponent != 24; exponent++, i *= 2) {

				// Carregar os dados do respectivo ficheiro e guardar no array
				final In inPartiallySorted = new In("dados_sort/partially_sorted_" + i + ".txt");
				originalValuesPartiallySorted = ArrayUtils.toObject(inPartiallySorted.readAllDoubles());

				performExperimentsFor(originalValuesPartiallySorted, false, "PartiallySorted");
			}
*/
			// Executar o algoritmo a ser estudado nos v�rios sizes de amostras
			// de dados ordenados
			for (int i = 2, exponent = 0; exponent != 24; exponent++, i *= 2) {

				// Carregar os dados do respectivo ficheiro e guardar no array
				final In inSorted = new In("dados_sort/sorted_" + i + ".txt");
				originalValuesSorted = ArrayUtils.toObject(inSorted.readAllDoubles());

				performExperimentsFor(originalValuesSorted, false, "Sorted");
			}

		}
}