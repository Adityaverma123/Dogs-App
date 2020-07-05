package com.example.dogsapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.model.DogDatabase;

import java.util.List;

public class DetailViewModel extends AndroidViewModel {
   public MutableLiveData<DogBreed>dogLiveData=new MutableLiveData<DogBreed>();
   private RetrieveData task;

   public DetailViewModel(@NonNull Application application) {
      super(application);
   }

   public void fetch(int uuid)
  {
      task=new RetrieveData();
      task.execute(uuid);

  }
  public class RetrieveData extends AsyncTask<Integer,Void,DogBreed>
  {

     @Override
     protected DogBreed doInBackground(Integer... integers) {
        int uuid=integers[0];
        return DogDatabase.getInstance(getApplication()).dogDao()
                .getDogbreed(uuid);
     }

     @Override
     protected void onPostExecute(DogBreed dogBreed) {
        super.onPostExecute(dogBreed);
        dogLiveData.setValue(dogBreed);
     }
  }

   @Override
   protected void onCleared() {
      super.onCleared();
      if(task!=null)
      {
         task.cancel(true);
         task=null;
      }
   }
}
