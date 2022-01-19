package org.example;

import junit.framework.TestCase;

public class WordInjectorTest extends TestCase {

    public static void main(String[] args) {
        WordInjector.addLettersToGraph(Constants.SOLUTION_LETTER_TAG);
        WordInjector.addLettersToGraph(Constants.HERRING_LETTER_TAG);
    }

}