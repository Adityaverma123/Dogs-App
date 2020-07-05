package com.example.dogsapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogsapp.R;
import com.example.dogsapp.databinding.ItemDogBinding;
import com.example.dogsapp.model.DogBreed;
import com.example.dogsapp.util.Util;

import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListener{
    private List<DogBreed>dogList;
        public DogsListAdapter(List<DogBreed>dogList)
        {
            this.dogList=dogList;
        }
        public void updateDogsList(List<DogBreed>newDogList)
        {
            dogList.clear();
            dogList.addAll(newDogList);
            notifyDataSetChanged();
        }
    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            ItemDogBinding view= DataBindingUtil.inflate(inflater,R.layout.item_dog,parent,false);
            //View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog,null);

            return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
//        ImageView view=holder.itemView.findViewById(R.id.imageView);
//        TextView name=holder.itemView.findViewById(R.id.name);
//        TextView lifespan=holder.itemView.findViewById(R.id.lifespan);
//        LinearLayout layout=holder.itemView.findViewById(R.id.dogLayout);
//        name.setText(dogList.get(position).dogBreed);
//        lifespan.setText(dogList.get(position).lifespan);
//        Util.loadImage(view,dogList.get(position).imageUrl,Util.getProgressDrawable(view.getContext()));
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ListFragmentDirections.ActionDetail action=ListFragmentDirections.actionDetail();
//                action.setDogUuid(dogList.get(position).uuid);
//                Navigation.findNavController(v).navigate(action);
//            }
//        });
        holder.itemView.setDog(dogList.get(position));
        holder.itemView.setListener(this);
    }

    @Override
    public void onDogClicked(View v) {
        String id=((TextView)v.findViewById(R.id.dogId)).getText().toString();
        int uuid=Integer.valueOf(id);
        ListFragmentDirections.ActionDetail action=ListFragmentDirections.actionDetail();
                action.setDogUuid(uuid);
                Navigation.findNavController(v).navigate(action);
    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder{
        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemView) {
            super(itemView.getRoot());
            this.itemView=itemView;
        }
    }
}
