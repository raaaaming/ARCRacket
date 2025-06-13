package kr.raming.racket.core.packet

import io.netty.buffer.ByteBuf

open class Packet(open var message: String = "") {

    fun write(buf: ByteBuf) {
        val bytes = message.toByteArray(Charsets.UTF_8)
        buf.writeInt(bytes.size)
        buf.writeBytes(bytes)
    }

    fun read(buf: ByteBuf) {
        val length = buf.readInt()
        val bytes = ByteArray(length)
        buf.readBytes(bytes)
        message = String(bytes, Charsets.UTF_8)
    }
}