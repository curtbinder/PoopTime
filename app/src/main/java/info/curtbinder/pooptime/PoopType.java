/*
 * MIT License
 * Copyright (c) 2020 Curt Binder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package info.curtbinder.pooptime;

public class PoopType {

    public static final int NORMAL = 0;
    public static final int HARD = 1;
    public static final int LOOSE = 2;

    public static final String NORMAL_TEXT = "Normal";
    public static final String HARD_TEXT = "Hard";
    public static final String LOOSE_TEXT = "Loose";
    public static final String NONE_TEXT = "None";


    public static String getTypeStringFromInt(int type) {
        switch (type) {
            case NORMAL:
                return NORMAL_TEXT;
            case HARD:
                return HARD_TEXT;
            case LOOSE:
                return LOOSE_TEXT;
        }
        return NONE_TEXT;
    }

    public static int getTypeIntFromString(String type) {
        int retVal;
        switch(type) {
            default:
            case NORMAL_TEXT:
                retVal = NORMAL;
                break;
            case HARD_TEXT:
                retVal = HARD;
                break;
            case LOOSE_TEXT:
                retVal = LOOSE;
                break;
        }
        return retVal;
    }
}
