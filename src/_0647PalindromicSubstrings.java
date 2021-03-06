/**
 * Similar Q:
 * Longest Palindromic substring
 */
public class _0647PalindromicSubstrings {
    /**
     * 1. DP
     *
     * Fixed endpoint dp[i]
     * Keep exploring starting point
     *
     * Time complexity: O(n ^ 3)
     * Space complexity: O(n)
     */
    public int countSubstrings11(String s) {
        int[] dp = new int[s.length()];
        dp[0] = 1;

        for (int i = 1; i < s.length(); i++) {
            dp[i] = dp[i- 1] + 1;
            for (int j = i - 1; j >= 0; j--) {
                if (isPalindromic(s, j, i)) {
                    dp[i]++;
                }
            }
        }

        return dp[s.length() -  1];
    }

    private boolean isPalindromic(String s, int start, int end) {
        while (start < end) {
            if (s.charAt(start) == s.charAt(end)) {
                start++;
                end--;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * 	  0  1  2  3  4
     * i\j | a  b  b  a  c
     * --------------------
     * 0 a | T  F  F  T  F
     * 1 b | F  T  T  F  F
     * 2 b | F  F  T  F  F
     * 3 a | F  F  F  T  F
     * 4 c | F  F  F  F  T
     *
     * Time complexity: O(n ^ 2)
     * Space complexity: O(n ^ 2)
     *
     */
    public int countSubstrings12(String s) {
        int n = s.length();
        if (n < 2) {
            return n;
        }

        int count = n;
        boolean[][] dp = new boolean[n][n];
        // size 1 substrings are palindromes
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // for size 2 substrings, check first and last char
        for (int i = 0; i + 1 < n; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
                count++;
            }
        }

        // for size = 3+
        for (int len = 2; len < n; len++) {// controls the size of the substring
            for (int i = 0; i + len < n; i++) { // controls the start index
                int j = i + len; // end index
                if ((s.charAt(i) == s.charAt(j)) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    count++;
                }
            }
        }

        return count;
    }



    /**
     * 2. Extend Palindromic from center
     *
     * If we extend palindromic from left or right, every char correspondence keeps changing
     * so we have to check every char for every substring
     *
     * If we utilize the symmetry of palindromic strings, fix the center & extend two endpoints
     * we only need to check the latest start and right.
     * (kind of like memoization)
     *
     * Why we shouldn't use class level variables:
     * 1) as you start adding more complexity to a system, it's hard to keep track who modifies what.
     * 2) extractPalindrome is a stateless instead of stateful method.
     * Global variables are not good for multi-threading
     *
     *
     */
    public int countSubstrings2(String s) {
        int count=0;
        for(int i=0;i<s.length();i++){
            count+=extractPalindrome(s,i,i);//odd length
            count+=extractPalindrome(s,i,i + 1);//even length
        }
        return count;
    }

    /**
     * return the number of palindromic strings generated by extending left and right
     */
    public int extractPalindrome(String s, int left, int right){
        int count = 0;
        while(left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)){
            count++;
            left--;
            right++;
        }
        return count;
    }
}
