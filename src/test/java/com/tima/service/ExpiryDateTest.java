package com.tima.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
class ExpiryDateTest {

    @Test
    void testForExpiryDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.setLenient(false);

        boolean isExpired = new Date(System.currentTimeMillis() + 300000).before(new Date());
        if (isExpired) log.info("This entity has already expired");
        else log.info("This entity is not expired");
    }

    @Test
    void test() {
        Pattern pattern = Pattern.compile("price");
        Matcher match = pattern.matcher("Price");

//        if (match.find()) {
//            log.info("regex passes");
//        } else log.info("regex does not pass");

        Integer[] optionList = {1, 5, 3, 6, 2, 5};
        Set<Integer> nums = new LinkedHashSet<>(Arrays.asList(optionList));
        for (Integer num : nums) {
            log.info(String.valueOf(num));
        }
    }

    @Test
    void testForTransactionRef() {
        String transactionRef = "TXN0" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        log.info(transactionRef);
    }
}