package muchbeer.raum.firebasewithpaging.repository;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import muchbeer.raum.firebasewithpaging.model.Entity;

public class RemoteQueryDataSource extends ItemKeyedDataSource<Integer, Entity> {

    private RemoteRepository fetchFromRemoteQueryFirebase;

    private RemoteQueryDataSource() {
        fetchFromRemoteQueryFirebase = new RemoteRepository();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Entity> callback) {
        fetchFromRemoteQueryFirebase.fetchFirestoreData(0, params.requestedLoadSize, callback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Entity> callback) {
        fetchFromRemoteQueryFirebase.fetchFirestoreData(params.key, params.requestedLoadSize, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Entity> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull Entity item) {
        return item.getId();
    }
}
