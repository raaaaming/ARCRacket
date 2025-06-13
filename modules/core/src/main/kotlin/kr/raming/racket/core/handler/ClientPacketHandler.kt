package kr.raming.racket.core.handler

import io.netty.channel.ChannelHandlerContext
import kr.raming.racket.core.packet.Packet
import kotlin.reflect.KClass

class ClientPacketHandler : PacketHandler {

    private val handlers = mutableMapOf<KClass<out Packet>, (ChannelHandlerContext, Packet) -> Unit>()

    fun onConnected(ctx: ChannelHandlerContext) {
        println("클라이언트: 서버에 연결됨")
    }

    fun onDisconnected(ctx: ChannelHandlerContext) {
        println("클라이언트: 연결 종료됨")
    }

    /** 재연결 시도 처리 */
    fun onReconnect(ctx: ChannelHandlerContext) {
        TODO("Not yet implemented")
    }

    /** 예외 발생 시 처리 */
    fun onError(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
    }

    // 제네릭 함수로 핸들러 등록
    fun <T : Packet> setHandler(
        packetClass: KClass<T>,
        handler: (ChannelHandlerContext, T) -> Unit
    ) {
        @Suppress("UNCHECKED_CAST")
        handlers[packetClass] = { ctx, packet ->
            if (packetClass.isInstance(packet)) {
                handler(ctx, packet as T)
            }
        }
    }

    // 패킷 처리
    override fun handle(ctx: ChannelHandlerContext, packet: Packet) {
        if (!handlers.containsKey(packet::class)) return
        handlers[packet::class]?.invoke(ctx, packet)
    }
}