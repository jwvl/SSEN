package simulate.phonetic.vowelsystems.levels;

import forms.Form;
import forms.primitives.Subform;
import grammar.levels.Level;
import grammar.levels.LevelInfo;

public class UnderlyingVowelForm implements Form {
    private final String contents;

    public UnderlyingVowelForm(String contents) {
        this.contents = contents;
    }

    @Override
    public Level getLevel() {
        return VowelSimLevels.getUnderlyingFormLevel();
    }

    @Override
    public int getLevelIndex() {
        return getLevel().myIndex();
    }

    @Override
    public int getNumSubForms() {
        return 1;
    }

    @Override
    public String toBracketedString() {
        LevelInfo levelInfo = getLevel().getInfo();
        StringBuilder sb = new StringBuilder();
        sb.append(levelInfo.getLeftBracket());
        sb.append(contents);
        sb.append(levelInfo.getRightBracket());
        return sb.toString();
    }

    @Override
    public int countSubform(Subform sf) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnderlyingVowelForm that = (UnderlyingVowelForm) o;

        if (getLevelIndex() != that.getLevelIndex()) return false;
        return contents != null ? contents.equals(that.contents) : that.contents == null;
    }

    @Override
    public int hashCode() {
        int result = getLevelIndex();
        result = 31 * result + (contents != null ? contents.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return toBracketedString();
    }

    public String getContent() {
        return contents;
    }
}
