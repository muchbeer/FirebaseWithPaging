package muchbeer.raum.firebasewithpaging.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.ItemKeyedDataSource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import muchbeer.raum.firebasewithpaging.model.Entity;

public class RemoteRepository {

    private static final String LOG_TAG = RemoteRepository.class.getSimpleName();
    private static  RemoteRepository sInstance;

    private FirebaseFirestore firestoreDB;

    private String mUsername;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mMessageDbReference;

    private RemoteRepository() {
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

    private void fetchFirestoreData(final int startUser, final int size,
                                   @NonNull final ItemKeyedDataSource.LoadCallback<Entity> callback) {
     //   firestoreDB.collection("pagelist")

               firestoreDB.collection("pagelist")
                                            .whereGreaterThan("id", startUser)
                                            .limit(size)
                                             .get()

                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(LOG_TAG, "Retrieve all Document as follows in Paging : " + queryDocumentSnapshots.getDocuments());

                        List<Entity> userList = new ArrayList<>();
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            userList.add(doc.toObject(Entity.class));
                            Log.d(LOG_TAG, "tHE output paging list is : " + userList);
                        }

                        if(userList.size() == 0){
                            Log.d(LOG_TAG, "We did not get anything from paging firestore : " + userList);
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception failError) {
                Log.d(LOG_TAG, "exception in fetching from firestore", failError);
            }
        });

       // fetchData();
    }
    public void fetchData() {

        firestoreDB.collection("pagelist")
                .whereGreaterThan("id", 0)
                .limit(25)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(LOG_TAG, "Retrieve all Document as follows using on Success: " + queryDocumentSnapshots.getDocuments());

                        List<Entity> userList = new ArrayList<>();
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            userList.add(doc.toObject(Entity.class));
                            Log.d(LOG_TAG, "tHE output list is of Success : " + userList);
                        }

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(LOG_TAG, "Retrieve all Document as follows complete : " + task.getResult());


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(LOG_TAG, "tHE value document is: " + document.getData());
                              //  Log.d(LOG_TAG, document.getId() + " => " + document.getData());
                                List<Entity> userList = new ArrayList<>();
                                userList.add(document.toObject(Entity.class));
                                Log.d(LOG_TAG, "tHE value that expected is: " + userList);
                            }
                        } else {
                            Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(LOG_TAG, "Error getting documents: ", e);
            }
        });
    }

    //To store Firestore Locally we use
    public void setup() {
        // [START get_firestore_instance]
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }
    public void setUserName(String userName){
        mUsername = userName;
    }

    public String getUserName(){
        return mUsername;
    }

}
