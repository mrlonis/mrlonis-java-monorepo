/**
 * *************************************************************
 *
 * <p>(c)
 *
 * <p>Classe : ArgumentChecker Heritage : -
 *
 * <p>Description : This class is exactly the same as StringTokenizer, except it provides a method for backing up a
 * position.
 *
 * <p>Date de creation : 17/08/97 Liste des modifications : Date Auteur Modifications
 * -------------------------------------------- 17/08/97 SUN Creation 29/01/00 V. Oberle Used in "Simulation of
 * Processor Architecture" Liste des bugs connus : Numero: 1 Note par: V. Oberle Description:
 *
 * <p>Version du JDK : 1.1
 *
 * <p>*************************************************************
 */

/*
 * KSStringTokenizer.java   1.0 17/08/97
 *
 * Copyright (c) 1995, 1996 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * CopyrightVersion 1.1_beta
 *
 */

package com.mrlonis.project1.procsimu.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * This class is exactly the same as StringTokenizer, except it provides a method for backing up a position. For
 * example,
 *
 * <p>Example usage:
 *
 * <PRE>
 * String s = "this is a test";
 * KSStringTokenizer st = new KSStringTokenizer(s);
 * println(st.nextToken();
 * st.backup();
 * println(st.nextToken();
 * </PRE>
 *
 * Prints the following on the console:
 *
 * <PRE>
 * this
 * this
 * </PRE>
 *
 * <p>
 *
 * <p>This class was written to allow tokens to be shoved back into the tokenizer. It is exactly the same as
 * java.util.StringTokenizer, but See the documentation for java.util.StringTokenizer for more information on how to use
 * this class.
 *
 * @author Kevin Swan
 * @version 1.0, 17 Aug 1997
 */
public class KSStringTokenizer implements Enumeration {
    private int currentPosition;
    private int maxPosition;
    private String str;
    private String delimiters;
    private boolean retTokens;

    /**
     * Constructs a KSStringTokenizer on the specified String, using the specified delimiter set.
     *
     * @param str the input String
     * @param delim the delimiter String
     * @param returnTokens returns delimiters as tokens or skip them
     */
    public KSStringTokenizer(String str, String delim, boolean returnTokens) {
        currentPosition = 0;
        this.str = str;
        maxPosition = str.length();
        delimiters = delim;
        retTokens = returnTokens;
    }

    /**
     * Constructs a KSStringTokenizer on the specified String, using the specified delimiter set.
     *
     * @param str the input String
     * @param delim the delimiter String
     */
    public KSStringTokenizer(String str, String delim) {
        this(str, delim, false);
    }

    /**
     * Constructs a KStringTokenizer on the specified String, using the default delimiter set (which is " \t\n\r").
     *
     * @param str the String
     */
    public KSStringTokenizer(String str) {
        this(str, " \t\n\r", false);
    }

    /** Skips delimiters. */
    private void skipDelimiters() {
        while (!retTokens
                && (currentPosition < maxPosition)
                && (delimiters.indexOf(str.charAt(currentPosition)) >= 0)) {
            currentPosition++;
        }
    }

    /** Returns true if more tokens exist. */
    public boolean hasMoreTokens() {
        skipDelimiters();
        return (currentPosition < maxPosition);
    }

    /** Backs up one token. Has no effect if we're at the beginning. */
    public void backup() {

        /*
         * This is a hack.  If we're at the very end, we need to back
         * up the currentPosition one so we can look at the last
         * character.
         */
        if (currentPosition == maxPosition) currentPosition--;

        if (currentPosition <= 0) return;

        /*
         * First, there are delimiters before our current position.
         * Skip over them.
         * while we're not at the beginning, and the character at
         * the currentPosition is in the delimiter set, back up.
         */
        while ((currentPosition > 0) && (delimiters.indexOf(str.charAt(currentPosition)) >= 0)) {
            currentPosition--;
        }

        /*
         * Now we're at the last character of the previous token.
         * Back up until we hit a delimiter.
         */
        while ((currentPosition > 0) && (delimiters.indexOf(str.charAt(currentPosition)) < 0)) {
            currentPosition--;
        }
    }

    /**
     * Returns the next token of the String.
     *
     * @throws NoSuchElementException If there are no more tokens in the String.
     */
    public String nextToken() {
        skipDelimiters();

        if (currentPosition >= maxPosition) {
            throw new NoSuchElementException();
        }

        int start = currentPosition;
        while ((currentPosition < maxPosition) && (delimiters.indexOf(str.charAt(currentPosition)) < 0)) {
            currentPosition++;
        }
        if (retTokens && (start == currentPosition) && (delimiters.indexOf(str.charAt(currentPosition)) >= 0)) {
            currentPosition++;
        }
        return str.substring(start, currentPosition);
    }

    /**
     * Returns the next token, after switching to the new delimiter set. The new delimiter set remains the default after
     * this call.
     *
     * @param delim the new delimiters
     */
    public String nextToken(String delim) {
        delimiters = delim;
        return nextToken();
    }

    /** Returns true if the Enumeration has more elements. */
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    /**
     * Returns the next element in the Enumeration.
     *
     * @throws NoSuchElementException If there are no more elements in the enumeration.
     */
    public Object nextElement() {
        return nextToken();
    }

    /**
     * Returns the next number of tokens in the String using the current deliminter set. This is the number of times
     * nextToken() can return before it will generate an exception. Use of this routine to count the number of tokens is
     * faster than repeatedly calling nextToken() because the substrings are not constructed and returned for each
     * token.
     */
    public int countTokens() {
        int count = 0;
        int currpos = currentPosition;

        while (currpos < maxPosition) {
            /*
             * This is just skipDelimiters(); but it does not affect
             * currentPosition.
             */
            while (!retTokens && (currpos < maxPosition) && (delimiters.indexOf(str.charAt(currpos)) >= 0)) {
                currpos++;
            }

            if (currpos >= maxPosition) {
                break;
            }

            int start = currpos;
            while ((currpos < maxPosition) && (delimiters.indexOf(str.charAt(currpos)) < 0)) {
                currpos++;
            }
            if (retTokens && (start == currpos) && (delimiters.indexOf(str.charAt(currpos)) >= 0)) {
                currpos++;
            }
            count++;
        }
        return count;
    } // countTokens()
}
