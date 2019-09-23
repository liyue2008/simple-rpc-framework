/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.liyue2008.rpc.serialize;

import com.github.liyue2008.rpc.Serializer;
import com.github.liyue2008.rpc.nameservice.Metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class SerializeSupport {
    private static Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();
    private static Map<Integer, Class<?>> typeMap = new HashMap<>();
    static {
        registerType(Types.TYPE_STRING, String.class, new StringSerializer());
        registerType(Types.TYPE_METADATA, Metadata.class, new MetadataSerializer());
    }
    private static int parseEntryType(byte[] buffer) {
        return buffer[0];
    }
    private static <E> void registerType(int type, Class<E> eClass, Serializer<E> serializer) {
        serializerMap.put(eClass, serializer);
        typeMap.put(type, eClass);
    }
    public static  <E> E parse(byte [] buffer, Class<E> eClass) {
        return parse(buffer, 0, buffer.length, eClass);
    }

    @SuppressWarnings("unchecked")
    private static  <E> E parse(byte [] buffer, int offset, int length, Class<E> eClass) {
        Object entry =  serializerMap.get(eClass).parse(buffer, offset, length);
        if (eClass.isAssignableFrom(entry.getClass())) {
            return (E) entry;
        } else {
            throw new SerializeException("Type mismatch!");
        }
    }
    public static  <E> E parse(byte [] buffer) {
        return parse(buffer, 0, buffer.length);
    }

    private static  <E> E parse(byte[] buffer, int offset, int length) {
        int type = parseEntryType(buffer);
        @SuppressWarnings("unchecked")
        Class<E> eClass = (Class<E> )typeMap.get(type);
        if(null == eClass) {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        } else {
            return parse(buffer, offset + 1, length - 1,eClass);
        }

    }

    public static <E> byte [] serialize(E  entry) {
        @SuppressWarnings("unchecked")
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if(serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
        }
        byte [] bytes = new byte [serializer.size(entry) + 1];
        bytes[0] = serializer.type();
        serializer.serialize(entry, bytes, 1, bytes.length - 1);
        return bytes;
    }
}
