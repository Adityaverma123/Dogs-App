package com.example.dogsapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogsapp.R;
import com.example.dogsapp.databinding.FragmentDetailedBinding;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.util.Util;
import com.example.dogsapp.viewmodel.DetailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedFragment extends Fragment {

    private int doguuid;
    private FragmentDetailedBinding view;
    DetailViewModel detailViewModel;
    public DetailedFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDetailedBinding view= DataBindingUtil.inflate(inflater,R.layout.fragment_detailed,container,false);
        this.view=view;

        return view.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailViewModel= ViewModelProviders.of(this).get(DetailViewModel.class);
        if(getArguments()!=null)
        {
            doguuid=DetailedFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }
        detailViewModel.fetch(doguuid);
        observeViewModel();
    }

    private void observeViewModel() {
        detailViewModel.dogLiveData.observe(this, new Observer<DogBreed>() {
            @Override
            public void onChanged(DogBreed dogbreed) {
                if(dogbreed!=null && dogbreed instanceof DogBreed && getContext()!=null)
                {
                    view.setDog(dogbreed);
                }

            }
        });
    }


}
