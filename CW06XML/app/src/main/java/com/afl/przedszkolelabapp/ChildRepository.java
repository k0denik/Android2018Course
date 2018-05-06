package com.afl.przedszkolelabapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Jakub Pamuła on 01/05/2018.
 */

class ChildRepository {
    private ChildDao childDao;
    private LiveData<List<Child>> allChildren;

    ChildRepository(Application application) {
        ChildRoomDatabase db = ChildRoomDatabase.getInstance(application);
        childDao = db.childDao();
        allChildren = childDao.getAllChildren();
    }

    LiveData<List<Child>> getAllChildren() {
        return allChildren;
    }

    void cleanDatabase() {
        new clearAsyncTask(childDao).execute();
    }

    private static class clearAsyncTask extends AsyncTask {
        ChildDao cd;

        clearAsyncTask(ChildDao cd) {
            this.cd = cd;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            cd.clearDatabase();
            return null;
        }
    }

    void insert(Child child) {
        new insertAsyncTask(childDao).execute(child);
    }

    private static class insertAsyncTask extends AsyncTask<Child, Void, Void> {
        private ChildDao cd;

        insertAsyncTask(ChildDao cd) {
            this.cd = cd;
        }

        @Override
        protected Void doInBackground(Child... children) {
            cd.insert(children[0]);
            return null;
        }
    }


}
