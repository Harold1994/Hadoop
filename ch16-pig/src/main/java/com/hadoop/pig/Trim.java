package com.hadoop.pig;

import org.apache.pig.PrimitiveEvalFunc;

import javax.print.DocFlavor;
import java.io.IOException;

public class Trim extends PrimitiveEvalFunc<String, String> {
    @Override
    public String exec(String o) throws IOException {
        return o.toString();
    }
}
