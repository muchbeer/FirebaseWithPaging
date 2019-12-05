package muchbeer.raum.firebasewithpaging.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ItemKeyedDataSource;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import muchbeer.raum.firebasewithpaging.model.Entity;

public class RemoteRepository {

    private static final String LOG_TAG = RemoteRepository.class.getSimpleName();
    private static  RemoteRepository sInstance;

    private FirebaseFirestore firestoreDB;

    public String mUsername;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mMessageDbReference;

    public RemoteRepository() {
        mFirebaseDb = FirebaseDatabase.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        mMessageDbReference = mFirebaseDb.getReference();
    }

    public static RemoteRepository getInstance() {
        if (sInstance == null) {
            synchronized (RemoteRepository.class) {
                if (sInstance == null) {
                    sInstance = new RemoteRepository();
                }
            }
        }
        return sInstance;
    }

    public FirebaseDatabase getFirebaseDbReference(){
        return mFirebaseDb;
    }

    public void fetchFirestoreData(final int startUser, final int size,
                                   @NonNull final ItemKeyedDataSource.LoadCallback<Entity> callback) {
        firestoreDB.collection("pagelist")
                .whereGreaterThan("paging", startUser)
                .limit(size)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException errorFirestore) {

                        if (errorFirestore != null) {
                            Log.w("", "exception in fetching from firestore", errorFirestore);
                            return;
                        }

                        List<Entity> userList = new ArrayList<>();
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            userList.add(doc.toObject(Entity.class));
                        }

                        if(userList.size() == 0){
                            return;
                        }

                        if(callback instanceof ItemKeyedDataSource.LoadInitialCallback){
                            //initial load
                            ((ItemKeyedDataSource.LoadInitialCallback)callback)
                                    .onResult(userList, 0, userList.size());
                        }else{
                            //next pages load
                            callback.onResult(userList);
                        }
                    }
                });

    }
    public void fetchData() {
        mFirebaseDb = FirebaseDatabase.getInstance();

        mMessageDbReference = mFirebaseDb.getReference().child("messagelivedata");

        mMessageDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null) {
                    Entity retrieveUser = dataSnapshot.getValue(Entity.class);
                    Log.d(LOG_TAG, "tHE out data is : " + retrieveUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(LOG_TAG, "tHE Error retrieve the data is: " + databaseError.getMessage());
            }
        });
    }
    public void setUserName(String userName){
        mUsername = userName;
    }

    public String getUserName(){
        return mUsername;
    }

}
