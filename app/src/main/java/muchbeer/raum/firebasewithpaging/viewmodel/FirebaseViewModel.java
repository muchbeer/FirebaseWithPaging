package muchbeer.raum.firebasewithpaging.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import muchbeer.raum.firebasewithpaging.model.Entity;
import muchbeer.raum.firebasewithpaging.repository.RemoteQueryDataSourceFactory;

public class FirebaseViewModel extends ViewModel {


    private static final int LOADING_PAGE_SIZE = 20;
    private static final int NUMBERS_OF_THREADS = 5;
    private RemoteQueryDataSourceFactory dataSourceFactory;
    private PagedList.Config config;
    private PagedList.Config pagedListConfig;

    final private LiveData<PagedList<Entity>> entityPaged;

    public FirebaseViewModel() {
       dataSourceFactory = new RemoteQueryDataSourceFactory();


      pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(LOADING_PAGE_SIZE)
                .setPageSize(LOADING_PAGE_SIZE).build();

        config = (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setInitialLoadSizeHint(20)
                .setPageSize(25).build();

        Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);

        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
        entityPaged =       livePagedListBuilder.
                setFetchExecutor(executor).
               // setBoundaryCallback(boundaryCallback).
                build();

    }

    public LiveData<PagedList<Entity>> getPagedEntityObservable(){
        return entityPaged;
    }

    public Observable<PagedList> getPagedListObservable(){
        return new RxPagedListBuilder(dataSourceFactory, config).buildObservable();
    }


/*    private PagedList.BoundaryCallback<Movie> boundaryCallback = new PagedList.BoundaryCallback<Movie>() {
        @Override
        public void onZeroItemsLoaded() {
            super.onZeroItemsLoaded();
            liveDataMerger.addSource(database.getMoviesPagingLocal(), mlocalValue -> {
                liveDataMerger.setValue(mlocalValue);
                liveDataMerger.removeSource(database.getMoviesPagingLocal());
            });
        }
    };*/

  /*  public RemoteNetworkCall(RemoteQueryDataSourceFactory dataSourceFactory, PagedList.BoundaryCallback<Entity> boundaryCallback){

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(LOADING_PAGE_SIZE)
                .setPageSize(LOADING_PAGE_SIZE).build();

            Executor executor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);

        LivePagedListBuilder livePagedListBuilder = new LivePagedListBuilder(dataSourceFactory, pagedListConfig);
        entityPaged =       livePagedListBuilder.
                setFetchExecutor(executor).
                setBoundaryCallback(boundaryCallback).
                build();

       // mLocalError = dataSourceFactory.getErrorMessage();

        return entityPaged;
    }*/

}
