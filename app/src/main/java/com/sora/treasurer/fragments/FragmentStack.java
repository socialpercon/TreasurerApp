package com.sora.treasurer.fragments;

import android.util.Log;

import com.sora.treasurer.enums.ViewScreen;

import java.util.ArrayList;

public class FragmentStack {
    private ViewScreen currentScreen;
    private ArrayList<ViewScreen> screenStack;

    public FragmentStack(){
        this.currentScreen = null;
        screenStack = new ArrayList<>();
    }

    public void addFragmentToStack(ViewScreen screen) {
        this.currentScreen = screen;
        while(this.screenStack.contains(screen)) {
            this.screenStack.remove(screen);
        }
        this.screenStack.add(screen);
        Log.d("addStack", stackContent());
    }

    public ViewScreen popFragmentFromStack() {
        if(screenStack.size() > 0) {
            int lastIndex = screenStack.size() - 1;
            ViewScreen screen = screenStack.get(lastIndex);
            screenStack.remove(lastIndex);
            Log.d("popStack", stackContent());
            if(currentScreen.getValue() == screen.getValue()) screen = popFragmentFromStack();
            currentScreen = screen;
            return screen;
        } else {
            Log.d("popStack", stackContent());
            return null;
        }
    }

    public void removeFromStack() {
        screenStack.remove(screenStack.size()-1);
    }

    public void clearFragmentStack() {
        screenStack.clear();
    }

    public void setCurrentScreen(ViewScreen screen) {
        this.currentScreen = screen;
    }

    public boolean isEmpty() {
        return this.screenStack.isEmpty();
    }

    private String stackContent() {
        String content = "{ ";
        for (ViewScreen screen : screenStack) {
            content += screen;
            content += ", ";
        }
        content += " }";
        return content;
    }
}
