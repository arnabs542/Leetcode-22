public class _0012IntegerToRoman {
    /**
     * 1. One Pass
     *
     * Time complexity: O(number of characters)
     * Space complexity: O(1)
     *
     * @param num
     * @return
     */
    public String intToRoman(int num) {
        if (num < 1 || num > 3999) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        String[] roman = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };

        int i = 0;
        // iterate until the number becomes zero, NO NEED to go until the last element in roman array
        while (num >0) {
            while ( num >= values[i]) {
                num -= values[i];
                result.append(roman[i]);
            }
            i++;
        }
        return result.toString();
    }
}
