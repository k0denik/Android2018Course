package com.afl.przedszkolelabapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Jakub Pamuła on 01/05/2018.
 */

public class ChildViewModel extends AndroidViewModel {
    private ChildRepository childRepository;
    private LiveData<List<Child>> childCache;

    public ChildViewModel(@NonNull Application application) {
        super(application);
        childRepository = new ChildRepository(application);
        childCache = childRepository.getAllChildren();
    }

    public LiveData<List<Child>> getallChildren() {
        return childCache;
    }

    public void insertChild(Child child){
        childRepository.insert(child);
    }

    public void cleanDatabase() {
        childRepository.cleanDatabase();
    }
}
