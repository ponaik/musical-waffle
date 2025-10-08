package com.intern.javacore.skynet;

import java.util.concurrent.Phaser;

public class RobotWars {

    public static void main(String[] args) throws InterruptedException {
        final int DAYS = 100;
        Factory factory = new Factory();
        Phaser phaser = new Phaser(3); // factory + 2 factions

        // Factory thread
        Thread factoryThread = new Thread(() -> {
            for (int day = 0; day < DAYS; day++) {
                // Day: produce parts
                factory.produceRandomParts();
                phaser.arriveAndAwaitAdvance(); // signal production done
                // Night: Wait till factions finish collection
                phaser.arriveAndAwaitAdvance();
            }
        }, "Factory");

        Faction world = new Faction("World", factory, phaser, DAYS);
        Faction wednesday = new Faction("Wednesday", factory, phaser, DAYS);

        Thread tWorld = new Thread(world, "World");
        Thread tWed = new Thread(wednesday, "Wednesday");

        // Start threads
        factoryThread.start();
        tWorld.start();
        tWed.start();

        // Wait for threads to finish
        factoryThread.join();
        tWorld.join();
        tWed.join();

        // Final results
        int robotsWorld = world.getRobotsBuilt();
        int robotsWed = wednesday.getRobotsBuilt();

        System.out.println("----- Simulation complete after " + DAYS + " days -----");
        System.out.println("World robots: " + robotsWorld + " Inventory: " + world.getInventoryString());
        System.out.println("Wednesday robots: " + robotsWed + " Inventory: " + wednesday.getInventoryString());

        if (robotsWorld > robotsWed) {
            System.out.println("Winner: World");
        } else if (robotsWed > robotsWorld) {
            System.out.println("Winner: Wednesday");
        } else {
            System.out.println("Result: Tie");
        }
    }
}
