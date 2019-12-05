package muchbeer.raum.firebasewithpaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import muchbeer.raum.firebasewithpaging.databinding.ListItemBinding;
import muchbeer.raum.firebasewithpaging.model.Entity;

public class FirebaseAdapter extends PagedListAdapter<Entity, FirebaseAdapter.FirebaseAdapterViewHolder> {


    protected FirebaseAdapter() {
        super(DIFF_CALLBACK_FIREBASE);
    }
public static DiffUtil.ItemCallback<Entity> DIFF_CALLBACK_FIREBASE =
            new DiffUtil.ItemCallback<Entity>() {
                @Override
                public boolean areItemsTheSame(@NonNull Entity oldItem, @NonNull Entity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Entity oldItem, @NonNull Entity newItem) {
                    return oldItem.equals(newItem);
                }
            };


    @NonNull
    @Override
    public FirebaseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.list_item,
                    parent,
                    false);
     /*   Context context;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);*/
        return new FirebaseAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseAdapterViewHolder holder, int position) {
        Entity userEntity = getItem(position);

        holder.binding.setUsermessage(userEntity);

        if(userEntity.getPhotoUrl() != null)
            Glide.with(holder.binding.photoImageView.getContext())
                        .load(userEntity.getPhotoUrl())
                    .thumbnail(0.01f)
                    .into(holder.binding.photoImageView);

        holder.binding.executePendingBindings();
    }

    public class FirebaseAdapterViewHolder extends RecyclerView.ViewHolder {

        ListItemBinding binding;
        public FirebaseAdapterViewHolder(@NonNull ListItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
