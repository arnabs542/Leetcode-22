import java.util.HashMap;
import java.util.Map;

/**
 * Target string might contain duplicate characters
 * SubString should contain each target char with corresponding frequency in target
 *
 * A template for substring problems: (HashMap + Two Pointers)
 * https://leetcode.com/problems/minimum-window-substring/discuss/26808/Here-is-a-10-line-template-that-can-solve-most-'substring'-problems
 * https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/discuss/49708/Sliding-Window-algorithm-template-to-solve-all-the-Leetcode-substring-search-problem.
 *
 * 1. Use two pointers: start and end to represent a window.
 * 2. Move end to find a valid window.
 * 3. When a valid window is found, move start to find a smaller window.
 *
 * Similar Questions: 3, 76, 159
 *
 */
public class _0076MinimumWindowSubstring {
    /**
     * 1. Two Pointers
     *
     * map value range for each char:
     * required chars: [frequency in t - frequency in s, frequency in t]
     * other chars: [- frequency in s, 0]
     *
     * Time complexity: O(|s| + O|t|)
     * Space complexity: O(1)
     *
     */
    public String minWindow(String s, String t) {
        if (s == null || t == null || t.isEmpty()) {
            // Leetcode doesn't test cases like s="abacasd" t="" -> invalid input
            // but we can handle it anyway
            return "";
        }

        int [] map = new int[128]; // number of requirements for each letter
        for (char c : t.toCharArray()) {
            map[c]++;
        }
        int start = 0;
        int end = 0;
        int minStart = 0;
        int minLen = Integer.MAX_VALUE;
        int counter = t.length(); // number of requirements

        while (end < s.length()) {
            char c1 = s.charAt(end);
            if (map[c1] > 0) {
                // only reduce counter when it's a required char that hasn't appeared enough times
                // map[c1] = 0: non-required char (haven't shown up) OR required char but already appeared enough times
                // map[c1] < 0: non-required char (already shown up) OR required char but appeared *TOO MANY* times
                counter--;
            }
            // Why do we also need to decrease for non-required char?
            // Because
            map[c1]--;

            while (counter == 0) {
                if (minLen > end - start + 1) {
                    minLen = end - start + 1;
                    minStart = start;
                    if (minLen == t.length()) {
                        break;
                    }
                }
                char c2 = s.charAt(start);
                start++;
                map[c2]++;
                if (map[c2] > 0) {
                    counter++;
                }
            }
            end++;
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
    }

    public String minWindow2(String source , String target) {
        // ?????????counter_s???counter_t
        Map<Character, Integer> counter_t = new HashMap<Character, Integer>();
        Map<Character, Integer> counter_s = new HashMap<Character, Integer>();
        for (int i = 0; i < target.length(); i++) {
            int count = counter_t.getOrDefault(target.charAt(i), 0);
            counter_t.put(target.charAt(i), count + 1);
        }

        int left = 0;
        int valid = 0;
        // ????????????????????????????????????????????????
        int start = -1;
        int minLen = Integer.MAX_VALUE;
        for (int right = 0; right < source.length(); right ++){
            // ???????????????, ch ???????????????????????????
            char ch = source.charAt(right);
            if (counter_t.containsKey(ch)) {
                counter_s.put(ch, counter_s.getOrDefault(ch, 0) + 1);
                if (counter_s.get(ch).compareTo(counter_t.get(ch)) == 0) {
                    valid++;
                }
            }
            // ?????????????????????????????????
            while (valid == counter_t.size()) {
                // ????????????????????????
                if (right - left < minLen) {
                    start = left;
                    minLen = right - left;
                    // Early Exit
                    if (minLen == target.length()) {
                        return source.substring(start, start + minLen  + 1);
                    }
                }
                // left_ch ???????????????????????????
                char left_ch = source.charAt(left);
                // ????????????
                left ++;
                // ???????????????????????????????????????
                if (counter_t.containsKey(left_ch)) {
                    if (counter_t.get(left_ch).equals(counter_s.get(left_ch))) {
                        valid--;
                    }
                    counter_s.put(left_ch, counter_s.getOrDefault(left_ch, 0) - 1);
                }
            }
        }

        // ????????????????????????
        return start == -1 ? "" : source.substring(start, start + minLen  + 1);
    }
}
