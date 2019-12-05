package muchbeer.raum.firebasewithpaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

        mainActivityViewModel.getPagedListObservable().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer)
                            .subscribe(pagedList -> adapter.submitList(pagedList));
    }
}
