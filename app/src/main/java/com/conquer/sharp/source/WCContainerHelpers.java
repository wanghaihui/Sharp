package com.conquer.sharp.source;

public class WCContainerHelpers {

    static final int[] EMPTY_INTS = new int[0];
    static final long[] EMPTY_LONGS = new long[0];
    static final Object[] EMPTY_OBJECTS = new Object[0];

    private WCContainerHelpers() {

    }

    public static int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    /**
     * 字节数组的理想化大小--https://blog.csdn.net/litefish/article/details/44078857
     */
    public static int idealByteArraySize(int need) {
        for(int i = 4; i < 32; ++i) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }

        return need;
    }

    // This is Arrays.binarySearch(), but doesn't do any argument validation(参数验证).
    static int binarySearch(int[] array, int size, int value) {
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            // 无符号右移
            int mid = (lo + hi) >>> 1;
            int midVal = array[mid];
            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;  // value found
            }
        }
        // ~位非
        return ~lo;  // value not present(不存在)
    }
}
