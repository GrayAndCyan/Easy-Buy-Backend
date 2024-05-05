package com.mizore.easybuy.utils;

public class BloomFilter<T> {

    private final int bitSize;

    private final byte[] bitmap;

    public BloomFilter(int bitSize) {
        this.bitSize = bitSize;
        bitmap = new byte[(bitSize  +  7) / 8];
    }

    // 返回命中结果
    public boolean exist(T obj) {
        int i = obj.hashCode() % bitSize;
        return getBit(i) == 1;
    }

    // 在bitmap查找第i位
    private int getBit(int i) {
        // 计算字节索引
        int byteIndex = i / 8;
        // 计算位偏移
        int bitOffset = i % 8;
        // 获取对应的字节
        byte b = bitmap[byteIndex];
        // 获取对应位
        return (b >> bitOffset) & 1;
    }

    // 放入元素，返回对应位原来的值。即返回1代表hash冲突，0为不冲突
    public int set(T obj) {
        int i = obj.hashCode() % bitSize;
        int origin = getBit(i);
        setBit(i);
        return origin;
    }

    // 将指定位 置1
    private void setBit(int i) {
        // 计算字节索引和位偏移
        int byteIndex = i / 8;
        int bitOffset = i % 8;
        //使用位掩码将指定位设置为 1
        bitmap[byteIndex] |= (1 << bitOffset);
    }
}
