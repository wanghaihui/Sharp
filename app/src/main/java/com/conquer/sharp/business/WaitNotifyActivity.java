package com.conquer.sharp.business;

import android.os.Bundle;
import android.util.Log;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象锁和类锁--https://blog.csdn.net/feiduclear_up/article/details/78019526
 * wait,notify,notifyAll--https://www.jianshu.com/p/25e243850bd2?appinstall=0
 */
public class WaitNotifyActivity extends BaseActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wait_notify);

        final Product product = new Product();
        Runnable runProduce = new Runnable() {
            int count = 4;

            @Override
            public void run() {
                while (count-- > 0) {
                    product.produce();
                }
            }
        };

        Runnable runConsume = new Runnable() {
            int count = 4;
            @Override
            public void run() {
                while (count-- > 0) {
                    product.consume();
                }
            }
        };

        for (int i = 0; i < 2; i++) {
            new Thread(runConsume).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(runProduce).start();
        }
    }

    private static class Product {
        private Buffer mBuffer = new Buffer();

        // 生产者
        public void produce() {
            synchronized (this) {
                while (mBuffer.isFull()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mBuffer.add();
                notifyAll();
            }
        }

        // 消费者
        public void consume() {
            synchronized (this) {
                while (mBuffer.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mBuffer.remove();
                notifyAll();
            }
        }
    }

    private static class Buffer {
        private static final int MAX_CAPACITY = 1;
        private List<Object> innerList = new ArrayList<>(MAX_CAPACITY);

        void add() {
            if (isFull()) {
                throw new IndexOutOfBoundsException();
            } else {
                innerList.add(new Object());
            }
            Log.d("WaitNotifyActivity", Thread.currentThread().toString() + " Add");
        }

        void remove() {
            if (isEmpty()) {
                throw new IndexOutOfBoundsException();
            } else {
                innerList.remove(MAX_CAPACITY - 1);
            }
            Log.d("WaitNotifyActivity", Thread.currentThread().toString() + " Remove");
        }

        boolean isEmpty() {
            return innerList.isEmpty();
        }

        boolean isFull() {
            return innerList.size() == MAX_CAPACITY;
        }
    }
}
