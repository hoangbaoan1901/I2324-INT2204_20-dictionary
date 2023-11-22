package DictionaryApplication.Trie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private final Map<Character, Trie> children;
    private String content;
    private boolean terminal = false;

    public Trie() {
        this(null);
    }

    private Trie(String content) {
        this.content = content;
        children = new HashMap<>();
    }

    private void addChild(char character) {
        String newContent = (this.content == null) ? Character.toString(character) : this.content + character;
        children.put(character, new Trie(newContent));
    }

    public void insert(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Null word entries are not valid.");
        }

        Trie node = this;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.addChild(c);
            }
            node = node.children.get(c);
        }
        node.terminal = true;
    }

    public List<String> autoComplete(String prefix) {
        Trie trieNode = this;
        for (char c : prefix.toCharArray()) {
            if (!trieNode.children.containsKey(c)) {
                return null;
            }
            trieNode = trieNode.children.get(c);
        }
        return trieNode.collectAllWordsWithPrefix();
    }

    private List<String> collectAllWordsWithPrefix() {
        List<String> wordResults = new ArrayList<>();
        if (this.terminal) {
            wordResults.add(this.content);
        }
        for (Map.Entry<Character, Trie> entry : children.entrySet()) {
            Trie child = entry.getValue();
            Collection<String> childWords = child.collectAllWordsWithPrefix();
            wordResults.addAll(childWords);
        }
        return wordResults;
    }
}
