package com.example.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {
    private UserManager userManager;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userManager = new UserManager(getContext());
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Obtener referencias a los elementos de la UI
        EditText etName = view.findViewById(R.id.user_name);
        EditText etPassword = view.findViewById(R.id.et_password);
        Button btnRegister = view.findViewById(R.id.btn_register);
        TextView tvLogin = view.findViewById(R.id.tv_login);

        // Configurar el evento de clic del botón
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
            ((UserActivity) requireActivity()).changeFragment();
        });

        return view;
    }
}
