package com.example.qllichlamviec.util.pojo;

import java.util.HashMap;
import java.util.Map;

public class RegexUtils {
    private static final Map<Character, String> DIACRITIC_MAP = new HashMap<>();

    static {
        DIACRITIC_MAP.put('a', "[aàáạảãâầấậẩẫăằắặẳẵ]");
        DIACRITIC_MAP.put('e', "[eèéẹẻẽêềếệểễ]");
        DIACRITIC_MAP.put('i', "[iìíịỉĩ]");
        DIACRITIC_MAP.put('o', "[oòóọỏõôồốộổỗơờớợởỡ]");
        DIACRITIC_MAP.put('u', "[uùúụủũưừứựửữ]");
        DIACRITIC_MAP.put('y', "[yỳýỵỷỹ]");
        DIACRITIC_MAP.put('d', "[dđ]");
    }

    public static String convertToRegex(String input) {
        StringBuilder regex = new StringBuilder();
        for (char c : input.toCharArray()) {
            String mappedChar = DIACRITIC_MAP.get(Character.toLowerCase(c));
            if (mappedChar != null) {
                regex.append(mappedChar);
            } else {
                regex.append(c);
            }
        }
        return regex.toString();
    }
}
