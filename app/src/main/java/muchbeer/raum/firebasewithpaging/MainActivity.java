package muchbeer.raum.firebasewithpaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import muchbeer.raum.firebasewithpaging.databinding.ActivityMainBinding;
import muchbeer.raum.firebasewithpaging.model.Entity;
import muchbeer.raum.firebasewithpaging.viewmodel.FirebaseViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseAdapter adapter;
    private PagedList<Entity> entityPaging;
    private RecyclerView recyclerView;

    private FirebaseViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Muchbeer Family");

        activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);

        getPopularFamily();

    }

    private void getPopularFamily() {

        mainActivityViewModel.getPagedEntityObservable().observe(this, pagedList -> {

            Log.d(LOG_TAG, "The List of Muchbeer Family is : " + pagedList);

            entityPaging = pagedList;
            showOnRecyclerView();
        });

      /*  mainActivityViewModel.getPagedListObservable()

                .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                           // .subscribe(observer)
                            .subscribe(pagedList -> {

                                Log.d(LOG_TAG, "tHE list of user are : " + pagedList);
                                entityPaging = pagedList;
                            });*/



        // adapter.submitList(pagedList)
      //  showOnRecyclerView();

    }

    private void showOnRecyclerView() {
        recyclerView = activityMainBinding.recyclerView;
        //    movieAdapter = new MovieAdapter(this, moviesMain);
//paging here below
        adapter = new FirebaseAdapter();
        adapter.submitList(entityPaging);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {


            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));


        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
