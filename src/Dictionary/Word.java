package src.Dictionary;

import java.util.Objects;

public class Word {
    private String word_target;
    private String word_explain;
    private String word_type;

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public Word(String word_target, String word_explain, String word_type) {
        this.word_target = word_target;
        this.word_explain = word_explain;
        this.word_type = word_type;
    }

    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public String getWord_type() {
        return word_type;
    }

    public void setWord_type(String word_type) {
        this.word_type = word_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word word)) return false;
        return Objects.equals(getWord_target(), word.getWord_target()) && Objects.equals(getWord_explain(), word.getWord_explain()) && Objects.equals(getWord_type(), word.getWord_type());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWord_target(), getWord_explain(), getWord_type());
    }
}
