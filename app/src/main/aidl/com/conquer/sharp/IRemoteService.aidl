// IRemoteService.aidl
package com.conquer.sharp;

import com.conquer.sharp.binder.MyData;

// Declare any non-default types here with import statements

interface IRemoteService {
    int getPid();
    MyData getMyData();
}
