package com.example.shutda.view.Ingame;

import android.content.Context;

public interface Command {

    boolean finish();

    void execute(Context context);

    void initiate();

}
