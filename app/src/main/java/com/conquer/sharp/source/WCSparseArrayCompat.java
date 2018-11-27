package com.conquer.sharp.source;

// Cloneable--https://blog.csdn.net/zxying187/article/details/80856062
public class WCSparseArrayCompat<E> implements Cloneable {

    private static final Object DELETED = new Object();
    private boolean mGarbage = false;

    private int[] mKeys;
    private Object[] mValues;
    private int mSize;

    public WCSparseArrayCompat() {
        this(10);
    }

    public WCSparseArrayCompat(int initialCapacity) {
        this.mGarbage = false;
        if (initialCapacity == 0) {
            this.mKeys = WCContainerHelpers.EMPTY_INTS;
            this.mValues = WCContainerHelpers.EMPTY_OBJECTS;
        } else {
            initialCapacity = WCContainerHelpers.idealIntArraySize(initialCapacity);
            this.mKeys = new int[initialCapacity];
            this.mValues = new Object[initialCapacity];
        }

        this.mSize = 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public WCSparseArrayCompat<E> clone() {
        WCSparseArrayCompat<E> clone = null;
        try {
            clone = (WCSparseArrayCompat<E>) super.clone();
            clone.mKeys = mKeys.clone();
            clone.mValues = mValues.clone();
        } catch (CloneNotSupportedException e) {
            /* ignore */
        }
        return clone;
    }

    public E get(int key) {
        return get(key, null);
    }

    @SuppressWarnings("unchecked")
    public E get(int key, E valueIfKeyNotFound) {
        // 二分查找
        int i = WCContainerHelpers.binarySearch(mKeys, mSize, key);
        if (i < 0 || mValues[i] == DELETED) {
            return valueIfKeyNotFound;
        } else {
            return (E) mValues[i];
        }
    }

    public void delete(int key) {
        int i =  WCContainerHelpers.binarySearch(mKeys, mSize, key);
        if (i >= 0) {
            if (mValues[i] != DELETED) {
                mValues[i] = DELETED;
                mGarbage = true;
            }
        }
    }

    // 别名
    public void remove(int key) {
        delete(key);
    }

    public void removeAt(int index) {
        if (mValues[index] != DELETED) {
            mValues[index] = DELETED;
            mGarbage = true;
        }
    }

    public void removeAtRange(int index, int size) {
        final int end = Math.min(mSize, index + size);
        for (int i = index; i < end; i++) {
            removeAt(i);
        }
    }

    private void gc() {
        int n = mSize;
        int o = 0;
        int[] keys = mKeys;
        Object[] values = mValues;
        for (int i = 0; i < n; i++) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        mGarbage = false;
        mSize = o;
    }

    public void put(int key, E value) {
        int i =  WCContainerHelpers.binarySearch(mKeys, mSize, key);
        if (i >= 0) {
            mValues[i] = value;
        } else {
            i = ~i;
            if (i < mSize && mValues[i] == DELETED) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }
            if (mGarbage && mSize >= mKeys.length) {
                gc();
                // gc会导致数组被调整
                // Search again because indices(索引) may have changed.
                i = ~ WCContainerHelpers.binarySearch(mKeys, mSize, key);
            }
            // 扩充数组
            if (mSize >= mKeys.length) {
                int n = WCContainerHelpers.idealIntArraySize(mSize + 1);
                int[] nKeys = new int[n];
                Object[] nValues = new Object[n];
                System.arraycopy(mKeys, 0, nKeys, 0, mKeys.length);
                System.arraycopy(mValues, 0, nValues, 0, mValues.length);
                mKeys = nKeys;
                mValues = nValues;
            }
            if (mSize - i != 0) {
                System.arraycopy(mKeys, i, mKeys, i + 1, mSize - i);
                System.arraycopy(mValues, i, mValues, i + 1, mSize - i);
            }
            mKeys[i] = key;
            mValues[i] = value;
            mSize++;
        }
    }

    public int size() {
        if (mGarbage) {
            gc();
        }
        return mSize;
    }

    public int keyAt(int index) {
        if (mGarbage) {
            gc();
        }
        return mKeys[index];
    }

    @SuppressWarnings("unchecked")
    public E valueAt(int index) {
        if (mGarbage) {
            gc();
        }
        return (E) mValues[index];
    }

    public void setValueAt(int index, E value) {
        if (mGarbage) {
            gc();
        }
        mValues[index] = value;
    }

    public int indexOfKey(int key) {
        if (mGarbage) {
            gc();
        }
        return  WCContainerHelpers.binarySearch(mKeys, mSize, key);
    }

    public int indexOfValue(E value) {
        if (mGarbage) {
            gc();
        }
        for (int i = 0; i < mSize; i++)
            if (mValues[i] == value)
                return i;
        return -1;
    }

    public void clear() {
        int n = mSize;
        Object[] values = mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        mSize = 0;
        mGarbage = false;
    }

    /**
     * Puts a key/value pair into the array, optimizing for the case where
     * the key is greater than all existing keys in the array.
     */
    public void append(int key, E value) {
        if (mSize != 0 && key <= mKeys[mSize - 1]) {
            put(key, value);
            return;
        }
        if (mGarbage && mSize >= mKeys.length) {
            gc();
        }
        int pos = mSize;
        if (pos >= mKeys.length) {
            int n =  WCContainerHelpers.idealIntArraySize(pos + 1);
            int[] nKeys = new int[n];
            Object[] nValues = new Object[n];
            System.arraycopy(mKeys, 0, nKeys, 0, mKeys.length);
            System.arraycopy(mValues, 0, nValues, 0, mValues.length);
            mKeys = nKeys;
            mValues = nValues;
        }
        mKeys[pos] = key;
        mValues[pos] = value;
        mSize = pos + 1;
    }

    @Override
    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(mSize * 28);
        buffer.append('{');
        for (int i = 0; i < mSize; i++) {
            // 技巧
            if (i > 0) {
                buffer.append(", ");
            }
            int key = keyAt(i);
            buffer.append(key);
            buffer.append('=');
            Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }
}
