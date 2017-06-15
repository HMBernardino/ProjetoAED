package terceira.entrega.hashtable;

import static primeira.entrega.Tools.out;

import primeira.entrega.Stopwatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BstOrderedTableTester {

	public static void ExcelWrite(int size, double time, long repetitions, long contiguousRepetitions) {
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {

			// Verificar se ficheiro existe
			if (!new File("ResultadosBST.xlsx").exists()) {
				// Workbook em branco
				workbook = new XSSFWorkbook();

				// Criar folha em branco com "Insertion" ou "QuickSort"
				sheet = workbook.createSheet("Insertion");
				sheet = workbook.createSheet("Update");
				sheet = workbook.createSheet("Delete");
				sheet = workbook.createSheet("Search");

				// Obter a folha de cálculo correta
				if (typeOperation == "Insertion") {
					sheet = workbook.getSheetAt(0);
				}
				if (typeOperation == "Update") {
					sheet = workbook.getSheetAt(1);
				}
				if (typeOperation == "Delete") {
					sheet = workbook.getSheetAt(2);
				}
				if (typeOperation == "Search") {
					sheet = workbook.getSheetAt(3);
				}

			} else {

				// Buscar ficheiro
				FileInputStream file = new FileInputStream(new File("ResultadosBST.xlsx"));

				// Criar Workbook com a referência para o ficheiro .xlsx
				workbook = new XSSFWorkbook(file);

				sheet = null;
				// Obter a folha de cálculo correta
				if (typeOperation == "Insertion") {
					sheet = workbook.getSheetAt(0);
				}
				if (typeOperation == "Update") {
					sheet = workbook.getSheetAt(1);
				}
				if (typeOperation == "Delete") {
					sheet = workbook.getSheetAt(2);
				}
				if (typeOperation == "Search") {
					sheet = workbook.getSheetAt(3);
				}
			}

			// Numero de linhas preenchidas
			int numberOfLines = sheet.getPhysicalNumberOfRows();

			sheet.setColumnWidth(0, 24 * 256);
			sheet.setColumnWidth(1, 39 * 256);
			sheet.setColumnWidth(2, 36 * 256);
			sheet.setColumnWidth(3, 55 * 256);

			// Se o numero de linhas preenchidas for = 0 -> Criar cabeçalho
			if (numberOfLines == 0) {

				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Tamanho da Amostra");
				header.createCell(1).setCellValue("Tempo decorrido por ordenação(Mediana)");
				header.createCell(2).setCellValue("Número de experiências");
				header.createCell(3).setCellValue("Número de repetições por experiência");

				numberOfLines = sheet.getPhysicalNumberOfRows();
			}

			// Registar resultado consoante numero de linhas preenchidas
			Row dataRow = sheet.createRow(numberOfLines);
			dataRow.createCell(0).setCellValue(size);
			dataRow.createCell(1).setCellValue(time);
			dataRow.createCell(2).setCellValue(repetitions);
			dataRow.createCell(3).setCellValue(contiguousRepetitions);

			// Criar/Gravar ficheiro .xlsx

			FileOutputStream out = new FileOutputStream(new File("ResultadosBST.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Orçamento de time por experiência. Cada experiência é repetida até ser
	// gasto o orçamento.
	public static final double timeBudgetPerExperiment = 5.0 /* seconds */;

	// time minimo por repetição contígua
	public static final double minimumTimePerContiguousRepetitions = 1e-2 /* seconds */;

	// Calcula a mediana dos valores num array. Neste caso a mediana dos times
	// de execução
	public static double medianOf(final ArrayList<Double> values) {
		final int size = values.size();

		values.sort(null);

		if (size % 2 == 0)
			return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
		else
			return values.get(size / 2);
	}

	// Variaveis de auxilio
	public static String typeOperation = "";

	// Inserção de dados em BinarySearchOrderedTable
	// BSTOrderedTable
	// SequencialSearchTable
	@SuppressWarnings("unchecked")
	public static void ins_BST(BstOrderedTable table, String key, int value) {
		table.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public static void del_BST(BstOrderedTable table, String key) {
		table.delete(key);
	}

	@SuppressWarnings("unchecked")
	public static void search_BSOT(BstOrderedTable table, String key) {
		table.valueFor(key);
	}

	//////////////////////////// INSERIR/UPDATE //////////////////////////
	// Estima o número de repetições contíguas necessárias
	public static int contiguousRepetitionsFor(BstOrderedTable table) {
		double before = 0;
		double after = 0;
		double remaining = 0;
		int contiguousRepetitions = 0;

		if (typeOperation == "Update") {
			for (int j = 0; j < 100; j++) {
				ins_BST(table, String.valueOf(j), j);
			}

			final Stopwatch stopwatch = new Stopwatch();
			contiguousRepetitions = 0;
			do {
				for (int j = 0; j < 100; j++) {
					ins_BST(table, String.valueOf(j), j);
				}
				contiguousRepetitions++;
			} while ((stopwatch.elapsedTime()) < minimumTimePerContiguousRepetitions);
		} else if (typeOperation == "Insertion") {

			final Stopwatch stopwatch = new Stopwatch();
			contiguousRepetitions = 0;
			do {
				for (int j = 0; j < 100; j++) {
					ins_BST(table, String.valueOf(j), j);
				}
				before = stopwatch.elapsedTime();
				table = new BstOrderedTable<>();
				after = stopwatch.elapsedTime();
				remaining = after - before;
				contiguousRepetitions++;
			} while ((stopwatch.elapsedTime() - remaining) < minimumTimePerContiguousRepetitions);
		}

		return contiguousRepetitions;

	}

	// Executa repetições contíguas de uma experiência para
	// obter os times de execução do algoritmo a ser testado
	public static double executionTimeFor(BstOrderedTable table, final int contiguousRepetitions) {
		double before = 0;
		double after = 0;
		double remaining = 0;
		if (typeOperation == "Update") {
			for (int j = 0; j < 100; j++) {
				ins_BST(table, String.valueOf(j), j);
			}
		}

		final Stopwatch stopwatch = new Stopwatch();
		for (int i = 0; i != contiguousRepetitions; i++) {
			for (int j = 0; j < 100; j++) {
				ins_BST(table, String.valueOf(j), j);
			}
			if (typeOperation == "Insertion") {
				before = stopwatch.elapsedTime();
				table = new BstOrderedTable<>();
				after = stopwatch.elapsedTime();
				remaining = after - before;
			}
		}
		if (typeOperation == "Insertion") {
			return (stopwatch.elapsedTime() - remaining) / contiguousRepetitions;
		} else {
			return stopwatch.elapsedTime() / contiguousRepetitions;
		}
	}

	//////////////////////////// Delete/Search
	//////////////////////////// //////////////////////////////////////
	// Executa repetições contíguas de uma experiência para
	// obter os times de execução do algoritmo a ser testado
	public static double executionTimeForDelete(BstOrderedTable table, final int contiguousRepetitions) {
		double before = 0;
		double after = 0;
		double remaining = 0;
		final Stopwatch stopwatch = new Stopwatch();
		for (int i = 0; i != contiguousRepetitions; i++)

			before = stopwatch.elapsedTime();
		for (int j = 0; j < 100; j++) {
			ins_BST(table, String.valueOf(j), j);
		}
		after = stopwatch.elapsedTime();
		remaining = after - before;
		if (typeOperation == "Delete") {
			for (int j = 0; j < 100; j++) {
				del_BST(table, String.valueOf(j));
			}
		}
		if (typeOperation == "Search") {
			for (int j = 0; j < 100; j++) {
				search_BSOT(table, String.valueOf(j));
			}
		}
		return (stopwatch.elapsedTime() - remaining) / contiguousRepetitions;

	}

	public static int contiguousRepetitionsForDelete(BstOrderedTable table) {
		double before = 0;
		double after = 0;
		double remaining = 0;
		final Stopwatch stopwatch = new Stopwatch();
		int contiguousRepetitions = 0;
		do {
			before = stopwatch.elapsedTime();
			for (int j = 0; j < 100; j++) {
				ins_BST(table, String.valueOf(j), j);
			}
			after = stopwatch.elapsedTime();
			remaining = after - before;
			if (typeOperation == "Delete") {
				for (int j = 0; j < 100; j++) {
					del_BST(table, String.valueOf(j));
				}
			}
			if (typeOperation == "Search") {
				for (int j = 0; j < 100; j++) {
					search_BSOT(table, String.valueOf(j));
				}
			}
			contiguousRepetitions++;
		} while ((stopwatch.elapsedTime() - remaining) < minimumTimePerContiguousRepetitions);

		return contiguousRepetitions;

	}

	// Executa experiências para obter uma estimativa de times de execução dos
	// algoritmos
	// até o orçamento de time ser esgotado
	public static void performExperimentsFor(BstOrderedTable table, final boolean isWarmup) {
		if (typeOperation == "Insertion") {
			final ArrayList<Double> executionTimes = new ArrayList<Double>();
			final int contiguousRepetitions = contiguousRepetitionsFor(table);
			long repetitions = 0;
			final Stopwatch stopwatch = new Stopwatch();
			do {
				executionTimes.add(executionTimeFor(table, contiguousRepetitions));
				repetitions++;
			} while (stopwatch.elapsedTime() < timeBudgetPerExperiment);

			final double median = medianOf(executionTimes);
			if (!isWarmup) {
				ExcelWrite(table.size(), median, repetitions, contiguousRepetitions);
				out.println(table.size() + "\t" + median + "\t" + repetitions + "\t" + contiguousRepetitions);
			}
		}

		if (typeOperation == "Delete") {
			// Inserir
			final ArrayList<Double> executionTimes = new ArrayList<Double>();
			final int contiguousRepetitions = contiguousRepetitionsFor(table);
			final Stopwatch stopwatch = new Stopwatch();
			do {
				executionTimes.add(executionTimeFor(table, contiguousRepetitions));
			} while (stopwatch.elapsedTime() < timeBudgetPerExperiment);
			final int sizetable = table.size();
			// Fim Inserir
			// Delete
			final ArrayList<Double> executionTimesDelete = new ArrayList<Double>();
			final int contiguousRepetitionsDelete = contiguousRepetitionsForDelete(table);
			long repetitionsDelete = 0;
			final Stopwatch stopwatchDelete = new Stopwatch();
			do {
				executionTimesDelete.add(executionTimeForDelete(table, contiguousRepetitionsDelete));
				repetitionsDelete++;
			} while (stopwatchDelete.elapsedTime() < timeBudgetPerExperiment);
			final double medianDelete = medianOf(executionTimesDelete);
			// Fim Delete

			if (!isWarmup) {
				ExcelWrite(sizetable, medianDelete, repetitionsDelete, contiguousRepetitionsDelete);
				out.println(sizetable + "\t" + medianDelete + "\t" + repetitionsDelete + "\t"
						+ contiguousRepetitionsDelete);
			}
		}
		if (typeOperation == "Update") {

			// Inserir
			final ArrayList<Double> executionTimes = new ArrayList<Double>();
			final int contiguousRepetitions = contiguousRepetitionsFor(table);
			final Stopwatch stopwatch = new Stopwatch();
			do {
				executionTimes.add(executionTimeFor(table, contiguousRepetitions));
			} while (stopwatch.elapsedTime() < timeBudgetPerExperiment);
			// Fim Inserir
			// Update
			final ArrayList<Double> executionTimesUpdate = new ArrayList<Double>();
			final int contiguousRepetitionsUpdate = contiguousRepetitionsFor(table);
			long repetitionsUpdate = 0;
			final Stopwatch stopwatchUpdate = new Stopwatch();
			do {
				executionTimesUpdate.add(executionTimeFor(table, contiguousRepetitionsUpdate));
				repetitionsUpdate++;
			} while (stopwatchUpdate.elapsedTime() < timeBudgetPerExperiment);
			final double medianUpdate = medianOf(executionTimesUpdate);
			// Fim Update

			if (!isWarmup) {
				ExcelWrite(table.size(), medianUpdate, repetitionsUpdate, contiguousRepetitionsUpdate);
				out.println(table.size() + "\t" + medianUpdate + "\t" + repetitionsUpdate + "\t"
						+ contiguousRepetitionsUpdate);
			}
		}
		if (typeOperation == "Search") {

			// Delete
			final ArrayList<Double> executionTimesSearch = new ArrayList<Double>();
			final int contiguousRepetitionsSearch = contiguousRepetitionsForDelete(table);
			long repetitionsSearch = 0;
			final Stopwatch stopwatchSearch = new Stopwatch();
			do {
				executionTimesSearch.add(executionTimeForDelete(table, contiguousRepetitionsSearch));
				repetitionsSearch++;
			} while (stopwatchSearch.elapsedTime() < timeBudgetPerExperiment);
			final double medianSearch = medianOf(executionTimesSearch);
			// Fim Delete

			if (!isWarmup) {
				ExcelWrite(table.size(), medianSearch, repetitionsSearch, contiguousRepetitionsSearch);
				out.println(table.size() + "\t" + medianSearch + "\t" + repetitionsSearch + "\t"
						+ contiguousRepetitionsSearch);
			}
		}

	}

	public static void main(final String[] arguments) {

		// Inicialização de tabelas de símbolos
		final BstOrderedTable<String, Integer> table = new BstOrderedTable<>();
		assert table.size() == 0;
		assert table.isEmpty();

		typeOperation = "Insertion";
		performExperimentsFor(table, true);
		performExperimentsFor(table, false);
		typeOperation = "Delete";
		performExperimentsFor(table, false);
		typeOperation = "Update";
		performExperimentsFor(table, false);
		typeOperation = "Search";
		performExperimentsFor(table, false);

	}
}
