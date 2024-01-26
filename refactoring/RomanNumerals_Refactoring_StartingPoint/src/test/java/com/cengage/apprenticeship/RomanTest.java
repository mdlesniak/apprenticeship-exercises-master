package com.cengage.apprenticeship;

/*
 * Instructions:  Please refactor this until it passes the checkstyle rules.  Pay particular
 *                attention to separation of concerns - is there another class (or two, or
 *                three) hiding in there?
 */

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

class RomanTest {

    @Test
    void testFoo() {
        Roman roman = new Roman();
        assertThat(roman.convert(1)).isEqualTo("I");
        assertThat(roman.convert(2)).isEqualTo("II");
        assertThat(roman.convert(3)).isEqualTo("III");
        assertThat(roman.convert(4)).isEqualTo("IV");
        assertThat(roman.convert(5)).isEqualTo("V");
        assertThat(roman.convert(6)).isEqualTo("VI");
        assertThat(roman.convert(7)).isEqualTo("VII");
        assertThat(roman.convert(8)).isEqualTo("VIII");
        assertThat(roman.convert(9)).isEqualTo("IX");
        assertThat(roman.convert(10)).isEqualTo("X");
        assertThat(roman.convert(99)).isEqualTo("XCIX");
        assertThat(roman.convert(50)).isEqualTo("L");
        assertThat(roman.convert(500)).isEqualTo("D");
        assertThat(roman.convert(1000)).isEqualTo("M");
    }
}
