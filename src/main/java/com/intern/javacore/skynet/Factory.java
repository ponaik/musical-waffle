package com.intern.javacore.skynet;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Factory {
    private final EnumMap<Part, Integer> stock = new EnumMap<>(Part.class);
    private final Random rnd = new Random();

    public Factory() {
        for (Part p : Part.values()) stock.put(p, 0);
    }

    // Produce up to 10 random parts
    public synchronized void produceRandomParts() {
        int count = rnd.nextInt(11); // 0..10
        for (int i = 0; i < count; i++) {
            Part p = Part.values()[rnd.nextInt(Part.values().length)];
            stock.put(p, stock.get(p) + 1);
        }
        System.out.printf("[Factory] Produced %d parts. Stock now: %s%n", count, stockSnapshot());
    }

    // A faction calls this to take up to maxParts. Returns map of parts taken.
    public synchronized EnumMap<Part, Integer> takePartsGreedy(Map<Part, Integer> needOrder, int maxParts) {
        EnumMap<Part, Integer> taken = new EnumMap<>(Part.class);
        for (Part p : Part.values()) taken.put(p, 0);

        int takenCount = 0;
        // Greedy loop: try to satisfy needs in order of deficits repeatedly
        while (takenCount < maxParts) {
            Part pick = null;
            int maxDeficit = 0;
            for (Part p : Part.values()) {
                int deficit = Math.max(0, needOrder.getOrDefault(p, 0) - taken.get(p));
                // If already no deficit, still allow taking if factory has surplus to later build more robots
                if (deficit > maxDeficit && stock.get(p) > 0) {
                    maxDeficit = deficit;
                    pick = p;
                }
            }
            if (pick == null) {
                // No deficit or deficits not available, pick any available part (prefer hands/feet because need 2)
                if (stock.get(Part.HAND) > 0) pick = Part.HAND;
                else if (stock.get(Part.FOOT) > 0) pick = Part.FOOT;
                else if (stock.get(Part.HEAD) > 0) pick = Part.HEAD;
                else if (stock.get(Part.TORSO) > 0) pick = Part.TORSO;
            }
            if (pick == null) break; // factory empty

            // take one
            stock.put(pick, stock.get(pick) - 1);
            taken.put(pick, taken.get(pick) + 1);
            takenCount++;

        }
        System.out.println("[Factory] Remaining total right after a take: " + totalStock());
        return taken;
    }

    public synchronized String stockSnapshot() {
        return stock.toString();
    }

    public synchronized int totalStock() {
        return stock.values().stream().mapToInt(Integer::intValue).sum();
    }
}
