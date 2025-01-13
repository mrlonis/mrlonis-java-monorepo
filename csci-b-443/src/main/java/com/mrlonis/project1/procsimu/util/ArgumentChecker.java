/**
 * *************************************************************
 *
 * <p>(c)
 *
 * <p>Classe : ArgumentChecker Heritage : -
 *
 * <p>Description : Methodes pour manipuler des arguments de ligne de commandes
 *
 * <p>Date de creation : 02/12/97 Liste des modifications : Date Auteur Modifications
 * -------------------------------------------- 02/12/97 Kevin Swan Creation 29/01/00 V. Oberle Used in "Simulation of
 * Processor Architecture" Liste des bugs connus : Numero: 1 Note par: V. Oberle Description:
 *
 * <p>Version du JDK : 1.1
 *
 * <p>*************************************************************
 */

/*
 * ArgumentChecker.java  1.9 02/12/97
 *
 * Copyright (c) 1997 Kevin Swan.  All rights reserved.
 *
 * I reserve all rights to this software.  You may use it and
 * distribute it freely, provided you do not remove this header
 * and attribute partial credit to the author, Kevin Swan.
 *
 * NOTE :
 * Anciennement ArgChecker. J'ai change le nom en plus explicite.
 * Vincent Oberle
 */

package com.mrlonis.project1.procsimu.util;

import java.util.Vector;

/**
 * This class provides several static methods for checking, manipulating, and accessing command line arguments. It
 * allows you to check if a valid argument set was given to your program with one simple call to a static method. It
 * also provides a single method to check for the presence of a switch, or for retrieving the value associated with a
 * switch.
 *
 * <p>
 *
 * <p>The main functionality of this class is based around the concept of a description String. A description String is
 * a simple, custom regular expression which describes a legal command line for your program. A switch is indicated with
 * a '-'. A value is any String that does not start with a '-', '[', ']', or '|', or the usual reserved characters for
 * your shell or command line interpreter.
 *
 * <p>
 *
 * <p>Switches and arguments which are optional are enclosed in square brackets. When a choice exists, the '|' character
 * indicates an <CODE>OR</CODE> situation. An example of a description String would be:<br>
 * <CODE>
 * &quot;-version | -help | -s server [ -p port | -i interval ]&quot;
 * </CODE>
 *
 * <p>
 *
 * <p>The above description String means the following. You could invoke the program and give it ONLY the argument
 * &quot;-version&quot;. Or, you could invoke it and give it ONLY the argument &quot;-help&quot;. Or, you could invoke
 * it and give it a &quot;-s&quot;, then a server name. If you do this, you can include other parameters after the
 * &quot;-s server&quot; part, if you want. You could give it a &quot;-p&quot; and some string representing a port
 * number, or you could give it a &quot;-i&quot; and some String representing an interval. You could NOT give it
 * anything after the &quot;-version&quot; String, or the &quot;-help&quot; String, if you used either of those. Note
 * that all tokens in the description MUST be seperated by spaces.
 *
 * <p>
 *
 * <p>You use it in your program as follows:
 *
 * <p>
 *
 * <p>
 *
 * <PRE>
 * public static void main (String[] args) {
 * ...
 * String usage = " -version | -help | server [ port ]";
 * if (!ArgumentChecker.isValid(usage, args)) {
 * System.err.println("usage: ProgramName " + usage);
 * return;
 * }
 * // if we get here, then the arguments are legal.
 * </PRE>
 *
 * <p>
 *
 * <p>This version has undergone a complete rewrite. If you find any bugs, please let me know at
 * 013639s@dragon.acadiau.ca.
 *
 * @author Kevin Swan
 * @version 1.9, 02 Dec 1997
 */
public class ArgumentChecker {

    /** The current version of this class. */
    public static final String VERSION = "1.9";

    /** Don't let anyone instantiate this class. Its only used for the static methods. */
    private ArgumentChecker() {}

    /**
     * This method checks if the given arguments legally satisfy the given description. The description in this version
     * is very simple. It is merely a String describing legal command line argument sets in a generic format. If a
     * parameter in the description starts with a '-', then the entire item must be matched exactly in the arguments. If
     * a parameter in the description starts with a '[', then it is an optional argument. An example would be to pass
     * the array {"-s" "schedule.dat"} and the description "[ -s datafile ] [ -d dayOfWeek ]". This should return true.
     *
     * <p>
     *
     * <p>Nesting is supported in versions 1.4 and above. This means that a description String such as "[ -s datafile [
     * -o outputfile ] ] -d day" would be perfectly legal. The level of nesting is restricted only by Java or the amount
     * of memory in your machine.
     *
     * @param args the command line arguments.
     * @param description a simple regular expression describing legal arguments.
     * @return true of the given arguments satisfy the description, false otherwise.
     */
    public static boolean isValid(String description, String[] args) {
        return ArgumentChecker.isValid(description, args, false);
    }

    private static boolean isValid(String description, String[] args, boolean permitExtraArgs) {
        /*
         * Convert the String[] args into a String and call the private method
         *   private static boolean isValid (KSStringTokenizer, String, boolean);
         * with a KSStringTokenizer on the args.
         * We're using a KSStringTokenizer because it lets us backup a token.
         */
        String argString = "";
        int numArgs = args.length;

        for (String arg : args) argString += arg + " ";

        return ArgumentChecker.isValid(new KSStringTokenizer(argString), description, permitExtraArgs);
    }

    /** This is the method that does all the work. */
    private static boolean isValid(KSStringTokenizer argTokenizer, String description, boolean permitExtraArgs) {

        /*
         * If the description contains at least one OR operator,
         * we need to seperate them out and decide which one we want to use.
         */
        if (ArgumentChecker.numSections(description, '|') > 1) {

            /*
             * Seperate the description into its sub-blocks.
             */
            Vector subBlocks = ArgumentChecker.seperateBlocks(description, '|');

            /*
             * If there are no more tokens in the argTokenizer, then check if
             * there are at least one sub-description with 0 mandatory blocks.
             * If there is, return true.  If there is not, return
             * permitExtraArgs.
             */
            if (!argTokenizer.hasMoreTokens()) {
                for (int i = 0; i < subBlocks.size(); i++)
                    if (minNumber((String) subBlocks.elementAt(i)) == 0) return true;
                return permitExtraArgs;
            } // if - argTokenizer has more tokens?

            String tok = argTokenizer.nextToken();

            /*
             * Check the first sets of all of the optional blocks.  Count how
             * many of these first sets contain the arg token currently being
             * evaluated.
             */
            int count = 0;

            for (int i = 0; i < subBlocks.size(); i++) {
                Vector firsts = ArgumentChecker.firstTokens((String) subBlocks.elementAt(i));
                for (int j = 0; j < firsts.size(); j++) if (match((String) firsts.elementAt(j), tok)) count++;
            }

            /*
             * If none of the sub-descriptions contain the current token, then
             * we're not going to match any of these blocks.  Shove this token
             * back on the argTokenizer and return permitExtraArgs.
             */
            if (count == 0) {
                argTokenizer.backup();
                return permitExtraArgs;
            }

            /*
             * If count is more than one, then that means we could match in
             * more than one of these optional blocks.  That shouldn't be
             * possible.  Treat this condition as an ambiguous description
             * and return false.
             */
            if (count > 1) return false;

            /*
             * If count is exactly one (it has to be if we got here), then
             * make a recursive call with the sub-description whose first set
             * contains the current token.  Use the same value of permitExtraArgs
             * in the recursive call.
             */

            String subDescription = "";

            outer:
            for (int i = 0; i < subBlocks.size(); i++) {
                Vector firsts = ArgumentChecker.firstTokens((String) subBlocks.elementAt(i));
                for (int j = 0; j < firsts.size(); j++)
                    if (match((String) firsts.elementAt(j), tok)) {
                        argTokenizer.backup();
                        subDescription = (String) subBlocks.elementAt(i);
                        break outer;
                    }
            }

            return ArgumentChecker.isValid(argTokenizer, subDescription, permitExtraArgs);
        }

        /*
         * Check that the number of arguments is legal.
         */
        int min = ArgumentChecker.minNumber(description);
        int max = ArgumentChecker.maxNumber(description);

        /*
         * We can never have less than the minimum.
         */
        if (argTokenizer.countTokens() < min) return false;

        /*
         * But if we're permitting extra args, we may have more than max.
         */
        if (argTokenizer.countTokens() > max) if (!permitExtraArgs) return false;

        KSStringTokenizer desTokenizer = new KSStringTokenizer(description);
        String currArgToken;
        String currDescriptionToken;
        boolean optionalBlock = false;

        while (true) {

            /*
             * If we're out of args, check if there are any more mandatory
             * parameters in the description.  If there are, return false.
             * If there aren't, return true.
             */
            if (!argTokenizer.hasMoreTokens()) {

                while (desTokenizer.hasMoreTokens()) {
                    String tok = desTokenizer.nextToken();
                    if ("]".equals(tok)) optionalBlock = false;
                    else if ("[".equals(tok)) optionalBlock = true;
                    else if (!optionalBlock) return false;
                }

                /*
                 * Something is wrong with the description.  We should not run
                 * out of tokens if we're in an optional block.  This means there
                 * are more '[' characters than ']' characters.
                 */
                return !optionalBlock;

                /*
                 * If we get here, then there were no more mandatory params in the
                 * description.  We can return true.
                 */
            }

            currArgToken = argTokenizer.nextToken();

            while (true) {

                /*
                 * Inside this while loop, we're evaluating ONE arg.  The whole
                 * purpose of this while loop is to match the arg "currArgToken."
                 * What we do here is advance the description desTokenizer, and see
                 * what we do with the token we get.  Each iteration of this inner
                 * while loop gets one more token from the description desTokenizer,
                 * but doesn't advance argTokenizer at all, unless a recursive
                 * call has to be made inside an optional block.
                 */

                /*
                 * If the description string is out of tokens, then if we're
                 * permitting extra command line params, return true.  If we're
                 * not permitting extra params, return false.
                 *
                 * Why would we permit extra params?  Because we may be nested
                 * inside an optional block. This is why we have the
                 * permitExtraArgs argument.
                 */
                if (!desTokenizer.hasMoreTokens()) {
                    argTokenizer.backup();
                    return permitExtraArgs;
                }

                /* Get the next description token. */
                String tok = desTokenizer.nextToken();

                /*
                 * First, try and match it.  If they match, break out of this loop.
                 * This loop just matches ONE arg token, so we want to get out of
                 * this loop and move to the next arg token in the outer loop.
                 *
                 * If they don't match, then don't break.  It may be an optional
                 * block seperator or something, we'll deal with that in a minute.
                 */
                if (ArgumentChecker.match(currArgToken, tok)) break;
                else
                /*
                 * If its not an optional block seperator, or an OR operator,
                 * then we should have made that match.  Return false.
                 */
                if ((!"[".equals(tok)) && (!"]".equals(tok)) && (!"|".equals(tok))) return false;

                /*
                 * So they didn't match.  If the tok is a "[", then we want
                 * to check the very next token.
                 *
                 * NEW: I've changed this.  Now, if we hit an optional block
                 * openener, we don't check the next token.  This would be
                 * too difficult, since we could have multiply-nested optional
                 * blocks all at the beginning.  So, if we hit the beginning of
                 * an optional block, we just get the entire optional block
                 * description, and make the recursive call.
                 */
                if ("[".equals(tok)) {

                    /*
                     * Now, we'll call this exact same method, except
                     * with the arg tokenizer made up of the rest of our args,
                     * and JUST the part of the description until we hit a "]".
                     * If the token we are examining is in the first set of
                     * all possible args in this sub-description, then we'll
                     * make a recursive call to this method with the sub-
                     * description.  If it is not, then we can skip to the end of
                     * this optional block and ignore it.
                     */

                    /*
                     * First, generate a String containing what's inside the
                     * '[', ']' pair in the description.  This will be stored
                     * in optionalDescription and will not include the
                     * terminating ']'.  Use the original desTokenizer because
                     * it still has that token we just matched at the front
                     * of it.  This string will include neither the opening
                     * '[', nor the closing ']'.
                     */
                    String optionalDescription = "";
                    /* For nested optional blocks. */
                    int depth = 1;
                    while (true) {
                        if (!desTokenizer.hasMoreTokens()) return false;
                        String tmpTok = desTokenizer.nextToken();
                        if ("[".equals(tmpTok)) depth++;
                        if ("]".equals(tmpTok)) {
                            depth--;
                            if (depth == 0) break;
                        }
                        optionalDescription += tmpTok + " ";
                    } // while

                    /*
                     * If any of the firstTokens match() with the currArgToken, then
                     * we want to enter this "if" block.
                     */
                    Vector firsts = ArgumentChecker.firstTokens(optionalDescription);
                    boolean matchMade = false;

                    for (int i = 0; i < firsts.size(); i++)
                        if (ArgumentChecker.match((String) firsts.elementAt(i), currArgToken))
                            if (matchMade)
                                /*
                                 * Cannot match more than one optional token, this would
                                 * be an ambiguous description.
                                 */
                                return false;
                            else matchMade = true;

                    if (matchMade) {
                        /*
                         * The current token exists in the set of first tokens for
                         * this optional block.  Put the current argument token
                         * back on the tokenizer and make a recursive call with this
                         * optional block as the description.
                         */
                        argTokenizer.backup();

                        /*
                         * Since we know we have to match this optional block, if
                         * it returns false we consider the whole check to fail.
                         */
                        if (!ArgumentChecker.isValid(argTokenizer, optionalDescription, true)) return false;

                        /*
                         * OK, that optional block was checked OK.  Break out of this
                         * loop and check the next arg.
                         */
                        break;
                    } else {
                        /*
                         * If the current argument token isn't anywhere in this
                         * optional block, then continue with the next one.
                         */
                        continue;
                    } // if - currArgToken is in first set of optional block?
                } // if - token was a "["?
            } // while - matching ONE token.  This is the inner while.  Each
            //         iteration of this while was one description token.
        } // while - This is the outer while.  Each iteration of this while
        //         was one arg.
    } // isValid()

    /**
     * Returns the minimum number of allowed arguments based on the given description.
     *
     * @param description the String describing the allowable argument string as described above in the comment for
     *     isValid().
     * @return the minimum number of arguments which may be supplied based on the given description. Returns -1 if an
     *     error occurs.
     */
    public static int minNumber(String description) {
        KSStringTokenizer desTokenizer = new KSStringTokenizer(description);
        String token;
        int count = 0;

        /*
         * If the description contains a top-level OR delimiter, we
         * must split the description up into its components and return
         * the minimum of their minimums.
         */
        if (numSections(description, '|') < 1)
            /* An error occured. */
            return -1;
        else if (numSections(description, '|') > 1) {
            Vector subBlocks = ArgumentChecker.seperateBlocks(description, '|');
            int smallest = minNumber((String) subBlocks.elementAt(0));
            for (int i = 1; i < subBlocks.size(); i++)
                if (minNumber((String) subBlocks.elementAt(i)) < smallest)
                    smallest = minNumber((String) subBlocks.elementAt(i));
            return smallest;
        }

        /*
         * If we get here, there shouldn't be any '|' characters in the
         * description.  If there were, then it would have been picked up
         * in the above if statement.  Here, we will count only mandatory
         * arguments; that is, we will ignore everything but top-level
         * tokens.
         */
        int depth = 0;
        while (desTokenizer.hasMoreTokens()) {
            token = desTokenizer.nextToken();
            if ("[".equals(token)) {
                depth++;
                continue;
            }
            if ("]".equals(token)) {
                depth--;
                continue;
            }
            if (depth == 0) count++;
        } // while

        if (depth != 0)
            /* Somehow, there is an unbalanced '[' and numSections didn't
             * report it.
             */
            return -1;
        else return count;
    }

    /**
     * Returns the maximum number of allowed arguments based on the given description.
     *
     * @param description the String describing the allowable argument string as described above in the comment for
     *     isValid().
     * @return the maximum number of arguments which may be supplied based on the given description. Returns -1 if an
     *     error occurs.
     */
    public static int maxNumber(String description) {
        KSStringTokenizer desTokenizer = new KSStringTokenizer(description);
        String token;
        int count = 0;

        /*
         * If the description has top-level OR tokens, we must return
         * the max of all of the different sections.
         */
        if (numSections(description, '|') < 1)
            /* An error occured. */
            return -1;
        else if (numSections(description, '|') > 1) {
            Vector subBlocks = ArgumentChecker.seperateBlocks(description, '|');
            int biggest = maxNumber((String) subBlocks.elementAt(0));
            for (int i = 1; i < subBlocks.size(); i++)
                if (maxNumber((String) subBlocks.elementAt(i)) > biggest)
                    biggest = maxNumber((String) subBlocks.elementAt(i));
            return biggest;
        }

        /*
         * If we get here, then there are no top-level OR blocks.
         * However, every time we hit a top level optional statement,
         * we must find the max of its contents and add that to our
         * running total.
         */

        while (desTokenizer.hasMoreTokens()) {
            token = desTokenizer.nextToken();
            if ("[".equals(token)) {
                /* Must get description up to the matching ']'. */
                String optionalDescription = "";
                int depth = 1;
                while (true) {
                    if (!desTokenizer.hasMoreTokens())
                        /* Should not run out before finding matching ']'. */
                        return -1;
                    String tmpTok = desTokenizer.nextToken();
                    if ("[".equals(tmpTok)) depth++;
                    if ("]".equals(tmpTok)) {
                        depth--;
                        if (depth == 0) break;
                    }
                    optionalDescription += tmpTok + " ";
                } // while
                count += maxNumber(optionalDescription);
                continue;
            }
            count++;
        } // while
        return count;
    }

    /**
     * This method is used to get the value associated with a switch. It supports only very simply value association;
     * one switch to one value. It is used as follows:
     *
     * <PRE>
     * args = { "-s", "sourceFile", "-o", "outFile" }
     * paramAtSwitch("-s", args)
     * </PRE>
     *
     * would return "sourceFile".
     *
     * <PRE>
     * paramAtSwitch("-l", args)
     * </PRE>
     *
     * would return null.
     *
     * @param switchID the String representing the switch to look for.
     * @param args the array of Strings making up the argument list.
     * @return the value associated with the named switch, if the named switch exists in the args array. If the switch
     *     is not found, null is returned.
     */
    public static String paramAtSwitch(String switchID, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(switchID))
                if (i >= args.length) break;
                else return args[++i];
        }
        return null;
    }

    /**
     * This method is used to determine if a particular argument is found in the argument array. It is intended to
     * detect whether or not a switch is in the arg array, but it doesn't check for a '-'. It will return true if
     * switchID is exactly equal to any String in the argument array.
     *
     * @param switchID the String to look for.
     * @param args the array to look for the switch in.
     * @return true if switchID is exactly equal to any String in args.
     */
    public static boolean switchFound(String switchID, String[] args) {
        for (String arg : args) if (arg.equals(switchID)) return true;
        return false;
    }

    /**
     * This routine determines whether we should consider the given arg to match the description, tok, if it appeared on
     * the command line.
     *
     * @param arg The actual arg given at the command line
     * @param tok The token description.
     */
    private static boolean match(String arg, String tok) {

        /*
         * Before we do anything, check if the tok is a special char.
         */
        if ("[".equals(tok) || "]".equals(tok) || "|".equals(tok)) return false;

        /*
         * First off, if one starts with '-', but the other doesn't, return
         * false.
         */
        if (arg.startsWith("-")) {
            if (!(tok.startsWith("-"))) return false;
        } else if (tok.startsWith("-")) return false;

        /*
         * Next, if they both start with a '-', then they have to match
         * exactly.
         */
        if ((arg.charAt(0) == '-') && (tok.charAt(0) == '-')) return arg.equals(tok);

        /*
         * If we made it here, then neither of them starts with a '-',
         * and we can consider them a match.
         */
        return true;
    } // match()

    /**
     * This routine is used to find all the possible FIRST tokens in an optional block for the purposes of deciding
     * whether we need to make a recursive isValid() call on this block. It returns a Vector containing Strings, any one
     * of which could legally appear as the first arg to satisfy this block.
     *
     * <p>
     *
     * <p>EXAMPLE: If it is called given the String <br>
     * <CODE>&quot;[ -s server ] [ -p port ] name&quot;</CODE><br>
     * then the result would be a Vector containing the Strings &quot;-s&quot; &quot;-p&quot; and &quot;name&quot;.
     *
     * @param block the block to check.
     * @return a Vector containing all possible first Strings.
     */
    private static Vector firstTokens(String description) {
        KSStringTokenizer tokenizer = new KSStringTokenizer(description);
        Vector firsts = new Vector();

        while (tokenizer.hasMoreTokens()) {
            String tok = tokenizer.nextToken();

            /*
             * If the token is an optional block opener, get the set of firsts
             * from the whole optional block and add them to our first set.
             */
            if ("[".equals(tok)) {
                /*
                 * First, we need to get this description String.
                 */
                String optionalDescription = "";
                /* For nested optional blocks. */
                int depth = 1;
                while (true) {
                    if (!tokenizer.hasMoreTokens()) {
                        return null;
                    }
                    String tmpTok = tokenizer.nextToken();
                    if ("[".equals(tmpTok)) depth++;
                    if ("]".equals(tmpTok)) {
                        depth--;
                        if (depth == 0) break;
                    }
                    optionalDescription += tmpTok + " ";
                } // while

                Vector subFirsts = ArgumentChecker.firstTokens(optionalDescription);

                if (subFirsts == null)
                    /*
                     * Something went wrong.
                     */
                    return null;

                /*
                 * Now, we need to add these to our own Vector.
                 */
                for (int i = 0; i < subFirsts.size(); i++) firsts.addElement(subFirsts.elementAt(i));

                continue;
            } // if - token is a "["?

            /*
             * If it wasn't an optional block opener, then it must have
             * been a legal token.  Why can't it have been an optional
             * block closer?  Because if it were, it would have to have
             * a matching opener, and the closer would have been parsed
             * out of the tokenizer in making the sub-description.
             */

            firsts.addElement(tok);
            return firsts;
        } // while - tokenizer has more tokens

        return firsts;
    } // firstTokens()

    /**
     * This method is used to determine how many distinct sections there are in the given description, using the given
     * character as the seperator. It is intended to be used to count how many OR blocks there are. The sep variable
     * will probably be the '|' character. It keeps track of depth as well, so if it is given a description such as<br>
     * <CODE>
     * &quot;-s server | [ -p port | -default ]&quot;<BR>
     * </CODE> it would return 2, not 3, even though there are 2 seperators.
     *
     * @param description the description String to evaluate
     * @param sep the character to use as the seperator token.
     * @return the number of sections in the description, seperated by the given sep token. Returns 0 if an error
     *     occurs.
     */
    private static int numSections(String description, char sep) {
        int depth = 0;
        int numSections = 1; // We have at least one section.
        KSStringTokenizer tokenizer = new KSStringTokenizer(description);
        String tok;

        while (tokenizer.hasMoreTokens()) {
            tok = tokenizer.nextToken();
            if ("[".equals(tok)) {
                depth++;
                continue;
            }
            if ("]".equals(tok)) {
                depth--;
                continue;
            }
            if (tok.charAt(0) == sep) if (depth == 0) numSections++;
        } // while
        if (depth != 0)
            /* Unbalanced '['. */
            return 0;
        else return numSections;
    } // numSections()

    /**
     * This method is used to seperate out the various blocks based on the seperator token (usually '|', for OR). It
     * returns a Vector of all the distinct blocks possible from the given description String. For example, if the
     * description String given were<br>
     * <CODE>
     * &quot;-s server | [ -p port | -default ]&quot;<BR>
     * </CODE> this method would return a Vector containing the following 2 Strings:<br>
     *
     * <UL>
     *   <LI>&quot;-s server&quot;
     *   <LI>&quot;[ -p | -default ]&quot;
     * </UL>
     *
     * @param description the description to be evaluated.
     * @param sep the character to use as the seperator token, usually '|' for OR.
     * @return a Vector containing all the different '|' seperated top-level Strings parsed out of description.
     */
    private static Vector seperateBlocks(String description, char sep) {
        int numSections = ArgumentChecker.numSections(description, sep);
        int depth = 0;
        Vector sections = new Vector();
        String currBlock = "";
        String tok;
        KSStringTokenizer tokenizer = new KSStringTokenizer(description);

        for (int i = 0; i < numSections; i++) {
            while (true) {
                if (!tokenizer.hasMoreTokens()) break;
                tok = tokenizer.nextToken();
                if (tok.charAt(0) == sep) if (depth == 0) break;
                currBlock += tok + " ";
                if ("[".equals(tok)) depth++;
                if ("]".equals(tok)) depth--;
            } // while
            sections.addElement(currBlock.trim());
            currBlock = "";
        } // for

        return sections;
    } // seperateBlocks()
}
