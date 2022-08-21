package com.skillw.itemsystem.internal.feature.listener

import com.skillw.itemsystem.api.glow.GlowColor.Companion.addGlowEntity
import com.skillw.itemsystem.api.glow.GlowColor.Companion.initGlowColors
import com.skillw.itemsystem.internal.feature.ItemCache.cacheTag
import com.skillw.itemsystem.util.ColorUtils.toColor
import com.skillw.itemsystem.util.nms.NMS
import org.bukkit.entity.Item
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submitAsync
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.module.nms.PacketSendEvent
import kotlin.experimental.or

private object GlowListener {
    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        submitAsync { e.player.initGlowColors() }
    }

    @SubscribeEvent
    fun e(event: PacketSendEvent) {
        val packet = event.packet
        if (packet.name != "PacketPlayOutEntityMetadata") return
        with(packet.source) {
            val player = event.player
            val id = getProperty<Int>("a") ?: return
            val entity = NMS.INSTANCE.getEntity(player.world, id) as? Item? ?: return
            val tag = entity.itemStack.cacheTag()
            val colorStr = tag.getDeep("ITEM_SYSTEM.glow-color")?.asString() ?: return
            val flag = getProperty<List<Any>>("b")?.get(0) ?: return
            val byte = flag.getProperty<Any>("b") as? Byte? ?: return
            flag.setProperty("b", byte or (1 shl 6))
            player.addGlowEntity(entity, colorStr.toColor())
        }
    }
}