package com.conquer.sharp.algorithm;

/**
 * 二叉查找树--适合范围查找--为了搜索而设计
 * 查找树: 一句话就是左孩子比父节点小，右孩子比父节点大
 * 特性: 中序遍历可以让结点有序
 */
public class BinarySearchTree {



    // 二叉树的最大深度
    private int maxDepth(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftDepth = maxDepth(node.left);
        int rightDepth = maxDepth(node.right);

        return Math.max(leftDepth, rightDepth) + 1;
    }

    // 二叉树的最小深度问题--最短路径的节点个数--深度优先搜索DFS
    // 二叉树的最小深度--写法1
    private int getMinDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return getMin(root);
    }
    private int getMin(TreeNode root) {
        if (root == null) {
            return Integer.MAX_VALUE;
        }

        if (root.left == null && root.right == null) {
            return 1;
        }

        return Math.min(getMin(root.left), getMin(root.right)) + 1;
    }

    // 二叉树的最小深度--写法2
    private int getMinDepth2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null) {
            return getMinDepth2(root.right) + 1;
        }
        if (root.right == null) {
            return getMinDepth2(root.left) + 1;
        }
        return Math.min(getMinDepth2(root.left), getMinDepth2(root.right)) + 1;
    }
}
