/**
 *
 */
package gen.rule.string;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import forms.phon.PhoneTransform;
import forms.primitives.segment.Phone;
import util.string.CollectionPrinter;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jwvl
 * @date Jul 11, 2015
 */
public class SimpleStringRewriteRule implements StringRewriteRule {

    private final Collection<String> leftContexts;
    private final Collection<String> rightContexts;
    private final Collection<String> focus;
    private final String target;
    private Pattern pattern;
    private List<PhoneTransform> mappings;

    /**
     * @param leftContexts
     * @param rightContexts
     * @param focus
     * @param target
     */
    public SimpleStringRewriteRule(Collection<String> leftContexts,
                                   Collection<String> rightContexts, Collection<String> focus,
                                   String target) {
        this.leftContexts = leftContexts;
        this.rightContexts = rightContexts;
        this.focus = focus;
        this.target = target;
    }

    public static SimpleStringRewriteRule createSimple(String before,
                                                       String after) {
        Collection<String> leftContexts = Collections.emptyList();
        Collection<String> rightContexts = Collections.emptyList();
        Collection<String> focus = Collections.singletonList(before);
        String target = after;
        SimpleStringRewriteRule result = new SimpleStringRewriteRule(leftContexts, rightContexts, focus,
                target);
        result.createPhoneTransforms(before, after);
        return result;
    }

    public static SimpleStringRewriteRule createSimpleEdge(List<PhoneTransform> input, Side edge, String context) {
        StringBuilder beforeBuilder = new StringBuilder();
        StringBuilder afterBuilder = new StringBuilder();
        // System.out.printf("Creating rule from before:%s after:%s edge:%s context:%s\n",
        // before,after,edge,context);
        for (PhoneTransform transform : input) {
            beforeBuilder.append(transform.left().toString());
            afterBuilder.append(transform.right().toString());
        }
        Collection<String> leftContexts = Lists.newArrayList();
        Collection<String> rightContexts = Lists.newArrayList();
        Collection<String> foci = Lists.newArrayList();
        String before = beforeBuilder.toString();
        String target = afterBuilder.toString();
        if (edge == Side.EITHER || edge == Side.LEFT) {
            foci.add(context + before);
        }
        if (edge == Side.EITHER || edge == Side.RIGHT) {
            foci.add(before + context);
        }
        SimpleStringRewriteRule result = new SimpleStringRewriteRule(leftContexts, rightContexts, foci,
                target);
        result.mappings = input;
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.rule.string.StringRewriteRule#apply(java.lang.StringBuilder)
     */
    @Override
    public void apply(StringBuilder building, Matcher matcher) {
        int startsAt = matcher.start(2);
        int endsAt = matcher.end(2);
        building.replace(startsAt, endsAt, target);
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.rule.string.StringRewriteRule#getPattern()
     */
    @Override
    public Pattern getPattern() {
        if (pattern == null) {
            StringBuilder result = new StringBuilder();
            result.append(getPatternString(leftContexts));
            result.append(getPatternString(focus));
            result.append(getPatternString(rightContexts));
            pattern = Pattern.compile(result.toString());
        }
        return pattern;
    }

    private static String getPatternString(Collection<String> strings) {
        StringBuilder stringResult = new StringBuilder("(");
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String toAppend = iterator.next();
            String quoted = Pattern.quote(toAppend);
            stringResult.append(quoted);
            if (iterator.hasNext())
                stringResult.append("|");
        }
        stringResult.append(')');
        return stringResult.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.rule.string.StringRewriteRule#getMatcher()
     */
    @Override
    public Matcher getMatcher(CharSequence cs) {
        Pattern p = getPattern();
        return p.matcher(cs);
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.rule.string.StringRewriteRule#getSpeStyleString()
     */
    @Override
    public String getSpeStyleString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append(CollectionPrinter.collectionToString(focus, ", "))
                .append("} --> ");
        builder.append(target).append(" / ");

        if (leftContexts.size() > 0) {
            builder.append("{")
                    .append(CollectionPrinter.collectionToString(leftContexts,
                            ", ")).append("} ");
        }

        builder.append("___");
        if (rightContexts.size() > 0) {
            builder.append(" {")
                    .append(CollectionPrinter.collectionToString(rightContexts,
                            ", ")).append("}");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getSpeStyleString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        String patternString = getPattern().toString();
        result = prime * result
                + ((patternString == null) ? 0 : patternString.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SimpleStringRewriteRule))
            return false;
        SimpleStringRewriteRule other = (SimpleStringRewriteRule) obj;
        if (target == null) {
            if (other.target != null)
                return false;
        } else if (!target.equals(other.target))
            return false;
        if (getPattern().toString() == null) {
            if (other.getPattern().toString() != null)
                return false;
        } else if (!getPattern().toString().equals(
                other.getPattern().toString()))
            return false;
        return true;
    }

    /**
     * @return
     */
    private void createPhoneTransforms(String before, String after) {
        mappings = Lists.newArrayList();
        for (int i = 0; i < before.length(); i++) {
            Phone a = Phone.getInstance(before.charAt(i));
            Phone b;
            if (Strings.isNullOrEmpty(after)) {
                b = Phone.NULL;
            } else {
                b = Phone.getInstance(after.charAt(i));
            }
            mappings.add(PhoneTransform.getInstance(a, b));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.rule.string.StringRewriteRule#getMappings()
     */
    @Override
    public List<PhoneTransform> getTransforms() {
        return mappings;
    }

    @Override
    public Collection<String> getLeftContexts() {
        return leftContexts;
    }

    @Override
    public Collection<String> getRightContexts() {
        return rightContexts;
    }

    @Override
    public Collection<String> getFocus() {
        return focus;
    }

    @Override
    public String getTarget() {
        return target;
    }


}
