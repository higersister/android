package com.cmy.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Huffman {

	public static void huffmanCoding(Node current) {
		Node root = current;
		if (null == root)
			return;
		if (null != root.left) {
			root.left.code = root.code + root.left.code;
			huffmanCoding(root.left);
		}
		if (null != root.right) {
			root.right.code = root.code + root.right.code;
			huffmanCoding(root.right);
		}
	}

	public static void coding(Node root) {
		if (null != root) {
			if (null == root.left && null == root.right)
				System.out.printf("data:%d---code:%s\n", root.data, root.code);
			else {
				coding(root.left);
				coding(root.right);
			}
		}
	}

	public static boolean isEmpty(String str) {
		return null == str || 0 == str.length();
	}

	public static Node buildHuffman(List<Node> src) {
		Node current = null;
		List<Node> dest = new ArrayList<>();
		for (int i = 0, srcSize = src.size(); i < srcSize; i++)
			dest.add(i, src.get(i));
		int i = 0;
		// src.clear();
		while (!dest.isEmpty()) {
			Collections.sort(dest, new Comparator<Node>() {
				@Override
				public int compare(Node n1, Node n2) {
					int x = n1.data;
					int y = n2.data;
					return (x < y) ? -1 : ((x == y) ? 0 : 1);
				}
			});
			// for (int j = 0; j < nodes.size(); j++) {
			// System.out.println(nodes.get(j).data);
			// }
			Node left = dest.remove(0);
			left.code = "";
			current = left;
			if (0 == dest.size())
				return current;
			left.code = "0";
			Node right = dest.remove(0);
			right.code = "1";
			// System.out.printf("i:%d------left.data:%d,right.data:%d\n", i++,
			// left.data, right.data);
			current = new Node(left.data + right.data);
			current.left = left;
			current.right = right;
			dest.add(current);
		}
		return current;
	}

	public static class Node {

		Integer data;
		private String code;
		Node left;
		Node right;

		public Node(Integer data, Node left, Node right) {
			super();
			this.data = data;
			this.left = left;
			this.right = right;
		}

		public Node(Integer data) {
			super();
			this.data = data;
		}

	}

}
