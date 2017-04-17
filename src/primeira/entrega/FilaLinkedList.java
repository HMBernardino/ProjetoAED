package primeira.entrega;


import java.util.NoSuchElementException;
import static primeira.entrega.ObjectSizeFetcher.sizeOf;

public class FilaLinkedList {

	private Node primeiro;
    private Node ultimo;
    private int tamanho;

    private class Node {
        private int item;
        private Node next;
    }
    
    public FilaLinkedList() {
        primeiro = null;
        ultimo = null;
        tamanho = 0;
    }

    public boolean isEmpty() {
        return primeiro == null;
    }

    public int tamanho() {
        return tamanho;
    }
    
    public void enqueue(final int item) {
        final Node oldultimo = ultimo;

        ultimo = new Node();

        ultimo.item = item;
        ultimo.next = null;

        if (primeiro == null)
            primeiro = ultimo;
        else
            oldultimo.next = ultimo;
        
        tamanho++;
    }

    public int dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow.");

        final int item = primeiro.item;

        primeiro = primeiro.next;

        if (primeiro == null)
            ultimo = null;

        tamanho--;
        
        return item;
    }
    
    //Obtenção da memória ocupada pela fila
    public long memoriaOcupada() {
        return sizeOf(this) * tamanho;
    }
    
}
