package primeira.entrega;

import java.util.NoSuchElementException;
import static primeira.entrega.ObjectSizeFetcher.sizeOf;

public class QueueLinkedList {

	private Node first;
	private Node last;
	private int size;

	private class Node {
		private int item;
		private Node next;
	}

	public QueueLinkedList() {
		first = null;
		last = null;
		size = 0;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return size;
	}

	public void enqueue(final int item) {
		final Node oldlast = last;

		last = new Node();

		last.item = item;
		last.next = null;

		if (first == null)
			first = last;
		else
			oldlast.next = last;

		size++;
	}

	public int dequeue() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow.");

		final int item = first.item;

		first = first.next;

		if (first == null)
			last = null;

		size--;

		return item;
	}

	// Obtenção da memória ocupada pela fila
	public long usedMemory() {
		return sizeOf(this) + (sizeOf(this) * size);
	}

}
