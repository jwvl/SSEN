/**
 *
 */
package grammar.levels;

/**
 * @author jwvl
 * @date Nov 18, 2014
 */
public class LevelInfo {
    private String abbreviation;
    private String fullName;
    private String leftBracket;
    private String rightBracket;
    public static LevelInfo nullLevelInfo = createNullLevel();
    public static LevelInfo sinkLevelInfo = createSinkLevel();
    public static LevelInfo sourceLevelInfo = createSourceLevel();

    public static LevelInfo createInstance(String fullName,
                                           String abbreviation, String leftBracket, String rightBracket) {
        return new LevelInfo(fullName, abbreviation, leftBracket, rightBracket);
    }

    public static LevelInfo createMorphologicalForm() {
        return new LevelInfo("Morphological Form", "MF", "<", ">");
    }

    public static LevelInfo createMStructureForm() {
        return new LevelInfo("MStructure Form", "MsF", "«", "»");
    }

    public static LevelInfo createMFormForm() {
        return new LevelInfo("MForm Form", "MF", "⟨", "⟩");
    }

    public static LevelInfo createNameless() {
        return new LevelInfo("Nameless Form", "F", "$", "$");
    }

    public static LevelInfo createNullLevel() {
        return new LevelInfo("Unleveled Form", "ulF", "⌜", "⌝");
    }

    /**
     * Creates 'anonymous' level with a number
     *
     * @param i Number to give the level
     * @return Level with name and brackets containing this number
     */
    public static LevelInfo createNumbered(int i) {
        return new LevelInfo(("Level " + i), ("L" + i), (i + "$"), ("$"));
    }

    public static LevelInfo createPhoneticForm() {
        return new LevelInfo("Phonetic Form", "PF", "[", "]");
    }

    public static LevelInfo createSemanticForm() {
        return new LevelInfo("Semantic Form", "SemF", "“", "”");
    }

    public static LevelInfo createSurfaceForm() {
        return new LevelInfo("Surface Form", "SF", "/", "/");
    }

    public static LevelInfo createUnderlyingForm() {
        return new LevelInfo("Underlying Form", "UF", "|", "|");
    }

    /**
     * @return
     */
    private static LevelInfo createSinkLevel() {
        return new LevelInfo("Sink Form", "SinkF", "", "");
    }

    /**
     * @return
     */
    private static LevelInfo createSourceLevel() {
        return new LevelInfo("Source Form", "SourceF", "", "");
    }

    /**
     * @param fullName
     * @param abbreviation
     * @param leftBracket
     * @param rightBracket
     */
    private LevelInfo(String fullName, String abbreviation, String leftBracket,
                      String rightBracket) {
        this.fullName = fullName;
        this.abbreviation = abbreviation;
        this.leftBracket = leftBracket;
        this.rightBracket = rightBracket;
    }

    public String bracket(String orig) {
        return (leftBracket + orig + rightBracket);
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLeftBracket() {
        return leftBracket;
    }

    public String getRightBracket() {
        return rightBracket;
    }

    @Override
    public String toString() {
        return String.format("Level: %s %s%s%s", fullName, leftBracket,
                abbreviation, rightBracket);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((abbreviation == null) ? 0 : abbreviation.hashCode());
        result = prime * result
                + ((fullName == null) ? 0 : fullName.hashCode());

        result = prime * result
                + ((leftBracket == null) ? 0 : leftBracket.hashCode());
        result = prime * result
                + ((rightBracket == null) ? 0 : rightBracket.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof LevelInfo))
            return false;
        LevelInfo other = (LevelInfo) obj;
        if (abbreviation == null) {
            if (other.abbreviation != null)
                return false;
        } else if (!abbreviation.equals(other.abbreviation))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (leftBracket == null) {
            if (other.leftBracket != null)
                return false;
        } else if (!leftBracket.equals(other.leftBracket))
            return false;
        if (rightBracket == null) {
            if (other.rightBracket != null)
                return false;
        } else if (!rightBracket.equals(other.rightBracket))
            return false;
        return true;
    }

}
