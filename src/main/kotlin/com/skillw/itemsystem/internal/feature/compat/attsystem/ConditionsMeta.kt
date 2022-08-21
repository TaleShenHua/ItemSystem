package com.skillw.itemsystem.internal.feature.compat.attsystem

import com.skillw.itemsystem.api.builder.ItemData
import com.skillw.itemsystem.api.meta.BaseMeta
import com.skillw.itemsystem.api.meta.data.Memory
import com.skillw.itemsystem.api.meta.data.Memory.Companion.get
import com.skillw.itemsystem.util.NBTUtils.toMutableMap
import com.skillw.pouvoir.api.annotation.AutoRegister
import taboolib.module.nms.ItemTag

@AutoRegister(test = "com.skillw.attsystem.AttributeSystem")
object ConditionsMeta : BaseMeta("conditions") {
    override fun invoke(memory: Memory) {
        with(memory) {
            val map = memory.get<Map<String, Any>>("conditions").analysis()
            nbt.getOrPut("CONDITION_DATA") { ItemTag() }.asCompound().putAll(ItemTag.toNBT(map).asCompound())
        }
    }


    override fun loadData(data: ItemData): Any? {
        return data.itemTag["CONDITION_DATA"]?.asCompound()?.toMutableMap()
    }

}