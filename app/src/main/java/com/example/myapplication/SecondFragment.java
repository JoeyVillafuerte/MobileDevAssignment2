package com.example.myapplication;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentSecondBinding;

// Page focused on adding and editing coordinates
public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private SharedLocationViewModel viewModel;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(getActivity()).get(SharedLocationViewModel.class);
        viewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                this.location = data;
                binding.editLatitude.setText(data.getLatitude());
                binding.editLongitude.setText(data.getLongitude());
            }
        });
        return binding.getRoot();
    }

    private boolean areInputsValid() {
        String latitude = binding.editLatitude.getText().toString();
        String longitude = binding.editLongitude.getText().toString();

        boolean validLongitude = true;
        boolean validLatitude = true;

        if (latitude != null && !latitude.isEmpty()) {
            validLatitude = Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90;
        }

        if (longitude != null && !longitude.isEmpty()) {
            validLongitude = Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude) < -180;
        }

        return latitude.isEmpty() || longitude.isEmpty() || validLatitude || validLongitude;
    }

    private void styleErrorInputs() {
        String latitude = binding.editLatitude.getText().toString();
        String longitude = binding.editLongitude.getText().toString();

        int color = Color.rgb(255, 0, 0);
        int color2 = Color.parseColor("#FF11AA");

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_hovered},
                new int[]{android.R.attr.state_enabled},
                new int[]{0}
        };

        int[] colors = new int[]{
                color,
                color,
                color,
                color2
        };

        ColorStateList myColorList = new ColorStateList(states, colors);

        if (latitude.isEmpty()) {
            binding.editLatitudeLayout.setError("Required");
        } else if (longitude.isEmpty()) {
            binding.editLongitudeLayout.setError("Required");
        } else {
            binding.editLatitudeLayout.setError(null);
        }

        if (latitude != null && !latitude.isEmpty()) {
            if (Double.parseDouble(latitude) > 90 || Double.parseDouble(latitude) < -90) {
                binding.editLatitudeLayout.setError("The value needs to be between -90 degrees and 90 degrees");
            } else {
                binding.editLatitudeLayout.setError(null);
            }
        }

        if (longitude != null && !longitude.isEmpty()) {
            if (Double.parseDouble(longitude) > 180 || Double.parseDouble(longitude) < -180) {
                binding.editLongitudeLayout.setError("The value needs to be between -180 degrees and 180 degrees");
            } else {
                binding.editLongitudeLayout.setError(null);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.submitButton.setOnClickListener(v -> {
            if (!areInputsValid()) {
                String latitude = binding.editLatitude.getText().toString();
                String longitude = binding.editLongitude.getText().toString();
                DBHelper dataBaseHelper = new DBHelper(getContext());

                if (location != null) {
                    Location updatedLocationModel = new Location(location.getId(), latitude, longitude);
                    dataBaseHelper.updateOne(updatedLocationModel);
                    Toast.makeText(getContext(), "Location Successfully Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Location location;
                    try {
                        location = new Location(latitude, longitude);
                    } catch (Exception e) {
                        location = new Location(-1, "error", "error", "error");
                    }
                    boolean success = dataBaseHelper.addOne(location);
                    Toast.makeText(getContext(), "Location Successfully Added", Toast.LENGTH_SHORT).show();
                }

                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }

            styleErrorInputs();
        });

        binding.deleteButton.setOnClickListener(v -> {
            DBHelper dataBaseHelper = new DBHelper(getContext());
            if (location != null) {
                dataBaseHelper.deleteOne(location.getId());
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
                Toast.makeText(getContext(), "Location Successfully Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Location Cannot be Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
