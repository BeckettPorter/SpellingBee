import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Beckett Porter
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        findSubstrings("", letters);
    }

    // findSubstrings calls itself recursively to fing all possible variations of a word with the given letters
    public void findSubstrings(String letters, String remaining)
    {
        if (!letters.isEmpty())
        {
            words.add(letters);
        }

        if (!remaining.isEmpty())
        {
            for (int i = 0; i < remaining.length(); i++)
            {
                String newWord = letters + remaining.charAt(i);
                String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
                findSubstrings(newWord, newRemaining);
            }
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        divideAndMergeSort(words, 0, words.size() - 1);
    }

    // This method takes an input list, start, and end index. It then calls itself recursively to divide up
    // the starting array into many smaller ones, then calling merge on them to combine them back into the larger
    // sorted array.
    public void divideAndMergeSort(ArrayList<String> inputList, int start, int end)
    {
        if (start < end)
        {
            int middle = (start + end) / 2;

            divideAndMergeSort(inputList, start, middle);
            divideAndMergeSort(inputList, middle + 1, end);

            merge(inputList, start, middle, end);
        }
    }

    // Merge method which takes an input list, a start, middle, and end index in this array
    public void merge(ArrayList<String> list, int start, int middle, int end)
    {
        int size1 = middle - start + 1;
        int size2 = end - middle;

        ArrayList<String> left = new ArrayList<>(size1);
        ArrayList<String> right = new ArrayList<>(size2);

        for (int i = 0;i < size1; i++)
        {
            left.add(i, list.get(start + i));
        }
        for (int j = 0; j < size2; j++)
        {
            right.add(j, list.get(middle + 1 + j));
        }

        int i = 0;
        int j = 0;
        int x = start;
        // Compare values of Strings in the array at indexes i and j
        while (i < size1 && j < size2)
        {
            if (left.get(i).compareTo(right.get(j)) <= 0)
            {
                list.set(x, left.get(i));
                i++;
            }
            else
            {
                list.set(x, right.get(j));
                j++;
            }
            x++;
        }
        // add any remaining elements of the remaining group of strings in the array
        while (i < size1)
        {
            list.set(x, left.get(i));
            i++;
            x++;
        }
        while (j < size2)
        {
            list.set(x, right.get(j));
            j++;
            x++;
        }
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords()
    {
        for (int i = 0; i < words.size(); i++)
        {
            if (!binarySearch(words.get(i)))
            {
                words.remove(i);
                i--;
            }
        }
        for (String w : words)
        {
            System.out.println(w);
        }
    }

    // Binary search method that goes through the whole dictionary array and finds if the targetWord string is in it.
    public boolean binarySearch(String targetWord)
    {
        int start = 0;
        int end = DICTIONARY_SIZE - 1;

        while (start <= end)
        {
            int middle = start + (end - start) / 2;

            if (targetWord.compareTo(DICTIONARY[middle]) < 0)
            {
                end = middle - 1;
            }
            else if (targetWord.compareTo(DICTIONARY[middle]) > 0)
            {
                start = middle + 1;
            }
            else
            {
                return true;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
