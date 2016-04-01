/**
 *
 */
package util.byteEncode;

/**
 * @author jwvl
 * @date Sep 1, 2015
 */
public interface ByteCodable<T extends Object> {
    byte getByte();

    T fromByte(byte b);


}
