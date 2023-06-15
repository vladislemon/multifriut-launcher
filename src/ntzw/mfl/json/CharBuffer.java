package ntzw.mfl.json;

public class CharBuffer {

    private final char[] internalArray;
    private int position;

    public CharBuffer(String s) {
        internalArray = s.toCharArray();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void reset() {
        setPosition(0);
    }

    public void skip(int length) {
        setPosition(position + length);
    }

    public boolean hasNext() {
        return position < internalArray.length;
    }

    public char next() {
        if(hasNext()) {
            return internalArray[position++];
        }
        return 0;
    }

    public char nextNonWhitespace() {
        char c;
        while((c = next()) != 0) {
            if(!Character.isWhitespace(c)) {
                return c;
            }
        }
        return 0;
    }

    public char[] getSubArray(int start, int end) {
        if(start > end) return new char[] {};
        char[] subArray = new char[end - start + 1];
        System.arraycopy(internalArray, start, subArray, 0, subArray.length);
        return subArray;
    }
}
