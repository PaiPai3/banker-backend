package com.mxs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class BankerAlgorithmApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        Integer[] a = {1, 2, 3, 4, 5};
        Integer[][] b = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        String sa = Arrays.toString(a);
        System.out.println(sa);
        String sb = Arrays.deepToString(b);
        System.out.println(sb);
        System.out.println(Arrays.toString(b));

        //将
        Integer[] newa = Arrays.stream(sa.substring(1, sa.length() - 1).split(",")).map(Integer::parseInt).toArray(Integer[]::new);
        Integer[][] newb = Arrays.stream(sb.substring(2, sb.length() - 2).split("], \\[")).map(s -> Arrays.stream(s.split(", ")).map(Integer::parseInt).toArray(Integer[]::new)).toArray(Integer[][]::new);
    }
}
