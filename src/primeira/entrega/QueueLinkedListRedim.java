package primeira.entrega;

import static primeira.entrega.Tools.out;
import static primeira.entrega.Tools.in;
import primeira.entrega.QueueLinkedList;
import primeira.entrega.Stopwatch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QueueLinkedListRedim {

	public static void ExcelWrite(int size, double time, String operation, long usedMemory) {
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		try {

			// Verificar se ficheiro existe
			if (!new File("ResultadosLinkedList.xlsx").exists()) {
				// Workbook em branco
				workbook = new XSSFWorkbook();

				// Criar folha em branco com "ResultadosInserir" ou
				// "ResultadosRemover"
				sheet = workbook.createSheet("ResultadosInserir");
				sheet = workbook.createSheet("ResultadosRemover");

				// Obter a folha de cálculo correta
				if (operation.matches("Inserir.*")) {
					sheet = workbook.getSheetAt(0);
				} else {
					sheet = workbook.getSheetAt(1);
				}

			} else {

				// Buscar ficheiro
				FileInputStream file = new FileInputStream(new File("ResultadosLinkedList.xlsx"));

				// Criar Workbook com a referência para o ficheiro .xlsx
				workbook = new XSSFWorkbook(file);

				// Obter a folha de cálculo correta
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

			// Se o numero de linhas preenchidas for = 0 -> Criar cabeçalho
			if (numberOfLines == 0) {

				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Número de inteiros");
				header.createCell(1).setCellValue("time decorrido");
				header.createCell(2).setCellValue("time por operação");
				header.createCell(3).setCellValue("Memória Ocupada(Bytes)");

				numberOfLines = sheet.getPhysicalNumberOfRows();
			}

			// Registar resultado consoante numero de linhas preenchidas
			Row dataRow = sheet.createRow(numberOfLines);

			dataRow.createCell(0).setCellValue(size);
			dataRow.createCell(1).setCellValue(time);
			dataRow.createCell(2).setCellValue(time / size);
			dataRow.createCell(3).setCellValue(usedMemory);

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

	public static void main(String[] args) {
		int r = 69;
		String operation = "";
		double time = 0;
		long usedMemory = 0;
		out.println("Escolha o tipo de operação (Inserir/Remover)");
		String oper = in.nextLine();
		out.println("Escolha o size da amosta  (>1000)");
		int kints = in.nextInt();
		// Escolha da operação e size da amostra
		operation = oper + kints;
		out.println("OP: " + operation);
		switch (oper) {
		case "Inserir":
			for (int x = 0; x < 1000; x++) {
				QueueLinkedList numbers = new QueueLinkedList();
				// Inserção do número da amostra selecionado na fila (enqueue)
				if (kints >= 1000) {
					// Início da inserção
					final Stopwatch stopwatchInsertion = new Stopwatch();
					for (int i = 0; i != kints; i++) {
						numbers.enqueue(r);
					}
					// Paragem do contador do time de execução
					time = stopwatchInsertion.elapsedTime();

					// Memória ocupada pela fila
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

					// Escrita em Excel
					ExcelWrite(numbers.size(), time, operation, usedMemory);
				} else {
					out.println("Tamanho inválido");
				}
			}
			break;

		case "Remover":
			for (int x = 0; x < 1000; x++) {
				QueueLinkedList numbers = new QueueLinkedList();
				// Remoção do numero da amostra selecionado na fila (dequeue)
				if (kints >= 1000) {
					// Inserir a amostra pedida para depois efectuar a remoção
					for (int i = 0; i != kints; i++) {
						numbers.enqueue(r);
					}
					int size = numbers.size();
					// Memória Ocupada pela fila
					usedMemory = numbers.usedMemory();

					// Início da remoção da amostra
					final Stopwatch stopwatchRemove = new Stopwatch();
					for (int i = 0; i != kints; i++) {
						numbers.dequeue();
					}
					// Paragem do contador do time de execução
					time = stopwatchRemove.elapsedTime();

					// Escrita em Excel
					ExcelWrite(size, time, operation, usedMemory);
				} else {
					out.println("Tamanho inválido");
				}
			}
			break;
		default:
			out.println("Operação Inválida");
			break;
		}
	}
}