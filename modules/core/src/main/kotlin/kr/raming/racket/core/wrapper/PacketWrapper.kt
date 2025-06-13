package kr.raming.racket.core.wrapper

import io.netty.buffer.ByteBuf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class PacketWrapper<T : Any>(
    private val serializer: KSerializer<T>
) {
    fun write(obj: T, buf: ByteBuf) {
        val json = Json.encodeToString(serializer, obj)
        val bytes = json.toByteArray(Charsets.UTF_8)
        buf.writeInt(bytes.size)
        buf.writeBytes(bytes)
    }

    fun read(buf: ByteBuf): T {
        val len = buf.readInt()
        val bytes = ByteArray(len)
        buf.readBytes(bytes)
        val json = String(bytes, Charsets.UTF_8)
        return Json.decodeFromString(serializer, json)
    }
}