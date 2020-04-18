package com.conquer.sharp.optimize;

/**
 * 斐波那契数列的优化
 */
public class Fibonacci {

    // 教科书写法
    public static long computeRecursively(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        return computeRecursively(n - 2) + computeRecursively(n - 1);
    }

    // 微小优化
    public static long computeRecursively1(int n) {
        if (n > 1) {
            return computeRecursively(n - 2) + computeRecursively(n - 1);
        }
        return n;
    }
}
