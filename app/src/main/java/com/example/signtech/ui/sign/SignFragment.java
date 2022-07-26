package com.example.signtech.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.signtech.R;
import com.example.signtech.databinding.FragmentSignBinding;

public class SignFragment extends Fragment {

    private FragmentSignBinding binding;

    Button btnCam;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSignBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnCam = root.findViewById(R.id.btnCamera);

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rawr = new Intent(root.getContext(), SignActivity.class);
                startActivity(rawr);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}