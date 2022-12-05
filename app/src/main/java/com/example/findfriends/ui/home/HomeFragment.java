package com.example.findfriends.ui.home;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.findfriends.MainActivity;
import com.example.findfriends.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.homeBtnEnvoyer.setOnClickListener(v->{
            String numerotxt=binding.homeEdNum.getText().toString();
//            envoie de sms : permission SEND_SMS
            if(MainActivity.send_permission){
                //send sms
                SmsManager manager=SmsManager.getDefault();
                manager.sendTextMessage(numerotxt,
                        null,
                        "FindFriends: envoyer votre position plz",
                        null,
                        null );


            }else{
                Toast.makeText(getContext(),"Pas de permission",Toast.LENGTH_LONG).show();
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