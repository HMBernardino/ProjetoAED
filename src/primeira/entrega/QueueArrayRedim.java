package primeira.entrega;

import static primeira.entrega.Tools.out;
import static primeira.entrega.Tools.in;
import static primeira.entrega.ObjectSizeFetcher.sizeOf;
import primeira.entrega.Stopwatch;

import edu.princeton.cs.algs4.ResizingArrayQueue;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QueueArrayRedim {

	public static void ExcelWrite(int size, double time, String operation, long usedMemory) {
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {

			// Verificar se ficheiro existe
			if (!new File("ResultadosArray.xlsx").exists()) {
				// Workbook em branco
				workbook = new XSSFWorkbook();

				// Criar folha em branco com "ResultadosInserir" ou
				// "ResultadosRemover"
				sheet = workbook.createSheet("ResultadosInserir");
				sheet = workbook.createSheet("ResultadosRemover");

				// Obter a folha de c�lculo correta
				if (operation.matches("Inserir.*")) {
					sheet = workbook.getSheetAt(0);
				} else {
					sheet = workbook.getSheetAt(1);
				}

			} else {

				// Buscar ficheiro
				FileInputStream file = new FileInputStream(new File("ResultadosArray.xlsx"));

				// Criar Workbook com a refer�ncia para o ficheiro .xlsx
				workbook = new XSSFWorkbook(file);

				// Obter a folha de c�lculo correta
				if (operation.matches("Inserir.*")) {
					sheet = workbook.getSheetAt(0);
				} else {
					sheet = workbook.getSheetAt(1);
				}
			}

			// Numero de linhas preenchidas
			int numberOfLines = sheet.getPhysicalNumberOfRows();

			sheet.setColumnWidth(0, 22 * 256);
			sheet.setColumnWidth(1, 20 * 256);
			sheet.setColumnWidth(2, 21 * 256);
			sheet.setColumnWidth(3, 26 * 256);

			// Se o numero de linhas preenchidas for = 0 -> Criar cabe�alho
			if (numberOfLines == 0) {

				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("N�mero de inteiros");
				header.createCell(1).setCellValue("time decorrido");
				header.createCell(2).setCellValue("time por opera��o");
				header.createCell(3).setCellValue("Mem�ria Ocupada(Bytes)");

				numberOfLines = sheet.getPhysicalNumberOfRows();
			}

			// Registar resultado consoante numero de linhas preenchidas
			Row dataRow = sheet.createRow(numberOfLines);

			dataRow.createCell(0).setCellValue(size);
			dataRow.createCell(1).setCellValue(time);
			dataRow.createCell(2).setCellValue(time / size);
			dataRow.createCell(3).setCellValue(usedMemory);

			// Criar/Gravar ficheiro .xlsx

			FileOutputStream out = new FileOutputStream(new File("ResultadosArray.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		int r = 69;
		String operation = "";
		double time = 0;
		long usedMemory = 0;
		out.println("Escolha o tipo de opera��o (Inserir/Remover)");
		String oper = in.nextLine();
		out.println("Escolha o size da amostra (>1000)");
		int kints = in.nextInt();
		// Escolha da opera��o e size da amostra
		operation = oper + kints;
		out.println("OP: " + operation);

		switch (oper) {
		case "Inserir":
			for (int x = 0; x < 0; x++) {
				ResizingArrayQueue<Integer> numbers = new ResizingArrayQueue<Integer>();
				// Inser��o do n�mero da amostra selecionado no array (enqueue)
				if (kints >= 1000) {
					// In�cio da inser��o
					final Stopwatch stopwatchInsertion = new Stopwatch();
					for (int i = 0; i != kints; i++) {
						numbers.enqueue(r);
					}
					// Paragem do contador do time de execu��o
					time = stopwatchInsertion.elapsedTime();
					out.println(time);
					// Mem�ria ocupada pela fila
					// Verificar n�mero de elementos no array ***
					// size(Bytes):
					// Classe:
					// -Header do objecto:16
					// -int[] numbers:8
					// -int size:4
					// -int primeiro:4
					// -int ultimo:4
					// -Padding:4
					// ResizingArrayQueue = 40
					// ResizingArrayQueue + Espa�o reservado do Array(8)
					// ResizingArrayQueue.sizeOf() + 8 * ResizingArrayQueue.size
					if (numbers.size() == 1) {
						usedMemory = sizeOf(numbers);
					} else {
						usedMemory = sizeOf(numbers) + 8 * numbers.size();
					}
					// Escrita em excel
					ExcelWrite(numbers.size(), time, operation, usedMemory);
				} else {
					out.println("Tamanho inv�lido");
				}
			}
			break;

		case "Remover":
			for (int x = 0; x < 1000; x++) {
				ResizingArrayQueue<Integer> numbers = new ResizingArrayQueue<Integer>();
				// Remo��o do numero da amostra selecionado no array (dequeue)
				if (kints >= 1000) {
					// Inserir a amostra pedida para depois efectuar a remo��o
					for (int i = 0; i != kints; i++) {
						numbers.enqueue(r);
					}
					int size = numbers.size();
					// Verificar n�mero de elementos no array
					// ***
					if (numbers.size() == 1) {
						usedMemory = sizeOf(numbers);
					} else {
						usedMemory = sizeOf(numbers) + 8 * numbers.size();
					}

					// In�cio da remo��o da amostra
					final Stopwatch stopwatchRemove = new Stopwatch();
					for (int i = 0; i != kints; i++) {
						numbers.dequeue();

					}
					// Paragem do contador do time de execu��o
					time = stopwatchRemove.elapsedTime();

					// Escrita em excel
					ExcelWrite(size, time, operation, usedMemory);
				} else {
					out.println("Tamanho inv�lido");
				}
			}
			break;
		default:
			out.println("Opera��o Inv�lida");
			break;
		}
	}
}