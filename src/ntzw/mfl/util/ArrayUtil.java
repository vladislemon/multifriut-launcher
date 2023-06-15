package ntzw.mfl.util;

public class ArrayUtil {

    public static boolean contains(Object[] array, Object object) {
        if(object == null) {
            for(Object entry : array) {
                if(entry == null)
                    return true;
            }
            return false;
        }
        for(Object entry : array) {
            if(object.equals(entry))
                return true;
        }
        return false;
    }

    public static int getIndex(Object[] array, Object object) {
        if(object == null) {
            for(int i = 0; i < array.length; i++) {
                if(array[i] == null)
                    return i;
            }
            return -1;
        }
        for(int i = 0; i < array.length; i++) {
            if(object.equals(array[i]))
                return i;
        }
        return -1;
    }

    public static Object[] subarray(Object[] array, int offset, int length) {
        Object[] subarray = new Object[length];
        System.arraycopy(array, offset, subarray, 0, length);
        return subarray;
    }
}
