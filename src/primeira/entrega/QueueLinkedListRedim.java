package primeira.entrega;

import static primeira.entrega.Tools.out;

import primeira.entrega.Stopwatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QueueLinkedListRedim {

	public static void ExcelWrite(int size, double time, long repetitions, long contiguousRepetitions,
			String typeOperation) {
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {

			// Verificar se ficheiro existe
			if (!new File("ResultadosLinkedList.xlsx").exists()) {
				// Workbook em branco
				workbook = new XSSFWorkbook();

				// Criar folha em branco com "Insertion" ou "QuickSort"
				sheet = workbook.createSheet("Insert");
				sheet = workbook.createSheet("Remove");

				// Obter a folha de c�lculo correta
				if (operation.matches("Insert")) {
					sheet = workbook.getSheetAt(0);
				} else {
					sheet = workbook.getSheetAt(1);
				}

			} else {

				// Buscar ficheiro
				FileInputStream file = new FileInputStream(new File("ResultadosLinkedList.xlsx"));

				// Criar Workbook com a refer�ncia para o ficheiro .xlsx
				workbook = new XSSFWorkbook(file);

				// Obter a folha de c�lculo correta
				if (operation.matches("Insert")) {
					sheet = workbook.getSheetAt(0);
				} else {
					sheet = workbook.getSheetAt(1);
				}
			}

			// Numero de linhas preenchidas
			int numberOfLines = sheet.getPhysicalNumberOfRows();

			sheet.setColumnWidth(0, 24 * 256);
			sheet.setColumnWidth(1, 24 * 256);
			sheet.setColumnWidth(2, 39 * 256);
			sheet.setColumnWidth(3, 36 * 256);
			sheet.setColumnWidth(4, 55 * 256);
			sheet.setColumnWidth(5, 24 * 256);

			// Se o numero de linhas preenchidas for = 0 -> Criar cabe�alho
			if (numberOfLines == 0) {

				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Tipo da Amostra");
				header.createCell(1).setCellValue("Tamanho da Amostra");
				header.createCell(2).setCellValue("Tempo decorrido por Opera��o(Mediana)");
				header.createCell(3).setCellValue("N�mero de experi�ncias");
				header.createCell(4).setCellValue("N�mero de repeti��es por experi�ncia");
				header.createCell(5).setCellValue("Mem�ria Ocupada");

				numberOfLines = sheet.getPhysicalNumberOfRows();
			}

			// Registar resultado consoante numero de linhas preenchidas
			Row dataRow = sheet.createRow(numberOfLines);
			dataRow.createCell(0).setCellValue(typeOperation);
			dataRow.createCell(1).setCellValue(size);
			dataRow.createCell(2).setCellValue(time);
			dataRow.createCell(3).setCellValue(repetitions);
			dataRow.createCell(4).setCellValue(contiguousRepetitions);
			dataRow.createCell(5).setCellValue(usedMemory);

			// Criar/Gravar ficheiro .xlsx

			FileOutputStream out = new FileOutputStream(new File("ResultadosLinkedList.xlsx"));
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
	// Guarda o int a inserir
	static int r = 69;
	// Guarda a mem�ria usada
	static long usedMemory = 0;

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
	public static QueueLinkedList Insert(final QueueLinkedList numbers) {
		numbers.enqueue(r);
		return numbers;
	}

	// Algoritmo de ordena��o r�pida
	public static QueueLinkedList Remove(final QueueLinkedList numbers) {
		numbers.dequeue();
		return numbers;
	}

	// EStima o n�mero de repeti��es cont�guas necess�rias
	public static int contiguousRepetitionsFor(final QueueLinkedList numbers) {
		double before = 0;
		double after = 0;
		double remaining = 0;
		// Se Inser��o
		if (operation == "Insert") {
			final Stopwatch stopwatch = new Stopwatch();
			int contiguousRepetitions = 0;
			do {
				Insert(numbers);
				contiguousRepetitions++;
			} while (stopwatch.elapsedTime() < minimumTimePerContiguousRepetitions);

			return contiguousRepetitions;
		}
		// Se Remo��o
		else {
			final Stopwatch stopwatch = new Stopwatch();
			int contiguousRepetitions = 0;
			do {
				before = stopwatch.elapsedTime();
				Insert(numbers);
				after = stopwatch.elapsedTime();
				remaining = after - before;
				Remove(numbers);
				contiguousRepetitions++;
			} while ((stopwatch.elapsedTime() - remaining) < minimumTimePerContiguousRepetitions);

			return contiguousRepetitions;
		}
	}

	// Executa repeti��es cont�guas de uma experi�ncia para
	// obter os times de execu��o do algoritmo a ser testado
	public static double executionTimeFor(final QueueLinkedList numbers, final int contiguousRepetitions) {
		double before = 0;
		double after = 0;
		double remaining = 0;

		if (operation == "Insert") {
			final Stopwatch stopwatch = new Stopwatch();
			for (int i = 0; i != contiguousRepetitions; i++) {
				Insert(numbers);
				before = stopwatch.elapsedTime();
				// Mem�ria ocupada pela fila
				// size(Bytes):
				// Classe:FilaLinkedList
				// -Header do objecto:16
				// -Referencia primeiro:8
				// -Referencia ultimo:8
				// -int size:4
				// -Padding:4
				// FilaLinkedList = 40

				// Node:
				// -Referencia classe FilaLinkedList :8
				// -Header do objecto:16
				// -int[] item:8
				// -Referencia Node.next:8

				// FilaLinkedList(40) + Node(40) * FilaLinkedList.size
				usedMemory = numbers.usedMemory();
				after = stopwatch.elapsedTime();
				remaining = after - before;
			}
			return (stopwatch.elapsedTime() - remaining) / contiguousRepetitions;
		} else {
			final Stopwatch stopwatch = new Stopwatch();
			for (int i = 0; i != contiguousRepetitions; i++) {
				before = stopwatch.elapsedTime();
				Insert(numbers);
				usedMemory = numbers.usedMemory();
				after = stopwatch.elapsedTime();
				remaining = after - before;
				Remove(numbers);
			}
			return (stopwatch.elapsedTime() - remaining) / contiguousRepetitions;
		}
	}

	// Executa experi�ncias para obter uam estimativa de times de execu��o dos
	// algoritmos
	// at� o or�amento de time ser esgotado
	public static void performExperimentsFor(final QueueLinkedList numbers, final boolean isWarmup,
			String typeOperation) {

		final ArrayList<Double> executionTimes = new ArrayList<Double>();
		final int contiguousRepetitions = contiguousRepetitionsFor(numbers);
		long repetitions = 0;
		final Stopwatch stopwatch = new Stopwatch();
		do {

			executionTimes.add(executionTimeFor(numbers, contiguousRepetitions));
			repetitions++;
		} while (stopwatch.elapsedTime() < timeBudgetPerExperiment);

		final double median = medianOf(executionTimes);

		if (!isWarmup) {
			ExcelWrite(numbers.size(), median, repetitions, contiguousRepetitions, typeOperation);
			out.println(numbers.size() + "\t" + median + "\t" + repetitions + "\t" + contiguousRepetitions);
		}
	}

	public static void main(final String[] arguments) throws InterruptedException {
		QueueLinkedList numbers = new QueueLinkedList();

		for(int i = 0; i != 6; i++){
			if(i == 0 || i == 1 || i == 2){operation = "Insert";}
			if(i == 3 || i == 4 || i == 5){operation = "Remove";}
		performExperimentsFor(numbers, true, operation);
		performExperimentsFor(numbers, false, operation);
		performExperimentsFor(numbers, false, operation);
		performExperimentsFor(numbers, false, operation);
		performExperimentsFor(numbers, false, operation);
		}

	}
}