/**
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   __  __                                                   _   
 *  |  \/  | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ 
 *  | |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '_ ` _ \ / _ \ '_ \| __|
 *  | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ 
 *  |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_| |_| |_|\___|_| |_|\__|
 *                           |___/                               
 * 
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   
 */
package de.depascaldc.management.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SuppressWarnings("rawtypes")
	public static JsonArray toArray(Object... objects) {
        List array = new ArrayList();
        Collections.addAll(array, objects);
        return GSON.toJsonTree(array).getAsJsonArray();
    }

    public static JsonObject toObject(Object object) {
        return GSON.toJsonTree(object).getAsJsonObject();
    }

    @SuppressWarnings("rawtypes")
	public static <E> JsonObject mapToObject(Iterable<E> collection, Function<E, JSONPair> mapper) {
        Map object = new LinkedHashMap();
        for (E e : collection) {
            JSONPair pair = mapper.apply(e);
            if (pair != null) {
                object.put(pair.key, pair.value);
            }
        }
        return GSON.toJsonTree(object).getAsJsonObject();
    }

    @SuppressWarnings("rawtypes")
	public static <E> JsonArray mapToArray(E[] elements, Function<E, Object> mapper) {
        ArrayList array = new ArrayList();
        Collections.addAll(array, elements);
        return mapToArray(array, mapper);
    }

    @SuppressWarnings("rawtypes")
	public static <E> JsonArray mapToArray(Iterable<E> collection, Function<E, Object> mapper) {
        List array = new ArrayList();
        for (E e : collection) {
            Object obj = mapper.apply(e);
            if (obj != null) {
                array.add(obj);
            }
        }
        return GSON.toJsonTree(array).getAsJsonArray();
    }

    public static class JSONPair {
        public final String key;
        public final Object value;

        public JSONPair(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public JSONPair(int key, Object value) {
            this.key = String.valueOf(key);
            this.value = value;
        }
    }
}
