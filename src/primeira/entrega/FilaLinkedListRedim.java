package primeira.entrega;

import static primeira.entrega.Tools.out;
import static primeira.entrega.Tools.in;
import primeira.entrega.FilaLinkedList;
import primeira.entrega.Stopwatch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

		public class FilaLinkedListRedim {
			
			public static void EscreverEmExcel(int tamanho, double tempo, String operacao, long memoriaOcupada){
				XSSFWorkbook workbook;
				XSSFSheet sheet;
				try{
					
				//Verificar se ficheiro existe
				if(!new File("ResultadosLinkedList.xlsx").exists()) {
					//Workbook em branco
					workbook = new XSSFWorkbook(); 
							
					//Criar folha em branco com "ResultadosInserir" ou "ResultadosRemover"
					sheet = workbook.createSheet("ResultadosInserir");
					sheet = workbook.createSheet("ResultadosRemover");
					
					//Obter a folha de c�lculo correta
					if (operacao.matches("Inserir.*")){
					sheet = workbook.getSheetAt(0);
					}else{
						sheet = workbook.getSheetAt(1);
					}
				
				}
				else {
					
					//Buscar ficheiro
					FileInputStream file = new FileInputStream(new File("ResultadosLinkedList.xlsx"));
					
					//Criar Workbook com a refer�ncia para o ficheiro .xlsx
					workbook = new XSSFWorkbook(file);

					//Obter a folha de c�lculo correta
					if (operacao.matches("Inserir.*")){
					sheet = workbook.getSheetAt(0);
					}else{
						sheet = workbook.getSheetAt(1);
					}
				}
				
				//Numero de linhas preenchidas
				int numLinhas = sheet.getPhysicalNumberOfRows();
				
				sheet.setColumnWidth(0, 22*256);
				sheet.setColumnWidth(1, 20*256);
				sheet.setColumnWidth(2, 21*256);
				sheet.setColumnWidth(3, 26*256);
				
				//Se o numero de linhas preenchidas for = 0 -> Criar cabe�alho
				if(numLinhas == 0){
				
				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("N�mero de inteiros");
				header.createCell(1).setCellValue("Tempo passado");
				header.createCell(2).setCellValue("Tempo nanosegundos");
				header.createCell(3).setCellValue("Mem�ria Ocupada(Bytes)");
				
				numLinhas = sheet.getPhysicalNumberOfRows();
				}
				
				//Registar resultado consoante numero de linhas preenchidas
				Row dataRow = sheet.createRow(numLinhas);
				
			    dataRow.createCell(0).setCellValue(tamanho);
			    dataRow.createCell(1).setCellValue(tempo);
			    dataRow.createCell(2).setCellValue(tempo/tamanho);
			    dataRow.createCell(3).setCellValue(memoriaOcupada);
			    
			    //Criar/Gravar ficheiro .xlsx
			    
			    FileOutputStream out =  new FileOutputStream(new File("ResultadosLinkedList.xlsx"));
			    workbook.write(out);
			    out.close();
			    System.out.println("Excel written successfully..");
			         
			    } catch (FileNotFoundException e) {
			        e.printStackTrace();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    
		}
			
	    public static void main(String[] args){
	    	
	    	//Cria��o da fila
	    	//FilaLinkedList listaNums = new FilaLinkedList();
	    	int r = 69;
	    	String operacao = "";
	    	double tempo = 0;
	    	long memoriaOcupada = 0;
	    	out.println("Escolha o tipo de opera��o (Inserir/Remover)");
	    	String oper = in.nextLine();
	    	out.println("Escolha o tamanho da amosta  (1000, 2000, 4000, ..., 32000)");
	    	int kints = in.nextInt();
	    	//Escolha da opera��o e tamanho da amostra
			operacao = oper+kints;
			out.println("OP: " + operacao);
			double tempojaa=0;
	        switch (oper) {
	            case "Inserir":
	            	final Stopwatch tempoja = new Stopwatch();
	            	for(int x=0;x<10000;x++){
	            		FilaLinkedList listaNums = new FilaLinkedList();
	            	
	            	//Inser��o do n�mero da amostra selecionado na fila (enqueue)
	            	if (kints>=1000) {
	            		
	            		//In�cio da inser��o
	            		final Stopwatch stopwatchInserir = new Stopwatch();
	            		for (int i = 0; i != kints; i++) {
	            			listaNums.enqueue(r);
	            		}
	            		//Paragem do contador do tempo de execu��o
	            		tempo = stopwatchInserir.elapsedTime();
	            		
	            		//Mem�ria ocupada pela fila
	            		memoriaOcupada = listaNums.memoriaOcupada();
	            		
	            		//Escrita em Excel
	            		EscreverEmExcel(listaNums.tamanho(), tempo, operacao, memoriaOcupada);
	            	}
	            	else {out.println("Tamanho inv�lido");}
	            	tempojaa = tempoja.elapsedTime();
	            	}
	            	out.println(tempojaa);
				break;
	            
	            case "Remover":
	            	for(int x=0;x<1000;x++){
	            		FilaLinkedList listaNums = new FilaLinkedList();
	            	//Remo��o do numero da amostra selecionado na fila (dequeue)
	            	if (kints>=1000){
	            		//Inserir a amostra pedida para depois efectuar a remo��o
	            		for(int i=0;i!=kints;i++){
		       		 		listaNums.enqueue(r);
	            		}
	            		int tamanho = listaNums.tamanho();
	            		//Mem�ria Ocupada pela fila
	            		memoriaOcupada = listaNums.memoriaOcupada();
	            		out.println(memoriaOcupada);
	            		
	            		// In�cio da remo��o da amostra
	            		final Stopwatch stopwatchRemover = new Stopwatch();
	            		for(int i=0;i!=kints;i++){
	            			listaNums.dequeue();
	            		}
	            		//Paragem do contador do tempo de execu��o
	            		tempo = stopwatchRemover.elapsedTime();
	            		
	            		//Escrita em Excel
	            		EscreverEmExcel(tamanho, tempo, operacao, memoriaOcupada);
	            	}
	            	else {out.println("Tamanho inv�lido");}
	            	}
		       		break;            
	            default:
	            	out.println("Opera��o Inv�lida");
	            	break;
	            }        
	    }
}