package com.cmy.test;

public class LinkedList {

	private Node first;
	private Node current;
	private int count;

	public void add(Integer e) {
		Node l = first;
		Node newNode = new Node(e);
		if (l == null) {
			first = newNode;
			first.next = newNode;
			current = first;
		} else {
			Node node = first;
			while (node.next != first) {
				node = node.next;
			}
			node.next = newNode;
			newNode.next = first;
		}
		count++;
	}

	public Integer next() {
		Node l = current;
		if (null == l) {
			return -1;
		} else {
			current = current.next;
			return l.item;
		}
	}

	public boolean remove(int n) {
		if (0 > n || null == first)
			return false;
		Node previous = current;
		for (;;) {
			if (current.item != n) {
				if (1 == count)
					return false;
				previous = current;
				current = current.next;
			} else {
				if (1 == count) {
					first = null;
					current = null;
				} else {
					previous.next = current.next;
					current = current.next;
				}
				count--;
				return true;
			}
		}
	}

	public int indexOf(int n) {
		if (0 > n || null == first)
			return -1;
		Node l = first;
		for (int i = 0;; i++) {
			if (l.item != n) {
				if (l.next == first)
					break;
				else
					l = l.next;
			} else {
				return i;
			}
		}
		return -1;
	}

	public boolean isEmpty() {
		return 0 == count;
	}

	public int size() {
		return count;
	}

	private class Node {
		Integer item;
		Node next;

		public Node(Integer item, Node next) {
			super();
			this.item = item;
			this.next = next;
		}

		public Node(Integer item) {
			super();
			this.item = item;
		}

	}

}
