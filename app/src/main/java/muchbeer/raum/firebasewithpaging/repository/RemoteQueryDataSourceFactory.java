package muchbeer.raum.firebasewithpaging.repository;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import muchbeer.raum.firebasewithpaging.model.Entity;

public class RemoteQueryDataSourceFactory extends DataSource.Factory<Integer, Entity> {
    @NonNull
    @Override
    public DataSource<Integer, Entity> create() {
        return new RemoteQueryDataSource();
    }
}
