package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

import java.util.List;

// Fragment displaying addresses as the main page
public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SharedLocationViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DBHelper dataBaseHelper = new DBHelper(getContext());
        List<Location> locationList = dataBaseHelper.getEveryone();

        ArrayAdapter<Location> locationArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, locationList);
        viewModel = new ViewModelProvider(getActivity()).get(SharedLocationViewModel.class);
        binding = FragmentFirstBinding.inflate(inflater, container, false);

        CustomAdapter customBaseAdapter = new CustomAdapter(getContext(), locationList);
        binding.listview.setAdapter(customBaseAdapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customBaseAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customBaseAdapter.getFilter().filter(newText);
                return false;
            }
        });

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);

                Location selectedLocation = locationArrayAdapter.getItem(position);
                viewModel.setData(selectedLocation);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                viewModel.setData(null);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
