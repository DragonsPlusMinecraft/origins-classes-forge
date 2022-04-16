package limonblaze.originsclasses.util;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;
import java.util.function.BiFunction;

public class NbtType<T> {
    public static final NbtType<Byte> BYTE = new NbtType<>(1, CompoundTag::getByte);
    public static final NbtType<Short> SHORT = new NbtType<>(2, CompoundTag::getShort);
    public static final NbtType<Integer> INT = new NbtType<>(3, CompoundTag::getInt);
    public static final NbtType<Long> LONG = new NbtType<>(4, CompoundTag::getLong);
    public static final NbtType<Float> FLOAT = new NbtType<>(5, CompoundTag::getFloat);
    public static final NbtType<Double> DOUBLE = new NbtType<>(6, CompoundTag::getDouble);
    public static final NbtType<byte[]> BYTE_ARRAY = new NbtType<>(7, CompoundTag::getByteArray);
    public static final NbtType<String> STRING = new NbtType<>(8, CompoundTag::getString);
    //TagList is not compatible with the getter bifunction
    public static final NbtType<CompoundTag> COMPOUND = new NbtType<>(10, CompoundTag::getCompound);
    public static final NbtType<int[]> INT_ARRAY = new NbtType<>(11, CompoundTag::getIntArray);
    public static final NbtType<long[]> LONG_ARRAY = new NbtType<>(12, CompoundTag::getLongArray);
    //TAG_ANY_NUMMERIC has no getter
    public static final NbtType<UUID> UUID = new NbtType<>(100, CompoundTag::getUUID) {
        @Override
        public boolean check(CompoundTag nbt, String key) {
            return nbt.hasUUID(key);
        }
    };

    private final int type;
    private final BiFunction<CompoundTag, String, T> getter;

    public NbtType(int type, BiFunction<CompoundTag, String, T> getter) {
        this.type = type;
        this.getter = getter;
    }

    public T get(CompoundTag nbt, String key) {
        return this.getter.apply(nbt, key);
    }

    public boolean check(CompoundTag nbt, String key) {
        return nbt.contains(key, this.type);
    }

}
