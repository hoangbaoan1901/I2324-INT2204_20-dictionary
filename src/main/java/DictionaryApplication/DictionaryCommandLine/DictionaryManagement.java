package DictionaryApplication.DictionaryCommandLine;

import DictionaryApplication.Trie.Trie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

import java.util.*;

public class DictionaryManagement {
    private Trie trie = new Trie();

    public void insertFromFile(Dictionary myDictionary, String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader buffer = new BufferedReader(fileReader);

            String line;
            while ((line = buffer.readLine()) != null) {
                Word words = new Word();
                words.setWord_target(line.trim());

                StringBuilder meaning = new StringBuilder();
                while ((line = buffer.readLine()) != null) {
                    if (!line.startsWith("|")) {
                        meaning.append(line).append("\n");
                    } else {
                        words.setWord_explain(meaning.toString().trim());
                        myDictionary.insertWord(words.getWord_target(), words.getWord_explain());
                        words.setWord_target(line.replace("|", "").trim());
                        meaning = new StringBuilder();
                    }
                }

                words.setWord_explain(meaning.toString().trim());
                myDictionary.insertWord(words.getWord_target(), words.getWord_explain());
            }

            buffer.close();
        } catch (IOException e) {
            System.out.println("An error occurred with the file: " + e);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }

    public void exportToFile(Dictionary myDictionary, String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter buffer = new BufferedWriter(fileWriter);

            for (Word word : myDictionary.Words) {
                buffer.write("|" + word.getWord_target() + "\n" + word.getWord_explain());
                buffer.newLine();
            }

            buffer.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
    }


    public ObservableList<String> dictionaryLookup(Dictionary myDictionary, String key) {
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            List<String> results = trie.autoComplete(key);
            if (results != null) {
                int length = Math.min(results.size(), 15);
                for (int i = 0; i < length; i++) {
                    list.add(results.get(i));
                }

                for (Word word : myDictionary.Words) {
                    if (word.getWord_target().equalsIgnoreCase(key)) {
                        list.add("Meaning in Vietnamese: " + word.getWord_explain());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e);
        }
        return list;
    }

    public HashSet<Word> searchWords(HashSet<Word> myDictionary, Trie trie, String prefix) {
        HashSet<Word> result = new HashSet<>();

        List<String> results = trie.autoComplete(prefix);
        if (results != null) {
            for (String resultWord : results) {
                for (Word word : myDictionary) {
                    if (word.getWord_target().equalsIgnoreCase(resultWord)) {
                        result.add(word);
                        break;
                    }
                }
            }
        }
        return result;
    }

    public int searchWord(HashSet<Word> myDictionary, String keyword) {
        try {
            List<Word> sortedWords = new ArrayList<>(myDictionary);

            Collections.sort(sortedWords, (w1, w2) -> w1.getWord_target().compareTo(w2.getWord_target()));

            int left = 0;
            int right = sortedWords.size() - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                int res = sortedWords.get(mid).getWord_target().compareTo(keyword);

                if (res == 0) {
                    return mid;
                }

                if (res < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
        return -1;
    }


    public void updateWord(Dictionary myDictionary, int index, String meaning, String path) {
        try {
            if (index >= 0 && index < myDictionary.Words.size()) {
                Word wordToUpdate = new ArrayList<>(myDictionary.Words).get(index);
                wordToUpdate.setWord_explain(meaning);
                exportToFile(myDictionary, path);
            } else {
                System.out.println("Invalid index. Word not updated.");
            }
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
    }

    public void deleteWord(Dictionary myDictionary, int index, String path) {
        try {
            if (index >= 0 && index < myDictionary.Words.size()) {
                myDictionary.Words.remove(index);
                Trie trie = new Trie();
                setTrie(myDictionary);
                exportToFile(myDictionary, path);
            } else {
                System.out.println("Invalid index. Word not deleted.");
            }
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
    }

    public void insertWord(Word word, String path) {
        try (FileWriter fileWriter = new FileWriter(path, true);
             BufferedWriter buffer = new BufferedWriter(fileWriter)) {

            buffer.write("|" + word.getWord_target() + "\n" + word.getWord_explain());
            buffer.newLine();

        } catch (IOException e) {
            System.out.println("IOException.");
        } catch (NullPointerException e) {
            System.out.println("Null Exception.");
        }
    }

    public void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }

    public void setTrie(Dictionary myDictionary) {
        try {
            for (Word word : myDictionary.Words) {
                trie.insert(word.getWord_target());
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e);
        }
    }

}
