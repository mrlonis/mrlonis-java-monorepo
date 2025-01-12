package com.mrlonis.lec3a;

/**
 * Given a treasure map and a starting position, follow the clues on the map to a location containing treasure and dig
 * it up.
 *
 * @author Matthew Lonis
 */
public class TreasureHunt {

    /**
     * Follows the clues in the map and returns the buried treasure.
     *
     * @param start The starting point for the hunt.
     * @param map An array representing the treasure map. The array elements are of type Entry, and each Entry is either
     *     a Clue or the actual Treasure.
     * @return What is found when you dig for treasure after following the clues in the map.
     */
    public static String hunt(int start, Entry[] map) {
        int i = start;
        int n = map.length;

        if ((i >= n) || (i < 0)) {
            throw new ArrrException();
        }

        while (map[i].hasNext()) {
            i += map[i].next();

            if ((i >= n) || (i < 0)) {
                throw new ArrrException();
            }
        }

        try {
            map[i].next();
        } catch (ArrrException e) {
            return map[i].dig();
        }
        return "default return";
    }

    public static void main(String... args) {
        Entry[] map;
        String loot;

        map = new Entry[] {
            new Treasure("gold"), new Clue(1), new Clue(1), new Treasure("silver"),
        };

        loot = hunt(0, map);
        assert "gold".equals(loot);

        map = new Entry[] {
            new Treasure("gold"), new Clue(2), new Clue(1), new Clue(1), new Treasure("silver"),
        };

        loot = hunt(1, map);
        assert "silver".equals(loot);

        assert "diamonds".equals(hunt(0, new Entry[] {
            new Clue(3),
            new Treasure("silver"),
            new Treasure("gold"),
            new Clue(2),
            new Clue(2),
            new Treasure("diamonds"),
        }));

        assert "gold".equals(hunt(1, new Entry[] {
            new Clue(7),
            new Clue(3),
            new Clue(6),
            new Treasure("rubies"),
            new Clue(-2),
            new Treasure("gold"),
            new Clue(1),
            new Treasure("silver"),
            new Clue(-3),
        }));

        assert "rubies".equals(hunt(1, new Entry[] {
            new Clue(-1),
            new Clue(2),
            new Clue(60),
            new Treasure("rubies"),
            new Clue(-2),
            new Treasure("gold"),
            new Clue(1),
            new Treasure("silver"),
            new Clue(-3),
        }));

        assert "c241".equals(hunt(2, new Entry[] {
            new Clue(-1),
            new Clue(2),
            new Clue(3),
            new Treasure("c343"),
            new Clue(5),
            new Treasure("c241"),
            new Clue(-3),
            new Treasure("c212"),
            new Clue(1),
        }));

        try {
            hunt(-1, new Entry[] {
                new Clue(3), new Clue(-1), new Treasure("gold"), new Clue(2), new Treasure("silver"), new Clue(-3),
            });
        } catch (ArrrException e) {
            System.out.println(e);
        }

        try {
            hunt(0, new Entry[] {
                new Clue(2),
                new Treasure("bigmac"),
                new Clue(3),
                new Clue(2),
                new Clue(2),
                new Clue(-1),
                new Clue(3),
                new Clue(3),
                new Treasure("whopper"),
                new Clue(3),
                new Treasure("fries"),
                new Treasure("coke"),
            });
        } catch (ArrrException e) {
            System.out.println(e);
        }

        try {
            hunt(0, new Entry[] {
                new Clue(2),
                new Treasure("bigmac"),
                new Clue(-3),
                new Clue(2),
                new Clue(2),
                new Clue(-1),
                new Clue(3),
                new Clue(3),
                new Treasure("whopper"),
                new Clue(3),
                new Treasure("fries"),
                new Treasure("coke"),
            });
        } catch (ArrrException e) {
            System.out.println(e);
        }

        try {
            hunt(-2, new Entry[] {
                new Clue(2),
                new Treasure("bigmac"),
                new Clue(3),
                new Clue(2),
                new Clue(2),
                new Clue(-1),
                new Clue(3),
                new Clue(3),
                new Treasure("whopper"),
                new Clue(3),
                new Treasure("fries"),
                new Treasure("coke"),
            });
        } catch (ArrrException e) {
            System.out.println(e);
        }
    }
}
