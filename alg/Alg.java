package com.cmy.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author higher sister
 * @version 2019-11-14
 */
public class Alg {

	public static void main(String[] args) {
		// int[] arr = new int[100];
		// for (int i = 0; i < arr.length; i++) {
		// if (i == 13)
		// continue;
		// arr[i] = i + 1;
		// }
		// System.out.println(item1_1(arr));
		// System.out.println(item1_2(new int[]{2,2,4,4,5,6,6}));
		// for (int i : item1_3(new int[] { 2, 2, 4, 4, 5, 6, 6 })) {
		// System.out.println(i);
		// }
		// for (int i : item1_4(new int[] { 2, 1, 3, 6, 4, 3 }, 8)) {
		// System.out.println(i);
		// }
		// for (int i : item1_5(new int[] { 55, 66, 77, 35, 24, 26, 55, 88, 24
		// })) {
		// System.out.println(i);
		// }

		// for (int i : item1_6(new int[] { 55, 66, 77, 35, 24, 26, 55, 88, 24
		// })) {
		// System.out.println(i);
		// }

		// for (int i : item1_7(new int[] { 55, 66, 77, 35, 24, 26, 55, 88, 24
		// })) {
		// System.out.println(i);
		// }
		// for (int i : item1_9(new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1 })) {
		// System.out.println(i);
		// }
		// for (int i : item1_10(new int[] { 55, 66, 77, 35, 24, 26, 55, 88, 24,
		// 24, 24, 24, 77, 66 })) {
		// System.out.println(i);
		// }
//		Node node1 = new Node(1);
//		Node node2 = new Node(2);
//		Node node3 = new Node(3);
//		Node node4 = new Node(4);
//		Node node5 = new Node(5);
//		Node node6 = new Node(6);
//		Node node7 = new Node(7);
//		node1.next = node2;
//		node2.next = node3;
//		node3.next = node4;
//		node4.next = node5;
//		node5.next = node6;
//		node6.next = node7;
//		System.out.println(item2_1(node1));
	}

	public static int item2_1(Node head) {
		int len = 0;
		Node curNode = head;
		Node midNode = head;
		while (null != curNode) {
			len++;
			if (0 == len % 2)
				midNode = midNode.next;
			curNode = curNode.next;
		}
		return midNode.data;
	}

	// 1.10
	public static int[] item1_10(int[] arr) {
		int len = 0;
		for (int i = 0; i < arr.length; i++) {
			if (containes(arr, arr[i], i)) {
				arr[i] = -1;
				continue;
			}
			len++;
		}
		int[] res = new int[len];
		for (int i = 0, j = 0; j < arr.length; j++) {
			if (-1 != arr[j]) {
				res[i++] = arr[j];
			}
		}
		return res;
	}

	public static boolean containes(int[] arr, int num, int i) {

		for (int j = i + 1; j < arr.length; j++) {
			if (arr[j] == num)
				return true;
		}

		return false;
	}

	// 1.9
	public static int[] item1_9(int[] arr) {

		for (int i = 0, j = arr.length - 1; i < j; i++, j--)
			swap(arr, i, j);
		return arr;
	}

	// 1.7
	public static int[] item1_7(int[] arr) {
		quickSort(arr, 0, arr.length - 1);
		return arr;
	}

	public static void quickSort(int[] arr, int i, int j) {
		if (i < j) {
			int mid = getPivot(arr, i, j);
			quickSort(arr, i, mid - 1);
			quickSort(arr, mid + 1, j);
		}
	}

	public static int getPivot(int[] arr, int i, int j) {
		int pivot = arr[i];
		while (i < j) {
			while (arr[j] >= pivot && i < j) {
				j--;
			}
			swap(arr, i, j);
			while (arr[i] <= pivot && i < j) {
				i++;
			}
			swap(arr, i, j);

		}
		return i;
	}

	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	// 1.6
	public static int[] item1_6(int[] arr) {
		TreeSet<Integer> set = new TreeSet<>();
		for (int i = 0; i < arr.length; i++) {
			set.add(arr[i]);
		}
		int[] res = new int[set.size()];
		int j = 0;
		for (int i : set) {
			res[j++] = i;
		}
		return res;
	}

	// 1.5
	public static ArrayList<Integer> item1_5(int[] arr) {
		ArrayList<Integer> num = new ArrayList<Integer>();
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < arr.length; i++) {
			Integer key = arr[i];
			Integer value = map.get(key);
			map.put(key, null == value ? 1 : value + 1);
		}
		for (int i = 0; i < arr.length; i++) {
			Integer key = arr[i];
			Integer value = map.get(key);
			if (null != value && 1 < value) {
				num.add(key);
				map.remove(key);
			}
		}
		return num;
	}

	// 1.4
	public static int[] item1_4(int[] arr, int sum) {
		int[] num = new int[2];
		int left = 0, right = arr.length - 1;
		Arrays.sort(arr);
		for (; right > left;) {
			int curSum = arr[left] + arr[right];
			if (curSum == sum) {
				num[0] = arr[left];
				num[1] = arr[right];
				break;
			} else if (curSum > sum)
				right--;
			else
				left++;
		}

		return num;
	}

	// 1.3
	public static int[] item1_3(int[] arr) {
		int[] num = new int[2];
		num[0] = Integer.MIN_VALUE;
		num[1] = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			num[0] = Math.max(arr[i], num[0]);
			num[1] = Math.min(arr[i], num[1]);
		}

		return num;
	}

	// 1.2
	public static int item1_2(int[] arr) {
		int num = 0;
		for (int i = 0; i < arr.length; i++) {
			num ^= arr[i];
		}

		return num;
	}

	// 1.1
	public static int item1_1(int[] arr) {
		int arrSum = 0;
		for (int i = 0; i < arr.length; i++) {
			arrSum += arr[i];
		}

		return (1 + 100) * 100 / 2 - arrSum;
	}

	public static class Node {
		private Node next;
		private int data;

		public Node(int data) {
			this.data = data;
		}

		public int data() {
			return data;
		}

		public void setData(int data) {
			this.data = data;
		}

		public Node next() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

	}

}
