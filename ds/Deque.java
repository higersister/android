package com.cmy.test;

public class Deque<E> {

	private int left, right;
	private int count;
	private Object[] elementDatas;

	public Deque() {
		elementDatas = new Object[16];
		left = -1;
		right = 0;
	}

	public void insertLeft(E e) {
		elementDatas[left = (left + 1) & (elementDatas.length - 1)] = e;
		count++;
	}

	public void insertRight(E e) {
		elementDatas[right = (right - 1) & (elementDatas.length - 1)] = e;
		count++;
	}

	public E removeLeft() {
		int index = (left + 1) & (elementDatas.length - 1);
		@SuppressWarnings("unchecked")
		E e = (E) elementDatas[index];
		if (null != e) {
			elementDatas[index] = null;
			left = index;
			count--;
		}
		return e;
	}

	public E removeRight() {
		int index = (right - 1) & (elementDatas.length - 1);
		@SuppressWarnings("unchecked")
		E e = (E) elementDatas[index];
		if (null != e) {
			elementDatas[index] = null;
			right = index;
			count--;
		}
		return e;
	}

	public boolean isEmpty() {
		return 0 == count;
	}

	public boolean isFull() {
		return count == elementDatas.length;
	}

}
