package com.conquer.sharp.manager;

/**
 * 单例模式
 */
public class UserManager {
    private static class SingletonHolder {
        public static UserManager instance = new UserManager();
    }

    private UserManager(){}

    public static UserManager newInstance() {
        return SingletonHolder.instance;
    }


}
