package muchbeer.raum.firebasewithpaging.viewmodel;


import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import io.reactivex.Observable;
import muchbeer.raum.firebasewithpaging.repository.RemoteQueryDataSourceFactory;

public class FirebaseViewModel extends ViewModel {



    private RemoteQueryDataSourceFactory dataSourceFactory;
    private PagedList.Config config;

    public FirebaseViewModel() {
       dataSourceFactory = new RemoteQueryDataSourceFactory();

        config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(20)
                .setPageSize(25).build();
    }

    public Observable<PagedList> getPagedListObservable(){
        return new RxPagedListBuilder(dataSourceFactory, config).buildObservable();
    }
}
