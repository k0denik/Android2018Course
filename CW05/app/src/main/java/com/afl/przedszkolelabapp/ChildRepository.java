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

    public ChildRepository(Application application) {
        ChildRoomDatabase db = ChildRoomDatabase.getInstance(application);
        childDao = db.childDao();
        allChildren = childDao.getAllChildren();
    }

    public LiveData<List<Child>> getAllChildren() {
        return childDao.getAllChildren();
    }

    public void cleanDatabase() {
        //todo:test all this shit
        new clearAsyncTask(childDao).execute();
    }

    private static class clearAsyncTask extends AsyncTask {
        ChildDao cd;

        public clearAsyncTask(ChildDao cd) {
            this.cd = cd;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            cd.clearDatabase();
            return null;
        }
    }

    public void insert(Child child) {
        new insertAsyncTask(childDao).execute(child);
    }

    private static class insertAsyncTask extends AsyncTask<Child, Void, Void> {
        private ChildDao cd;

        public insertAsyncTask(ChildDao cd) {
            this.cd = cd;
        }

        @Override
        protected Void doInBackground(Child... children) {
            cd.insert(children[0]);
            return null;
        }
    }


}
