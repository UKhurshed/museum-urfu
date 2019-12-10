package ru.urfu.museum.classes;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.urfu.museum.R;
import ru.urfu.museum.utils.Preference;

public class MocksProvider {
    private static Map<String, List<Entry>> entries = new HashMap<>();

    public static List<Entry> getEntries(Context context, int floor) {
        init();
        String lang = Preference.getValue(context, Preference.LANG, KeyWords.LANG_EN);
        List<Entry> filtered = new ArrayList<>();
        if (entries.containsKey(lang)) {
            List<Entry> langEntries = entries.get(lang);
            if (langEntries == null) {
                return filtered;
            }
            for (Entry entry : langEntries) {
                if (entry.floor != floor) {
                    continue;
                }
                filtered.add(entry);
            }
        }
        return filtered;
    }

    public static Entry getEntry(Context context, int id) {
        init();
        String lang = Preference.getValue(context, Preference.LANG, KeyWords.LANG_EN);
        if (entries.containsKey(lang)) {
            List<Entry> langEntries = entries.get(lang);
            if (langEntries == null) {
                return null;
            }
            for (Entry entry : langEntries) {
                if (entry.id != id) {
                    continue;
                }
                return entry;
            }
        }
        return null;
    }

    public static Entry getPrevEntry(Context context, int id) {
        init();
        String lang = Preference.getValue(context, Preference.LANG, KeyWords.LANG_EN);
        if (entries.containsKey(lang)) {
            List<Entry> langEntries = entries.get(lang);
            if (langEntries == null) {
                return null;
            }
            Iterator<Entry> iterator = langEntries.iterator();
            Entry prevEntry;
            Entry currentEntry = null;
            while (iterator.hasNext()) {
                prevEntry = currentEntry;
                currentEntry = iterator.next();
                if (currentEntry.id == id) {
                    return prevEntry;
                }
            }
        }
        return null;
    }

    public static Entry getNextEntry(Context context, int id) {
        init();
        String lang = Preference.getValue(context, Preference.LANG, KeyWords.LANG_EN);
        if (entries.containsKey(lang)) {
            List<Entry> langEntries = entries.get(lang);
            if (langEntries == null) {
                return null;
            }
            Iterator<Entry> iterator = langEntries.iterator();
            Entry currentEntry;
            while ((currentEntry = iterator.next()) != null && iterator.hasNext()) {
                if (currentEntry.id == id) {
                    return iterator.next();
                }
            }
        }
        return null;
    }

    public static ArrayList<Integer> getEntryImages(Context context, int id) {
        Entry entry = MocksProvider.getEntry(context, id);
        ArrayList<Integer> list = new ArrayList<>();
        if (entry == null || entry.gallery == null || entry.gallery.length == 0) {
            return list;
        }
        for (int i = 0; i < entry.gallery.length; i++) {
            list.add(entry.gallery[i]);
        }
        return list;
    }

    public static boolean containsEntry(Context context, int id) {
        return MocksProvider.getEntry(context, id) != null;
    }

    private static void init() {
        if (entries.keySet().size() == 0) {
            entries.put(KeyWords.LANG_EN, getEnglishEntries());
            entries.put(KeyWords.LANG_ZH, getChineseEntries());
        }
    }

    private static List<Entry> getEnglishEntries() {
        Entry[] entries = {
                new Entry(
                        1,
                        R.drawable.entry_1_1,
                        "Boris Yeltsin 1",
                        "Butka",
                        "From early childhood Boris was energetic and courageous. In his youth, he changed schools several times, in each new classroom, he was among the leaders in academic performance and social activism. He was always elected class President. Thanks to his high growth and athletic physique, Boris Nikolayevich played for the national Berezniki team in volleyball. In district and city, he was known as an activist and a bright young man. Therefore, could pass the final school exams as an external student a year earlier. The precedent was enormous-many teachers were against such innovations, but still persistent Boris achieved his goal. In 1950 he entered the Ural Polytechnical Institute of construction faculty. Studying at the University for Yeltsin was not difficult like studying in school. Through all his academic cursus his grades were whether \"good\" or \"excellent\". At the Institute, Yeltsin continued to play sports as a national team player in volleyball: first at the Ural polytechnical Institute, and then Sverdlovsk. After graduating from the University in 1955, Yeltsin took a job in a construction trust. Not in the position of a master, which was offered to the graduate of the construction faculty, but as a simple plasterer. As Boris Nikolayevich himself will later tell, he considered it necessary to master all construction specialties before starting to manage people on the construction site. A year later, having worked on construction projects in the twelve specialties of the trust, he took the position of the master. In 1961, Yeltsin joined the CPSU, and later became chief engineer of Sverdlovsk house-building combine. From now on, Boris Nikolayevich begins to build not only new buildings in the city, but also its own political career. In fifteen years of membership and work in the party, Yeltsin passed from candidate of the CPSU to Secretary of the Sverdlovsk regional Committee. His rapid career growth reached the highest rank. On June 12, 1991 Boris Yeltsin becomes the first President of Russian Federation. Having won huge people’s sympathy, Yeltsin became the first elected, not appointed President. His hard work and perseverance became an example for many colleagues and friends. They couldn't even imagine, that for a long time they were in the same team with the future President Boris Nikolayevich Yeltsin. President of the biggest and greatest country in the world.",
                        new int[]{R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1},
                        1
                ),
                new Entry(
                        2,
                        R.drawable.entry_1_1,
                        "Boris Yeltsin 2",
                        "Butka",
                        "From early childhood Boris was energetic and courageous. In his youth, he changed schools several times, in each new classroom, he was among the leaders in academic performance and social activism. He was always elected class President. Thanks to his high growth and athletic physique, Boris Nikolayevich played for the national Berezniki team in volleyball. In district and city, he was known as an activist and a bright young man. Therefore, could pass the final school exams as an external student a year earlier. The precedent was enormous-many teachers were against such innovations, but still persistent Boris achieved his goal. In 1950 he entered the Ural Polytechnical Institute of construction faculty. Studying at the University for Yeltsin was not difficult like studying in school. Through all his academic cursus his grades were whether \"good\" or \"excellent\". At the Institute, Yeltsin continued to play sports as a national team player in volleyball: first at the Ural polytechnical Institute, and then Sverdlovsk. After graduating from the University in 1955, Yeltsin took a job in a construction trust. Not in the position of a master, which was offered to the graduate of the construction faculty, but as a simple plasterer. As Boris Nikolayevich himself will later tell, he considered it necessary to master all construction specialties before starting to manage people on the construction site. A year later, having worked on construction projects in the twelve specialties of the trust, he took the position of the master. In 1961, Yeltsin joined the CPSU, and later became chief engineer of Sverdlovsk house-building combine. From now on, Boris Nikolayevich begins to build not only new buildings in the city, but also its own political career. In fifteen years of membership and work in the party, Yeltsin passed from candidate of the CPSU to Secretary of the Sverdlovsk regional Committee. His rapid career growth reached the highest rank. On June 12, 1991 Boris Yeltsin becomes the first President of Russian Federation. Having won huge people’s sympathy, Yeltsin became the first elected, not appointed President. His hard work and perseverance became an example for many colleagues and friends. They couldn't even imagine, that for a long time they were in the same team with the future President Boris Nikolayevich Yeltsin. President of the biggest and greatest country in the world.",
                        null,
                        1
                ),
                new Entry(
                        3,
                        R.drawable.entry_1_1,
                        "Boris Yeltsin 3",
                        "Butka",
                        "From early childhood Boris was energetic and courageous. In his youth, he changed schools several times, in each new classroom, he was among the leaders in academic performance and social activism. He was always elected class President. Thanks to his high growth and athletic physique, Boris Nikolayevich played for the national Berezniki team in volleyball. In district and city, he was known as an activist and a bright young man. Therefore, could pass the final school exams as an external student a year earlier. The precedent was enormous-many teachers were against such innovations, but still persistent Boris achieved his goal. In 1950 he entered the Ural Polytechnical Institute of construction faculty. Studying at the University for Yeltsin was not difficult like studying in school. Through all his academic cursus his grades were whether \"good\" or \"excellent\". At the Institute, Yeltsin continued to play sports as a national team player in volleyball: first at the Ural polytechnical Institute, and then Sverdlovsk. After graduating from the University in 1955, Yeltsin took a job in a construction trust. Not in the position of a master, which was offered to the graduate of the construction faculty, but as a simple plasterer. As Boris Nikolayevich himself will later tell, he considered it necessary to master all construction specialties before starting to manage people on the construction site. A year later, having worked on construction projects in the twelve specialties of the trust, he took the position of the master. In 1961, Yeltsin joined the CPSU, and later became chief engineer of Sverdlovsk house-building combine. From now on, Boris Nikolayevich begins to build not only new buildings in the city, but also its own political career. In fifteen years of membership and work in the party, Yeltsin passed from candidate of the CPSU to Secretary of the Sverdlovsk regional Committee. His rapid career growth reached the highest rank. On June 12, 1991 Boris Yeltsin becomes the first President of Russian Federation. Having won huge people’s sympathy, Yeltsin became the first elected, not appointed President. His hard work and perseverance became an example for many colleagues and friends. They couldn't even imagine, that for a long time they were in the same team with the future President Boris Nikolayevich Yeltsin. President of the biggest and greatest country in the world.",
                        null,
                        1
                ),
                new Entry(
                        4,
                        R.drawable.entry_1_1,
                        "Boris Yeltsin 4",
                        "Butka",
                        "From early childhood Boris was energetic and courageous. In his youth, he changed schools several times, in each new classroom, he was among the leaders in academic performance and social activism. He was always elected class President. Thanks to his high growth and athletic physique, Boris Nikolayevich played for the national Berezniki team in volleyball. In district and city, he was known as an activist and a bright young man. Therefore, could pass the final school exams as an external student a year earlier. The precedent was enormous-many teachers were against such innovations, but still persistent Boris achieved his goal. In 1950 he entered the Ural Polytechnical Institute of construction faculty. Studying at the University for Yeltsin was not difficult like studying in school. Through all his academic cursus his grades were whether \"good\" or \"excellent\". At the Institute, Yeltsin continued to play sports as a national team player in volleyball: first at the Ural polytechnical Institute, and then Sverdlovsk. After graduating from the University in 1955, Yeltsin took a job in a construction trust. Not in the position of a master, which was offered to the graduate of the construction faculty, but as a simple plasterer. As Boris Nikolayevich himself will later tell, he considered it necessary to master all construction specialties before starting to manage people on the construction site. A year later, having worked on construction projects in the twelve specialties of the trust, he took the position of the master. In 1961, Yeltsin joined the CPSU, and later became chief engineer of Sverdlovsk house-building combine. From now on, Boris Nikolayevich begins to build not only new buildings in the city, but also its own political career. In fifteen years of membership and work in the party, Yeltsin passed from candidate of the CPSU to Secretary of the Sverdlovsk regional Committee. His rapid career growth reached the highest rank. On June 12, 1991 Boris Yeltsin becomes the first President of Russian Federation. Having won huge people’s sympathy, Yeltsin became the first elected, not appointed President. His hard work and perseverance became an example for many colleagues and friends. They couldn't even imagine, that for a long time they were in the same team with the future President Boris Nikolayevich Yeltsin. President of the biggest and greatest country in the world.",
                        null,
                        2
                )
        };
        return Arrays.asList(entries);
    }

    private static List<Entry> getChineseEntries() {
        Entry[] entries = {
                new Entry(
                        1,
                        R.drawable.entry_1_1,
                        "Boris Yeltsin",
                        "Butka",
                        "From early childhood Boris was energetic and courageous. In his youth, he changed schools several times, in each new classroom, he was among the leaders in academic performance and social activism. He was always elected class President. Thanks to his high growth and athletic physique, Boris Nikolayevich played for the national Berezniki team in volleyball. In district and city, he was known as an activist and a bright young man. Therefore, could pass the final school exams as an external student a year earlier. The precedent was enormous-many teachers were against such innovations, but still persistent Boris achieved his goal. In 1950 he entered the Ural Polytechnical Institute of construction faculty. Studying at the University for Yeltsin was not difficult like studying in school. Through all his academic cursus his grades were whether \"good\" or \"excellent\". At the Institute, Yeltsin continued to play sports as a national team player in volleyball: first at the Ural polytechnical Institute, and then Sverdlovsk. After graduating from the University in 1955, Yeltsin took a job in a construction trust. Not in the position of a master, which was offered to the graduate of the construction faculty, but as a simple plasterer. As Boris Nikolayevich himself will later tell, he considered it necessary to master all construction specialties before starting to manage people on the construction site. A year later, having worked on construction projects in the twelve specialties of the trust, he took the position of the master. In 1961, Yeltsin joined the CPSU, and later became chief engineer of Sverdlovsk house-building combine. From now on, Boris Nikolayevich begins to build not only new buildings in the city, but also its own political career. In fifteen years of membership and work in the party, Yeltsin passed from candidate of the CPSU to Secretary of the Sverdlovsk regional Committee. His rapid career growth reached the highest rank. On June 12, 1991 Boris Yeltsin becomes the first President of Russian Federation. Having won huge people’s sympathy, Yeltsin became the first elected, not appointed President. His hard work and perseverance became an example for many colleagues and friends. They couldn't even imagine, that for a long time they were in the same team with the future President Boris Nikolayevich Yeltsin. President of the biggest and greatest country in the world.",
                        new int[]{R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1},
                        1
                ),
                new Entry(
                        2,
                        R.drawable.entry_1_1,
                        "Boris Yeltsin",
                        "Butka",
                        "From early childhood Boris was energetic and courageous. In his youth, he changed schools several times, in each new classroom, he was among the leaders in academic performance and social activism. He was always elected class President. Thanks to his high growth and athletic physique, Boris Nikolayevich played for the national Berezniki team in volleyball. In district and city, he was known as an activist and a bright young man. Therefore, could pass the final school exams as an external student a year earlier. The precedent was enormous-many teachers were against such innovations, but still persistent Boris achieved his goal. In 1950 he entered the Ural Polytechnical Institute of construction faculty. Studying at the University for Yeltsin was not difficult like studying in school. Through all his academic cursus his grades were whether \"good\" or \"excellent\". At the Institute, Yeltsin continued to play sports as a national team player in volleyball: first at the Ural polytechnical Institute, and then Sverdlovsk. After graduating from the University in 1955, Yeltsin took a job in a construction trust. Not in the position of a master, which was offered to the graduate of the construction faculty, but as a simple plasterer. As Boris Nikolayevich himself will later tell, he considered it necessary to master all construction specialties before starting to manage people on the construction site. A year later, having worked on construction projects in the twelve specialties of the trust, he took the position of the master. In 1961, Yeltsin joined the CPSU, and later became chief engineer of Sverdlovsk house-building combine. From now on, Boris Nikolayevich begins to build not only new buildings in the city, but also its own political career. In fifteen years of membership and work in the party, Yeltsin passed from candidate of the CPSU to Secretary of the Sverdlovsk regional Committee. His rapid career growth reached the highest rank. On June 12, 1991 Boris Yeltsin becomes the first President of Russian Federation. Having won huge people’s sympathy, Yeltsin became the first elected, not appointed President. His hard work and perseverance became an example for many colleagues and friends. They couldn't even imagine, that for a long time they were in the same team with the future President Boris Nikolayevich Yeltsin. President of the biggest and greatest country in the world.",
                        new int[]{R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1, R.drawable.entry_1_1},
                        1
                )
        };
        return Arrays.asList(entries);
    }
}
