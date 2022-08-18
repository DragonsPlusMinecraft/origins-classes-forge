package dev.limonblaze.originsclasses.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.UUID;
import java.util.function.BiFunction;

public class NbtType<T> {
    public static final NbtType<Byte> BYTE = new NbtType<>(Tag.TAG_BYTE, CompoundTag::getByte);
    public static final NbtType<Short> SHORT = new NbtType<>(Tag.TAG_SHORT, CompoundTag::getShort);
    public static final NbtType<Integer> INT = new NbtType<>(Tag.TAG_INT, CompoundTag::getInt);
    public static final NbtType<Long> LONG = new NbtType<>(Tag.TAG_LONG, CompoundTag::getLong);
    public static final NbtType<Float> FLOAT = new NbtType<>(Tag.TAG_FLOAT, CompoundTag::getFloat);
    public static final NbtType<Double> DOUBLE = new NbtType<>(Tag.TAG_DOUBLE, CompoundTag::getDouble);
    public static final NbtType<byte[]> BYTE_ARRAY = new NbtType<>(Tag.TAG_BYTE_ARRAY, CompoundTag::getByteArray);
    public static final NbtType<String> STRING = new NbtType<>(Tag.TAG_STRING, CompoundTag::getString);
    public static final NbtType<CompoundTag> COMPOUND = new NbtType<>(Tag.TAG_COMPOUND, CompoundTag::getCompound);
    public static final NbtType<int[]> INT_ARRAY = new NbtType<>(Tag.TAG_INT_ARRAY, CompoundTag::getIntArray);
    public static final NbtType<long[]> LONG_ARRAY = new NbtType<>(Tag.TAG_LONG_ARRAY, CompoundTag::getLongArray);
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
