package util.string.encode;

public class LongestCommonSubstring {

    public static String getLongestCommonSubstring(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int max = 0;
        int pos = 0;

        int[][] array = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    if (i == 0 || j == 0) {
                        array[i][j] = 1;
                    } else {
                        array[i][j] = array[i - 1][j - 1] + 1;
                    }
                    if (max < array[i][j]) {
                        max = array[i][j];
                        pos = i + 1;
                    }
                }
            }
        }
        if (pos > 0) {
            return s1.substring(pos - max, pos);
        } else {
            return "";
        }
    }
}