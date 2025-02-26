package com.example.gamecenter.app_interfaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.gamecenter.DTO.UserDTO;
import com.example.gamecenter.R;
import com.example.gamecenter.dbmanagement.UserManager;

public class RegisterFragment extends Fragment {
    private UserManager userManager;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userManager = new UserManager(getContext());
        // inflar la vista
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // views
        EditText etName = view.findViewById(R.id.user_name);
        EditText etPassword = view.findViewById(R.id.et_password);
        Button btnRegister = view.findViewById(R.id.btn_register);
        TextView tvLogin = view.findViewById(R.id.tv_login);

        // listener para registrar un usuario
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                UserDTO userDTO = new UserDTO(name, password);
                if (userManager.addUser(userDTO)){
                    Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    ((UserActivity) requireActivity()).toMenu(userDTO);
                }
                else{
                    Toast.makeText(getActivity(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // cambiar a la pantalla de login
        tvLogin.setOnClickListener(v -> {
            userManager.close();
            ((UserActivity) requireActivity()).changeFragment();
        });

        return view;
    }
}
