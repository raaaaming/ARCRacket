package kr.raming.racket.core.handler

import io.netty.channel.ChannelHandlerContext
import kr.raming.racket.core.packet.Packet
import kotlin.reflect.KClass

class ServerPacketHandler : PacketHandler {

    private val handlers = mutableMapOf<KClass<out Packet>, (ChannelHandlerContext, Packet) -> Unit>()

    /** 클라이언트 연결 시 처리 */
    fun onConnect(ctx: ChannelHandlerContext) {
        println("서버: 클라이언트 연결됨")
    }

    /** 클라이언트 연결 종료 시 처리 */
    fun onDisconnect(ctx: ChannelHandlerContext) {
        println("서버: 종료됨")
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
