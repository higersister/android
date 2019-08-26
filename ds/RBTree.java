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
	 * 1.����ڵ�ĸ��ڵ��������ڵ㣨�游�ڵ����һ���ӽڵ㣩��Ϊ��ɫ�ģ�
	 * 
	 * 2.����ڵ�ĸ��ڵ��Ǻ�ɫ������ڵ��Ǻ�ɫ���Ҳ���ڵ����丸�ڵ�����ӽڵ㣻
	 * 
	 * 3.����ڵ�ĸ��ڵ��Ǻ�ɫ������ڵ��Ǻ�ɫ���Ҳ���ڵ����丸�ڵ�����ӽڵ㡣
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
	 * ����ʾ��ͼ���Խڵ�x��������
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
	 * �����������£�
	 * 
	 * 1. ��y�����ӽڵ㸳��x�����ӽڵ�,����x����y���ӽڵ�ĸ��ڵ�(y���ӽڵ�ǿ�ʱ)
	 * 
	 * 2. ��x�ĸ��ڵ�p(�ǿ�ʱ)����y�ĸ��ڵ㣬ͬʱ����p���ӽڵ�Ϊy(�����)
	 * 
	 * 3. ��y�����ӽڵ���Ϊx����x�ĸ��ڵ���Ϊy
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
	 * ����ʾ��ͼ���Խڵ�y��������
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
	 * �����������£�
	 * 
	 * 1. ��x�����ӽڵ㸳��y�����ӽڵ�,����y����x���ӽڵ�ĸ��ڵ�(x���ӽڵ�ǿ�ʱ)
	 * 
	 * 2. ��y�ĸ��ڵ�p(�ǿ�ʱ)����x�ĸ��ڵ㣬ͬʱ����p���ӽڵ�Ϊx(�����)
	 * 
	 * 3. ��x�����ӽڵ���Ϊy����y�ĸ��ڵ���Ϊx
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
	 * ����˵�� �������
	 * 
	 * Case 1 x���ֵܽڵ��Ǻ�ɫ��(��ʱx�ĸ��ڵ��x���ֵܽڵ���ӽڵ㶼�Ǻڽڵ�)��
	 * 
	 * (01) ��x���ֵܽڵ���Ϊ����ɫ����
	 * 
	 * (02) ��x�ĸ��ڵ���Ϊ����ɫ����
	 * 
	 * (03) ��x�ĸ��ڵ����������
	 * 
	 * (04) ��������������x���ֵܽڵ㡣
	 * 
	 * Case 2 x���ֵܽڵ��Ǻ�ɫ��x���ֵܽڵ���������Ӷ��Ǻ�ɫ��
	 * 
	 * (01) ��x���ֵܽڵ���Ϊ����ɫ����
	 * 
	 * (02) ���á�x�ĸ��ڵ㡱Ϊ���µ�x�ڵ㡱��
	 * 
	 * Case 3 x���ֵܽڵ��Ǻ�ɫ��x���ֵܽڵ�������Ǻ�ɫ���Һ����Ǻ�ɫ�ġ�
	 * 
	 * (01) ��x�ֵܽڵ��������Ϊ����ɫ����
	 * 
	 * (02) ��x�ֵܽڵ���Ϊ����ɫ����
	 * 
	 * (03) ��x���ֵܽڵ����������
	 * 
	 * (04) ��������������x���ֵܽڵ㡣
	 * 
	 * Case 4 x���ֵܽڵ��Ǻ�ɫ��x���ֵܽڵ���Һ����Ǻ�ɫ�ģ�x���ֵܽڵ������������ɫ��
	 * 
	 * (01) ��x���ڵ���ɫ ��ֵ�� x���ֵܽڵ㡣
	 * 
	 * (02) ��x���ڵ���Ϊ����ɫ����
	 * 
	 * (03) ��x�ֵܽڵ�����ӽ���Ϊ����ɫ����
	 * 
	 * (04) ��x�ĸ��ڵ����������
	 * 
	 * (05) ���á�x��Ϊ�����ڵ㡱��
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
