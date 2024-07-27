package io.github.bindglam.weirdcosmetic.utils

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

class ItemStackDataType : PersistentDataType<ByteArray, ItemStack> {
    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<ItemStack> {
        return ItemStack::class.java
    }

    override fun toPrimitive(complex: ItemStack, context: PersistentDataAdapterContext): ByteArray {
        return complex.serializeAsBytes()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): ItemStack {
        return ItemStack.deserializeBytes(primitive)
    }
}