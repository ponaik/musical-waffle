package com.intern.javacore.skynet;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

class Faction implements Runnable {
    private final String name;
    private final Factory factory;
    private final Phaser phaser;
    private final EnumMap<Part, Integer> inventory = new EnumMap<>(Part.class);
    private final int carryLimit = 5;
    private final AtomicInteger robotsBuilt = new AtomicInteger(0);
    private final int days;

    public Faction(String name, Factory factory, Phaser phaser, int days) {
        this.name = name;
        this.factory = factory;
        this.phaser = phaser;
        this.days = days;
        for (Part p : Part.values()) inventory.put(p, 0);
    }

    @Override
    public void run() {
        // Registered in Phaser by caller
        for (int day = 0; day < days; day++) {
            // Day: Wait for production phase to finish
            phaser.arriveAndAwaitAdvance(); // after factory produce
            // Night: try to take parts
            takePartsNight();
            assembleRobots();
            phaser.arriveAndAwaitAdvance(); // signal finished night
        }
    }

    private void takePartsNight() {
        // prepare need map for one robot deficits * large number to express preference
        Map<Part, Integer> needOrder = Map.of(
                Part.HEAD, 1,
                Part.TORSO, 1,
                Part.HAND, 2,
                Part.FOOT, 2
        );
        EnumMap<Part, Integer> taken = factory.takePartsGreedy(needOrder, carryLimit);
        // merge taken into inventory
        for (Part p : Part.values()) {
            int t = taken.getOrDefault(p, 0);
            if (t > 0) {
                inventory.put(p, inventory.get(p) + t);
            }
        }
        System.out.printf("[%s] Took parts: %s; Inventory now: %s%n",
                name, taken.toString(), inventory.toString());
    }

    private void assembleRobots() {
        int possible = Integer.MAX_VALUE;
        possible = Math.min(possible, inventory.get(Part.HEAD));
        possible = Math.min(possible, inventory.get(Part.TORSO));
        possible = Math.min(possible, inventory.get(Part.HAND) / 2);
        possible = Math.min(possible, inventory.get(Part.FOOT) / 2);
        if (possible > 0) {
            // build all possible robots now
            inventory.put(Part.HEAD, inventory.get(Part.HEAD) - possible);
            inventory.put(Part.TORSO, inventory.get(Part.TORSO) - possible);
            inventory.put(Part.HAND, inventory.get(Part.HAND) - possible * 2);
            inventory.put(Part.FOOT, inventory.get(Part.FOOT) - possible * 2);
            robotsBuilt.addAndGet(possible);
            System.out.printf("[%s] Built %d robots (total %d). Inventory after build: %s%n",
                    name, possible, robotsBuilt.get(), inventory.toString());
        }
    }

    public int getRobotsBuilt() {
        return robotsBuilt.get();
    }

    public String getInventoryString() {
        return inventory.toString();
    }
}
