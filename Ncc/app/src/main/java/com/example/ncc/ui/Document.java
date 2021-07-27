package com.example.ncc.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ncc.databinding.DocumentFragmentBinding;

public class Document extends Fragment {

    private DocumentViewModel mViewModel;
    private DocumentFragmentBinding binding;

    public static Document newInstance() {
        return new Document();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ActionBar ab;
        ab = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(ab != null){
            ab.setDisplayHomeAsUpEnabled(false);
        }
        mViewModel = new ViewModelProvider(this).get(DocumentViewModel.class);
        // TODO: Use the ViewModel
        binding = DocumentFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.Document;
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}