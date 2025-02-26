package com.example.gamecenter.app_interfaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gamecenter.DTO.UserDTO;
import com.example.gamecenter.R;
import com.example.gamecenter.dbmanagement.UserManager;

public class LoginFragment extends Fragment {
    private UserManager userManager;

    public LoginFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        userManager = new UserManager(getContext());
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // views
        EditText etUsername = view.findViewById(R.id.et_username);
        EditText etPassword = view.findViewById(R.id.et_password);
        Button btnLogin = view.findViewById(R.id.btn_login);
        TextView tvRegister = view.findViewById(R.id.tv_register);

        // listener iniciar sesion
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                if(userManager.checkUser(new UserDTO(username, password))){
                    UserDTO userDTO = new UserDTO(username, password);
                    Toast.makeText(getActivity(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    ((UserActivity) requireActivity()).toMenu(userDTO);
                }
                else{
                    Toast.makeText(getActivity(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // listener para ir a registro
        tvRegister.setOnClickListener(v -> {
            userManager.close();
            ((UserActivity) requireActivity()).changeFragment();
        });

        return view;
    }
}
