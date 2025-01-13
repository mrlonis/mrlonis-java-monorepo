package com.mrlonis.mar45.mars.mips.instructions;

import com.mrlonis.mar45.mars.*;
import com.mrlonis.mar45.mars.assembler.*;
import java.util.*;

/*
Copyright (c) 2003-2006,  Pete Sanderson and Kenneth Vollmar

Developed by Pete Sanderson (psanderson@otterbein.edu)
and Kenneth Vollmar (kenvollmar@missouristate.edu)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
 */

/**
 * Base class to represent member of MIPS instruction set.
 *
 * @author Pete Sanderson and Ken Vollmar
 * @version August 2003
 */
public abstract class Instruction {
    /**
     * Length in bytes of a machine instruction. MIPS is a RISC architecture so all instructions are the same length.
     * Currently set to 4.
     */
    public static final int INSTRUCTION_LENGTH = 4;

    public static final int INSTRUCTION_LENGTH_BITS = 32;
    /** Characters used in instruction mask to indicate bit positions for 'f'irst, 's'econd, and 't'hird operands. */
    public static char[] operandMask = {'f', 's', 't'};
    /** The instruction name. * */
    protected String mnemonic;
    /** Example usage of this instruction. Is provided as subclass constructor argument. * */
    protected String exampleFormat;
    /** Description of instruction for display to user * */
    protected String description;
    /** List of tokens generated by tokenizing example usage (see <tt>exampleFormat</tt>). * */
    protected TokenList tokenList;

    /**
     * Get operation mnemonic
     *
     * @return operation mnemonic (e.g. addi, sw)
     */
    public String getName() {
        return mnemonic;
    }

    /**
     * Get string descriptor of instruction's format. This is an example MIPS assembler instruction usage which contains
     * the operator and all operands. Operands are separated by commas, an operand that begins with a '$' represents a
     * register, and an integer operand represents an immediate value or address. Here are two examples: "nor $1,$2,$3"
     * and "sw $1,100($2)"
     *
     * @return String representing example instruction format.
     */
    public String getExampleFormat() {
        return exampleFormat;
    }

    /**
     * Get string describing the instruction. This is not used internally by MARS, but is for display to the user.
     *
     * @return String describing the instruction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get TokenList corresponding to correct instruction syntax. For example, the instruction with format "sw
     * $1,100($2)" yields token list <operator><register_number><integer><left_paren><register_number><right_parent>
     *
     * @return TokenList object representing correct instruction usage.
     */
    public TokenList getTokenList() {
        return tokenList;
    }

    /**
     * Get length in bytes that this instruction requires in its binary form. Default is 4 (holds for all basic
     * instructions), but can be overridden in subclass.
     *
     * @return int length in bytes of corresponding binary instruction(s).
     */
    public int getInstructionLength() {
        return INSTRUCTION_LENGTH;
    }

    /** Used by subclass constructors to extract operator mnemonic from the instruction example. * */
    protected String extractOperator(String example) {
        StringTokenizer st = new StringTokenizer(example, " ,\t");
        return st.nextToken();
    }

    /**
     * Used to build a token list from the example instruction provided as constructor argument. Parser uses this for
     * syntax checking. *
     */
    protected void createExampleTokenList() {
        try {
            tokenList = ((new Tokenizer()).tokenizeExampleInstruction(exampleFormat));
        } catch (ProcessingException pe) {
            System.out.println(
                    "CONFIGURATION ERROR: Instruction example \"" + exampleFormat + "\" contains invalid token(s).");
        }
    }
}
