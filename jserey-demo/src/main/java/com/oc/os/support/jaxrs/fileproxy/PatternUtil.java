package com.oc.os.support.jaxrs.fileproxy;


import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
public class PatternUtil
{

    /**
     * Match against the pattern.
     * <p>
     * If a matched then the capturing group values (if any) will be added to
     * a list passed in as parameter.
     *
     * @param cs          the char sequence to match against the template.
     * @param groupValues the list to add the values of a pattern's capturing
     *                    groups if matching is successful. The values are
     *                    added in the same order as the pattern's capturing
     *                    groups. The list is cleared before values are added.
     * @return {@code true} if the char sequence matches the pattern,
     * otherwise {@code false}.
     * @throws IllegalArgumentException if the group values is {@code null}.
     */
    public final static boolean match(String regex, final CharSequence cs,
                                      final List<String> groupValues) throws
            IllegalArgumentException
    {
        if (groupValues == null)
        {
            throw new IllegalArgumentException();
        }

        final int[] groupIndexes = new int[0];
        // Check for match against the empty pattern
        Pattern regexPattern = Pattern.compile(regex);
        if (cs == null || cs.length() == 0)
        {
            return regexPattern == null;
        }
        else if (regexPattern == null)
        {
            return false;
        }

        // Match the regular expression
        Matcher m = regexPattern.matcher(cs);
        if (!m.matches())
        {
            return false;
        }

        groupValues.clear();
        if (groupIndexes.length > 0)
        {
            for (int i = 0; i < groupIndexes.length; i++)
            {
                groupValues.add(m.group(groupIndexes[i]));
            }
        }
        else
        {
            for (int i = 1; i <= m.groupCount(); i++)
            {
                groupValues.add(m.group(i));
            }
        }

        // TODO check for consistency of different capturing groups
        // that must have the same value

        return true;
    }

    /**
     * Match against the pattern.
     * <p>
     * If a matched then the capturing group values (if any) will be added to
     * a list passed in as parameter.
     *
     * @param cs          the char sequence to match against the template.
     * @param groupNames  the list names associated with a pattern's
     *                    capturing groups. The names MUST be in the same
     *                    order as the
     *                    pattern's capturing groups and the size MUST be
     *                    equal to or less than the number of capturing groups.
     * @param groupValues the map to add the values of a pattern's capturing
     *                    groups if matching is successful. A values is put
     *                    into the map using the group name associated with
     *                    the capturing group. The map is cleared before values
     *                    are added.
     * @return {@code true} if the matches the pattern, otherwise {@code false}.
     * @throws IllegalArgumentException if group values is {@code null}.
     */
    public final static boolean match(String regex, final CharSequence cs,
                                      final List<String> groupNames, final
                                      Map<String, String> groupValues) throws
            IllegalArgumentException
    {
        if (groupValues == null)
        {
            throw new IllegalArgumentException();
        }
        final int[] groupIndexes = new int[0];
        // Check for match against the empty pattern
        Pattern regexPattern = Pattern.compile(regex);
        // Check for match against the empty pattern
        if (cs == null || cs.length() == 0)
        {
            return regexPattern == null;
        }
        else if (regexPattern == null)
        {
            return false;
        }

        // Match the regular expression
        Matcher m = regexPattern.matcher(cs);
        if (!m.matches())
        {
            return false;
        }

        // Assign the matched group values to group names
        groupValues.clear();

        for (int i = 0; i < groupNames.size(); i++)
        {
            String name = groupNames.get(i);
            String currentValue = m.group((groupIndexes.length >
                    0) ? groupIndexes[i] : i + 1);

            // Group names can have the same name occurring more than once,
            // check that groups values are same.
            String previousValue = groupValues.get(name);
            if (previousValue != null && !previousValue.equals(currentValue))
            {
                return false;
            }

            groupValues.put(name, currentValue);
        }

        return true;
    }
}
