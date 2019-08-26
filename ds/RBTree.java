package com.cmy.test;

/**
 * @author higher sister
 * @version 2019-8-13
 * @see https://blog.csdn.net/eson_15/article/details/51144079
 * @see https://www.cnblogs.com/xingele0917/p/3848278.html#a4
 * @link TreeMap
 */
public class RBTree<T extends Comparable<T>> {

	private Node<T> root;

	public RBTree() {
	}

	public void insert(T t) {
		Node<T> node = new Node<T>(t);
		Node<T> current = root;
		if (null == current) {
			root = node;
		} else {
			Node<T> parent = null;
			int cmp = 0;
			while (null != current) {
				parent = current;
				cmp = t.compareTo(current.t);
				if (cmp < 0)
					current = current.left;
				else
					current = current.right;
			}
			node.parent = parent;
			if (cmp < 0)
				parent.left = node;
			else
				parent.right = node;
		}
		fixInsertion(node);
	}

	/**
	 * 
	 * 1.插入节点的父节点和其叔叔节点（祖父节点的另一个子节点）均为红色的；
	 * 
	 * 2.插入节点的父节点是红色，叔叔节点是黑色，且插入节点是其父节点的右子节点；
	 * 
	 * 3.插入节点的父节点是红色，叔叔节点是黑色，且插入节点是其父节点的左子节点。
	 */
	private void fixInsertion(Node<T> n) {
		n.color = RED;
		while (null != n && n != root && n.parent.color == RED) {
			if (parentOf(n) == leftOf(parentOf(parentOf(n)))) {
				Node<T> r = rightOf(parentOf(parentOf(n)));
				if (!colorOf(r)) {
					setColor(r, BLACK);
					setColor(parentOf(n), BLACK);
					setColor(parentOf(parentOf(n)), RED);
					n = parentOf(parentOf(n));
				} else {
					if (n == rightOf(parentOf(n))) {
						n = parentOf(n);
						rotateLeft(n);
					}
					setColor(parentOf(n), BLACK);
					setColor(parentOf(parentOf(n)), RED);
					rotateRight(parentOf(parentOf(n)));
				}

			} else {
				Node<T> l = leftOf(parentOf(parentOf(n)));
				if (!colorOf(l)) {
					setColor(l, BLACK);
					setColor(parentOf(n), BLACK);
					setColor(parentOf(parentOf(n)), RED);
					n = parentOf(parentOf(n));
				} else {
					if (n == leftOf(parentOf(n))) {
						n = parentOf(n);
						rotateRight(n);
					}
					setColor(parentOf(n), BLACK);
					setColor(parentOf(parentOf(n)), RED);
					rotateLeft(parentOf(parentOf(n)));
				}

			}
		}
		root.color = BLACK;
	}

	/**
	 * 
	 * 左旋示意图：对节点x进行左旋
	 * 
	 * -----p-----------------------p
	 * 
	 * ----/-----------------------/
	 * 
	 * ---x-----------------------y
	 * 
	 * --/-\---------------------/-\
	 * 
	 * -lx--y||||||----->|||||||x---ry
	 * 
	 * ----/-\-----------------/-\
	 * 
	 * ---ly-ry---------------lx-ly
	 * 
	 * 
	 * 左旋做三件事：
	 * 
	 * 1. 将y的左子节点赋给x的右子节点,并将x赋给y左子节点的父节点(y左子节点非空时)
	 * 
	 * 2. 将x的父节点p(非空时)赋给y的父节点，同时更新p的子节点为y(左或右)
	 * 
	 * 3. 将y的左子节点设为x，将x的父节点设为y
	 */
	private void rotateLeft(Node<T> p) {
		if (null != p) {
			Node<T> r = p.right;
			p.right = r.left;
			if (null != r.left)
				r.left.parent = p;
			r.parent = p.parent;
			if (null == p.parent)
				root = r;
			else if (p.parent.left == p)
				p.parent.left = r;
			else
				p.parent.right = r;
			r.left = p;
			p.parent = r;
		}

	}

	/**
	 * 右旋示意图：对节点y进行右旋
	 * 
	 * --------p-------------------p
	 * 
	 * -------/-------------------/
	 * 
	 * ------y-------------------x
	 * 
	 * -----/-\-----------------/-\
	 * 
	 * ----x--ry||||----->||||lx---y
	 * 
	 * ---/-\---------------------/-\
	 * 
	 * -lx--rx------------------rx--ry
	 * 
	 * 右旋做三件事：
	 * 
	 * 1. 将x的右子节点赋给y的左子节点,并将y赋给x右子节点的父节点(x右子节点非空时)
	 * 
	 * 2. 将y的父节点p(非空时)赋给x的父节点，同时更新p的子节点为x(左或右)
	 * 
	 * 3. 将x的右子节点设为y，将y的父节点设为x
	 */
	private void rotateRight(Node<T> p) {
		if (null != p) {
			Node<T> l = p.left;
			p.left = l.right;
			if (null != l.right)
				l.right.parent = p;
			l.parent = p.parent;
			if (null == p.parent)
				root = p;
			else if (p.parent.right == p)
				p.parent.right = l;
			else
				p.parent.left = l;
			l.right = p;
			p.parent = l;

		}
	}

	/**
	 * 
	 * @param t
	 */
	public void remove(T t) {
		Node<T> p = search(t);
		if (null != p.left && null != p.right) {
			Node<T> s = successor(p);
			p.t = s.t;
			p = s;
		}
		Node<T> replacement = null != p.left ? p.left : p.right;
		if (null != replacement) {
			replacement.parent = p.parent;
			if (null == p.parent)
				root = replacement;
			else if (p == p.parent.left)
				p.parent.left = replacement;
			else
				p.parent.right = replacement;
			p.parent = p.left = p.right = null;
			if (colorOf(p))
				fixDeletion(replacement);
		} else if (null == p.parent) {
			root = null;
		} else {
			if (colorOf(p))
				fixDeletion(p);
			if (null != p.parent) {
				if (p == p.parent.left)
					p.parent.left = null;
				else if (p == p.parent.right)
					p.parent.right = null;
				p.parent = null;
			}
		}
	}

	/**
	 * 
	 * 现象说明 处理策略
	 * 
	 * Case 1 x的兄弟节点是红色。(此时x的父节点和x的兄弟节点的子节点都是黑节点)。
	 * 
	 * (01) 将x的兄弟节点设为“黑色”。
	 * 
	 * (02) 将x的父节点设为“红色”。
	 * 
	 * (03) 对x的父节点进行左旋。
	 * 
	 * (04) 左旋后，重新设置x的兄弟节点。
	 * 
	 * Case 2 x的兄弟节点是黑色，x的兄弟节点的两个孩子都是黑色。
	 * 
	 * (01) 将x的兄弟节点设为“红色”。
	 * 
	 * (02) 设置“x的父节点”为“新的x节点”。
	 * 
	 * Case 3 x的兄弟节点是黑色；x的兄弟节点的左孩子是红色，右孩子是黑色的。
	 * 
	 * (01) 将x兄弟节点的左孩子设为“黑色”。
	 * 
	 * (02) 将x兄弟节点设为“红色”。
	 * 
	 * (03) 对x的兄弟节点进行右旋。
	 * 
	 * (04) 右旋后，重新设置x的兄弟节点。
	 * 
	 * Case 4 x的兄弟节点是黑色；x的兄弟节点的右孩子是红色的，x的兄弟节点的左孩子任意颜色。
	 * 
	 * (01) 将x父节点颜色 赋值给 x的兄弟节点。
	 * 
	 * (02) 将x父节点设为“黑色”。
	 * 
	 * (03) 将x兄弟节点的右子节设为“黑色”。
	 * 
	 * (04) 对x的父节点进行左旋。
	 * 
	 * (05) 设置“x”为“根节点”。
	 * 
	 * @param node
	 */
	private void fixDeletion(Node<T> x) {
		while (x != root && colorOf(x)) {
			if (x == leftOf(parentOf(x))) {
				Node<T> ch = rightOf(parentOf(x));
				// case 1
				if (!colorOf(ch)) {
					setColor(ch, BLACK);
					setColor(parentOf(x), RED);
					rotateLeft(parentOf(x));
					ch = rightOf(parentOf(x));
				}

				// case 2
				if (colorOf(leftOf(ch)) && colorOf(rightOf(ch))) {
					setColor(ch, RED);
					x = parentOf(x);
				} else {
					// case 3
					if (colorOf(rightOf(ch))) {
						setColor(leftOf(ch), BLACK);
						setColor(ch, RED);
						rotateRight(ch);
						ch = rightOf(parentOf(x));
					}
					// case 4
					setColor(ch, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(rightOf(ch), BLACK);
						rotateLeft(parentOf(x));
					x = root;
				}

			} else {
				Node<T> ch = leftOf(parentOf(x));
				// case 1
				if (!colorOf(ch)) {
					setColor(ch, BLACK);
					setColor(parentOf(x), RED);
					rotateRight(parentOf(x));
					ch = leftOf(parentOf(x));
				}

				// case 2
				if (colorOf(leftOf(ch)) && colorOf(rightOf(ch))) {
					setColor(ch, RED);
					x = parentOf(x);
				} else {
					// case 3
					if (colorOf(leftOf(ch))) {
						setColor(rightOf(ch), BLACK);
						setColor(ch, RED);
						rotateLeft(ch);
						ch = leftOf(parentOf(x));
					}
					// case 4
					setColor(ch, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(leftOf(ch), BLACK);
					rotateRight(parentOf(x));
					x = root;
				}
			}
		}
		setColor(x, BLACK);
	}

	private Node<T> successor(Node<T> t) {
		if (null == t)
			return null;
		if (null != t.right) {
			Node<T> node = t.right;
			while (null != node.left)
				node = node.left;
			return node;
		} else {
			Node<T> node = t.left;
			while (null != node.right)
				node = node.right;
			return node;
		}
	}

	private Node<T> search(T t) {
		if (null == t)
			return null;
		Node<T> node = root;
		while (null != node) {
			int compare = node.t.compareTo(t);
			if (compare > 0)
				node = node.left;
			else if (compare < 0)
				node = node.right;
			else
				return node;

		}
		return node;

	}

	private void setColor(Node<T> node, boolean color) {
		if (null != node)
			node.color = color;
	}

	private Node<T> parentOf(Node<T> node) {
		return null == node ? null : node.parent;
	}

	private Node<T> leftOf(Node<T> node) {
		return null == node ? null : node.left;
	}

	private Node<T> rightOf(Node<T> node) {
		return null == node ? null : node.right;
	}

	private boolean colorOf(Node<T> node) {
		return null == node ? BLACK : node.color;
	}

	public void preOrder() {
		preOrder(root);
	}

	private void preOrder(Node<T> node) {
		if (null != node) {
			System.out.println(node.toString());
			preOrder(node.left);
			preOrder(node.right);
		}
	}

	private static final boolean RED = false;
	private static final boolean BLACK = true;

	@SuppressWarnings("hiding")
	private class Node<T extends Comparable<T>> {
		T t;
		Node<T> parent;
		Node<T> left;
		Node<T> right;
		boolean color;

		public Node(T t) {
			this(t, RED);
		}

		public Node(T t, boolean color) {
			this(t, null, null, null, RED);
		}

		public Node(T t, Node<T> parent, Node<T> left, Node<T> right,
				boolean color) {
			super();
			this.t = t;
			this.parent = parent;
			this.left = left;
			this.right = right;
			this.color = color;
		}

		@Override
		public String toString() {
			return "value:" + t + ",color:" + (color ? "BLACK" : "RED");
		}
	}

}
