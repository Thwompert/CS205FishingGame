package com.example.cs205fishinggame.Diver;

public class DiverThread extends Thread {
    private boolean running = false;
    private final Diver diver;

    public DiverThread(Diver diver) {
        this.diver = diver;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            long currentTime = System.currentTimeMillis();
            diver.update(currentTime);
            try {
                Thread.sleep(16); // Aim for a 60 FPS update rate
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
