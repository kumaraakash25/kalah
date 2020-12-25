package com.game.kalah.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A function that transforms the input Array into a map with keys representing the position of element in Array
 */
public class ListToMapTransformerFunction implements Function<Integer[], Map> {

    @Override
    public Map apply(Integer[] list) {
        var resultMap = new LinkedHashMap<>();
        for (int i = 0; i < list.length; i++) resultMap.put(i + 1, list[i]);
        return resultMap;
    }
}
