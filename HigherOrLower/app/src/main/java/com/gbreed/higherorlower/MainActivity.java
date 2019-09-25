package com.gbreed.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    public int ranNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateRandomNumber();
    }

    public void generateRandomNumber()
    {
        Random ram = new Random();
        ranNumber = ram.nextInt(20) + 1;
    }

    public void guessNumber(View view)
    {
        EditText guess = (EditText) findViewById(R.id.editTextGuess);

        int aGuess = Integer.parseInt(guess.getText().toString());
        
        if(aGuess > ranNumber)
        {
            Toast.makeText(this, "Lower!", Toast.LENGTH_SHORT).show();
        }else
            if(aGuess < ranNumber)
            {
                Toast.makeText(this, "Higher!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "You got it! Try again!", Toast.LENGTH_SHORT).show();

                generateRandomNumber();
            }
    }
}
