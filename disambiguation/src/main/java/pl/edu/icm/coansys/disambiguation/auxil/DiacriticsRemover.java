/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.disambiguation.auxil;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Mapping to the basic Latin alphabet (a-z, A-Z). In most cases, a character is
 * mapped to the closest visual form, rather than functional one, e.g.: "ö" is
 * mapped to "o" rather than "oe", and "đ" is mapped to "d" rather than "dj" or
 * "gj". Notable exceptions include: "ĸ" mapped to "q", "ß" mapped to "ss", and
 * "Þ", "þ" mapped to "Y", "y".
 *
 * <p> Each character is processed as follows: <ol> <li>the character is
 * compatibility decomposed,</li> <li>all the combining marks are removed,</li>
 * <li>the character is compatibility composed,</li> <li>additional "manual"
 * substitutions are applied.</li> </ol> </p>
 *
 * <p> All the characters from the "Latin-1 Supplement" and "Latin Extended-A"
 * Unicode blocks are mapped to the "Basic Latin" block. Characters from other
 * alphabets are generally left intact, although the decomposable ones may be
 * affected by the procedure. </p>
 *
 * @author Lukasz Bolikowski (bolo@icm.edu.pl)
 *
 */
public class DiacriticsRemover {

    private DiacriticsRemover() {
    }
    private static final Character[] from = {
        'Æ', 'Ð', 'Ø', 'Þ', 'ß', 'æ', 'ð', 'ø', 'þ', 'Đ', 'đ', 'Ħ',
        'ħ', 'ı', 'ĸ', 'Ł', 'ł', 'Ŋ', 'ŋ', 'Œ', 'œ', 'Ŧ', 'ŧ'};
    private static final String[] to = {
        "AE", "D", "O", "Y", "ss", "ae", "d", "o", "y", "D", "d", "H",
        "h", "i", "q", "L", "l", "N", "n", "OE", "oe", "T", "t"};
    private static final char[] INTERESTING_CHARACTERS = {'%', '_'};
    private static final int[] INTERESTING_TYPES = new int[]{
        Character.LOWERCASE_LETTER,
        Character.DECIMAL_DIGIT_NUMBER,
        Character.NON_SPACING_MARK,
        Character.SPACE_SEPARATOR
    };
    private static final String MAGIC = "IeG2Ut!3";
    private static Map<Character, String> lookup = buildLookup();
    private static Map<Character, String> alphaSortableMapping = alphaSortableMapping();

    private static Map<Character, String> buildLookup() {
        if (from.length != to.length) {
            throw new IllegalStateException();
        }

        Map<Character, String> _lookup = new HashMap<Character, String>();
        for (int i = 0; i < from.length; i++) {
            _lookup.put(from[i], to[i]);
        }

        return _lookup;
    }

    private static Map<Character, String> alphaSortableMapping() {
        Map<Character, String> result = new HashMap<Character, String>();

        // ł is not handled by normalization to NFKD form and so
        // we use artificial mapping:
        // ł -> l + combining long stroke overlay
        result.put('ł', "l\u0336");

        return result;
    }

    /**
     * Removes diacritics from a text.
     *
     * @param text Text to process.
     * @return Text without diacritics.
     */
    public static String removeDiacritics(String text) {
        if (text == null) {
            return null;
        }

        text = Normalizer.normalize(text, Normalizer.Form.NFKD);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            Character ch = text.charAt(i);
            if (Character.getType(ch) == Character.NON_SPACING_MARK) {
                continue;
            }

            if (lookup.containsKey(ch)) {
                builder.append(lookup.get(ch));
            } else {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    /**
     * Backwards-compatible removal of diacritics with optional escaping.
     *
     * @deprecated Use {@link #removeDiacritics(String)} for actual diacritics
     * removal or {@link #alphaSortable(String, boolean)} for sort key
     * generation.
     *
     * @param text Text to process.
     * @param escapeNonSpacingMarks If
     * <code>true</code> then the result of
     * <code>alphaSortable(text, false)</code> is returned, otherwise the result
     * of
     * <code>removeDiacritics(text)</code> is returned.
     * @return
     */
    @Deprecated
    public static String removeDiacritics(String text, boolean escapeNonSpacingMarks) {
        if (escapeNonSpacingMarks) {
            return alphaSortable(text, false);
        } else {
            return removeDiacritics(text);
        }
    }

    /**
     * Generates a sort key for a given text. This key is useful in environments
     * where only basic Latin characters are reliably sorted (for example, a
     * RDBMS with unknown collation settings).
     *
     * @param text Text to process.
     * @param idempotent Whether the conversion should be idempotent. This is
     * guaranteed to be true:
     * <code>alphaSortable(s, true).equals(alphaSortable(alphaSortable(s, true), true)</code>,
     * while this is not necessarily true:
     * <code>alphaSortable(s, false).equals(alphaSortable(alphaSortable(s, false), false)</code>.
     * @return
     */
    public static String alphaSortable(String text, boolean idempotent) {
        if (text == null) {
            return null;
        }

        if (idempotent && text.startsWith(MAGIC)) {
            return text;
        }

        text = text.toLowerCase(Locale.ENGLISH);
        text = Normalizer.normalize(text, Normalizer.Form.NFKD);

        StringBuilder builder = new StringBuilder();
        if (idempotent) {
            builder.append(MAGIC);
        }

        boolean wasSpaceSeparator = false;
        for (int i = 0; i < text.length(); i++) {
            Character ch = text.charAt(i);
            if (!ArrayUtils.contains(INTERESTING_TYPES, Character.getType(ch))
                    && !ArrayUtils.contains(INTERESTING_CHARACTERS, ch)) {
                continue;
            }

            String s;

            // TODO quick fix of mantis 3231
            if (isSpaceSeparator(ch)) {
                if (wasSpaceSeparator) {
                    continue;
                }
                wasSpaceSeparator = true;
            } else {
                wasSpaceSeparator = false;
            }

            if (alphaSortableMapping.containsKey(ch)) {
                s = alphaSortableMapping.get(ch);
            } else if (lookup.containsKey(ch)) {
                s = lookup.get(ch);
            } else {
                s = ch.toString();
            }

            for (int j = 0; j < s.length(); j++) {
                Character c = s.charAt(j);
                // TODO Very ugly workaround of the problem described in 0002643
                if (ArrayUtils.contains(INTERESTING_CHARACTERS, c)) {
                    builder.append(c);
                } else {
                    builder.append(StringUtils.leftPad(Integer.toHexString(c.charValue()), 4, '0'));
                }
            }
        }

        return builder.toString();
    }

    private static boolean isSpaceSeparator(char ch) {
        return Character.SPACE_SEPARATOR == Character.getType(ch);
    }
}
