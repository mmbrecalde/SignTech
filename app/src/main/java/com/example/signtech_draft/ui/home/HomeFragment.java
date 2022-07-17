package com.example.signtech_draft.ui.home;

import static com.example.signtech_draft.MainActivity.activityVisible;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signtech_draft.MainActivity;
import com.example.signtech_draft.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import com.example.signtech_draft.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    //Database Variables
    private DatabaseReference mDatabase;
    double totalCorrect;
    double totalWrong;

    String[] questions = {"a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y",
            "z", "1", "2", "3", "4",
            "5", "6", "7", "8", "9",
            "10", "and"};

    int[] images = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i,
            R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m, R.drawable.n,
            R.drawable.o, R.drawable.p, R.drawable.q, R.drawable.r, R.drawable.s,
            R.drawable.t, R.drawable.u, R.drawable.v, R.drawable.w, R.drawable.x,
            R.drawable.y, R.drawable.z, R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
            R.drawable.nine, R.drawable.ten, R.drawable.and};

    int[] progressBars = new int[]{
            R.id.progress1, R.id.progress2, R.id.progress3, R.id.progress4, R.id.progress5,
            R.id.progress6, R.id.progress7, R.id.progress8, R.id.progress9, R.id.progress10,
            R.id.progress11, R.id.progress12, R.id.progress13, R.id.progress14, R.id.progress15,
            R.id.progress16, R.id.progress17, R.id.progress18, R.id.progress19, R.id.progress20,
            R.id.progress21, R.id.progress22, R.id.progress23, R.id.progress24, R.id.progress25,
            R.id.progress26, R.id.progress27, R.id.progress28, R.id.progress29, R.id.progress30,
            R.id.progress31, R.id.progress32, R.id.progress33, R.id.progress34, R.id.progress35,
            R.id.progress36, R.id.progress37
    };

    int[] textLetters = new int[]{
            R.id.textLetter1, R.id.textLetter2, R.id.textLetter3, R.id.textLetter4, R.id.textLetter5,
            R.id.textLetter6, R.id.textLetter7, R.id.textLetter8, R.id.textLetter9, R.id.textLetter10,
            R.id.textLetter11, R.id.textLetter12, R.id.textLetter13, R.id.textLetter14, R.id.textLetter15,
            R.id.textLetter16, R.id.textLetter17, R.id.textLetter18, R.id.textLetter19, R.id.textLetter20,
            R.id.textLetter21, R.id.textLetter22, R.id.textLetter23, R.id.textLetter24, R.id.textLetter25,
            R.id.textLetter26, R.id.textLetter27, R.id.textLetter28, R.id.textLetter29, R.id.textLetter30,
            R.id.textLetter31, R.id.textLetter32, R.id.textLetter33, R.id.textLetter34, R.id.textLetter35,
            R.id.textLetter36, R.id.textLetter37
    };
    LinearLayout progressList;

    final Handler handler = new Handler();

    int i;

    //Binding
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //Database Variables
                mDatabase = FirebaseDatabase.getInstance().getReference();
                progressList = root.findViewById(R.id.progressList);

                mDatabase.child("Questions").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (activityVisible) {

                            for (i = 0; i <= 36 ; i++) {
                                TextView textQ = root.findViewById(textLetters[i]);
                                ((View) textQ.getParent().getParent()).setVisibility(View.VISIBLE);

                            }

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                for (i = 0; i <= 36; i++) {
                                    String key = postSnapshot.getKey();
                                    String key2 = questions[i];
                                    if (key.equalsIgnoreCase(key2)) {
                                        //TextView
                                        TextView textQ = root.findViewById(textLetters[i]);
                                        textQ.setText(questions[i].toUpperCase(Locale.ROOT));
                                        //Progress
                                        for (DataSnapshot childSnapshot : postSnapshot.getChildren()) {
                                            String childKey = childSnapshot.getKey();
                                            if (childKey.equalsIgnoreCase("correct")) {
                                                String convertString = String.valueOf(childSnapshot.getValue());
                                                totalCorrect = Integer.parseInt(convertString);
                                            }
                                            if (childKey.equalsIgnoreCase("wrong")) {
                                                String convertString = String.valueOf(childSnapshot.getValue());
                                                totalWrong = Integer.parseInt(convertString);
                                            }
                                        }

                                        double sum = totalCorrect + totalWrong;
                                        double prog = totalCorrect / sum * 100.0;
                                        ProgressBar progressQ = root.findViewById(progressBars[i]);
                                        progressQ.setProgress((int) prog);
                                    }
                                }
                            }

                            for (i = 0; i <= 36; i++) {
                                TextView textQ = root.findViewById(textLetters[i]);
                                String text = String.valueOf(textQ.getText());
                                if (text.equalsIgnoreCase("")) {
                                    ((View) textQ.getParent()).setVisibility(View.GONE);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(root.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, 2000);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}