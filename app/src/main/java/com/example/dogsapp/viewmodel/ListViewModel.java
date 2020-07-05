package com.example.dogsapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.dogsapp.model.DogApiService;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.model.DogDao;
import com.example.dogsapp.model.DogDatabase;
import com.example.dogsapp.util.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {
    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> error = new MutableLiveData<Boolean>();
    private AsyncTask<List<DogBreed>,Void,List<DogBreed>>insertTask;
    private AsyncTask<Void,Void,List<DogBreed>> retrieveTask;
    private SharedPreferenceHelper helper=SharedPreferenceHelper.getInstance(getApplication());
    long refreshTime=5*60*1000*1000*1000L;
    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    private DogApiService apiService = new DogApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void refresh() {
        long updateTime=helper.getUpdateTime();
        long currentTime=System.nanoTime();
        if(updateTime!=0 &&currentTime-updateTime<refreshTime)
        {
            fetchFromDatabase();
        }
        else {
            fetchFromRemote();
        }
    }
    void fetchFromDatabase()
    {
        loading.setValue(true);
        retrieveTask=new RetrieveDogsTask();
        retrieveTask.execute();

    }
    public void refreshBypassCache()
    {
        fetchFromRemote();
    }
    public void fetchFromRemote() {
        loading.setValue(true);
        disposable.add(apiService.getDogs().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(List<DogBreed> dogBreeds) {
                        insertTask=new InsertDogsTask();
                        insertTask.execute(dogBreeds);
                        Toast.makeText(getApplication(),"Dogs retreived from end point",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.setValue(false);
                        error.setValue(true);
                        e.printStackTrace();
                    }
                })
        );
    }

    private void dogsRetrieved(List<DogBreed>dogList) {
        dogs.setValue(dogList);
        loading.setValue(false);
        error.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        if(insertTask!=null)
        {
            insertTask.cancel(true);
            insertTask=null;
        }
        if(retrieveTask!=null)
        {
            retrieveTask.cancel(true);
            retrieveTask=null;
        }
    }
    private class InsertDogsTask extends AsyncTask<List<DogBreed>,Void,List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed>list=lists[0];
            DogDao dao= DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();
            ArrayList<DogBreed>newlist=new ArrayList<>(list);
            List<Long>results=dao.insertAll(newlist.toArray(new DogBreed[0]));
            int i=0;
            while (i<list.size())
            {
                list.get(i).uuid= results.get(i).intValue();
                i++;
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            super.onPostExecute(dogBreeds);
            dogsRetrieved(dogBreeds);
            helper.saveUpdateTime(System.nanoTime());
        }
    }
    private class RetrieveDogsTask extends AsyncTask<Void,Void,List<DogBreed>>
    {

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {

            return DogDatabase.getInstance(getApplication()).dogDao()
                    .getAllDogs();
        }

        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            super.onPostExecute(dogBreeds);
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(),"Dogs retreived from database",Toast.LENGTH_SHORT).show();

        }
    }
}

