package com.bibleloops.vis;

import java.util.Hashtable;

public class BookMap {
    private static Hashtable<String, String> map = new Hashtable<String, String>();

    static {
        map = new Hashtable<String, String>();
        String[][] pairs = {
            {"Ge", "Genesis"}, {"Ex", "Exodus"}, {"Le", "Leviticus"}, {"Nu", "Numbers"}, {"De", "Deuteronomy"}, {"Jos", "Joshua"}, {"Jg", "Judges"}, 
            {"Ru", "Ruth"}, {"1Sa", "1 Samuel"}, {"2Sa", "2 Samuel"}, {"1Ki", "1 Kings"}, {"2Ki", "2 Kings"}, {"1Ch", "1 Chronicles"}, {"2Ch", "2 Chronicles"}, 
            {"Ezr", "Ezra"}, {"Ne", "Nehemiah"}, {"Es", "Esther"}, {"Job", "Book of Job"}, {"Ps", "Psalms"}, {"Pr", "Proverbs"}, {"Ec", "Ecclesiastes"}, 
            {"So", "Song of Songs"}, {"Isa", "Isaiah"}, {"Jer", "Jeremiah"}, {"La", "Lamentations"}, {"Eze", "Ezekiel"}, {"Da", "Daniel"}, 
            {"Ho", "Hosea"}, {"Joe", "Joel"}, {"Am", "Amos"}, {"Ob", "Obadiah"}, {"Jon", "Jonah"}, {"Mic", "Micah"}, {"Na", "Nahum"},
            {"Hab", "Habakkuk"}, {"Zep", "Zephaniah"}, {"Hag", "Haggai"}, {"Zec", "Zechariah"}, {"Mal", "Malachai"}, {"Mt", "Matthew"}, 
            {"Mr", "Mark"}, {"Lu", "Luke"}, {"Joh", "John"}, {"Ac", "Acts"}, {"Ro", "Romans"}, {"1Co", "1 Corinthians"},
            {"2Co", "2 Corinthians"}, {"Ga", "Galatians"}, {"Eph", "Ephesians"}, {"Php", "Philippians"}, {"Col", "Colossians"}, {"1Th", "1 Thessalonians"}, 
            {"2Th", "2 Thessalonians"}, {"1Ti", "1 Timothy"}, {"2Ti", "2 Timothy"}, {"Tit", "Titus"}, {"Phm", "Philemon"}, {"Heb", "Hebrews"},
            {"Jas", "James"}, {"1Pe", "1 Peter"}, {"2Pe", "2 Peter"}, {"1Jo", "1 John"}, {"2Jo", "2 John"}, {"3Jo", "3 John"}, {"Jude", "Jude"}, {"Re", "Revelation"}
            
            
        };
        
        for (String[] pair : pairs) {
            map.put(pair[0], pair[1]);
        }
    }

    public static String getBook(String bookShort) {
        return map.get(bookShort);
    }
}
