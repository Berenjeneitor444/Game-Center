package com.example.gamecenter.app_interfaces;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamecenter.DTO.UserDTO;
import com.example.gamecenter.R;

public class UserActivity extends AppCompatActivity {
    private final RegisterFragment registerFragment = new RegisterFragment();
    private final LoginFragment loginFragment = new LoginFragment();
    private FragmentType currentFragment = FragmentType.LOGIN;
    private enum FragmentType { REGISTER, LOGIN }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // cargar el fragmento de login al principio
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, loginFragment);
            transaction.commit();
        }
    }
    // cambiar de login a register
    public void changeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = currentFragment == FragmentType.LOGIN ? registerFragment : loginFragment;
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        currentFragment = currentFragment == FragmentType.LOGIN ? FragmentType.REGISTER : FragmentType.LOGIN;
    }
    // ir al menu una vez iniciada la sesion
    public void toMenu(UserDTO userDTO){
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        intent.putExtra("current_user", userDTO);
        startActivity(intent);
    }
}
