package com.github.liyue2008.rpc.nameservice;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author LiYue
 * Date: 2019/9/20
 */
public class Metadata extends HashMap<String, List<URI>> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Metadata:").append("\n");
        for (Entry<String, List<URI>> entry : entrySet()) {
            sb.append("\t").append("Classname: ")
                    .append(entry.getKey()).append("\n");
            sb.append("\t").append("URIs:").append("\n");
            for (URI uri : entry.getValue()) {
                sb.append("\t\t").append(uri).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
