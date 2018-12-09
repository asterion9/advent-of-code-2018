package fr.sma.adventofcode.resolve.day9;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CircularDoublyLinkedList {
	private Node current;
	
	private int size;
	
	public CircularDoublyLinkedList(long data) {
		this.current = new Node(data);
		size = 1;
	}
	
	public int getSize() {
		return size;
	}
	
	public long read() {
		return current.getData();
	}
	
	public long remove() {
		if(size == 1) {
			throw new IllegalStateException("can't remove last element");
		}
		size--;
		current.getPrev().setNext(current.getNext());
		current.getNext().setPrev(current.getPrev());
		Node old = current;
		current = old.getNext();
		return old.getData();
	}
	
	public void update(long value) {
		current.setData(value);
	}
	
	public void insertAfter(long data) {
		Node newNode = new Node(current, current.getNext(), data);
		current.getNext().setPrev(newNode);
		current.setNext(newNode);
		size++;
	}
	
	public CircularDoublyLinkedList move(long nb) {
		if( nb > 0) {
			for (int i = 0; i < nb; i++) {
				current = current.getNext();
			}
		} else if (nb < 0) {
			for (int i = 0; i > nb; i--) {
				current = current.getPrev();
			}
		}
		return this;
	}
	
	private Node next() {
		return current.getNext();
	}
	
	private Node prev() {
		return current.getPrev();
	}
	
	private static class Node {
		private Node prev, next;
		private long data;
		
		Node(long data) {
			this.data = data;
			this.prev = this;
			this.next = this;
		}
		
		Node(Node prev, Node next, long data) {
			this.prev = prev;
			this.next = next;
			this.data = data;
		}
		
		long getData() {
			return data;
		}
		
		void setData(long data) {
			this.data = data;
		}
		
		Node getPrev() {
			return prev;
		}
		
		void setPrev(Node prev) {
			this.prev = prev;
		}
		
		Node getNext() {
			return next;
		}
		
		void setNext(Node next) {
			this.next = next;
		}
	}
	
	@Override
	public String toString() {
		return Stream.iterate(current, Node::getNext)
				.limit(this.size)
				.map(Node::getData)
				.map(i -> String.format("%3s", i))
				.collect(Collectors.joining(" "));
	}
}
