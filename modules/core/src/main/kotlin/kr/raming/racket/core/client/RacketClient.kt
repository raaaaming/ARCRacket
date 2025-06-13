package kr.raming.racket.core.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import kr.raming.racket.core.handler.ClientChannelHandler
import kr.raming.racket.core.handler.ClientPacketHandler
import kr.raming.racket.core.packet.Packet
import kr.raming.racket.core.util.PacketDecoder
import kr.raming.racket.core.util.PacketEncoder
import kotlin.reflect.KClass

class RacketClient() {

	lateinit var channel: Channel
	private val group = NioEventLoopGroup()
	private var host: String = "localhost"
	private var port: Int = 0
	private val handler = ClientPacketHandler()

	fun bind(host: String, port: Int) : RacketClient {
		this.host = host
		this.port = port
		return this
	}

	fun <T: Packet> register(packet: KClass<T>, handleAction: (ChannelHandlerContext, T) -> Unit) : RacketClient {
		handler.setHandler(packet, handleAction)
		return this
	}

	fun start() : RacketClient {
		val bootstrap = Bootstrap()
			.group(group)
			.channel(NioSocketChannel::class.java)
			.handler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					ch.pipeline()
						.addLast(PacketDecoder())
						.addLast(PacketEncoder())
						.addLast(ClientChannelHandler(handler))
				}
			})

		val future = bootstrap.connect(host, port).sync()
		channel = future.channel()

		return this
	}

	fun send(packet: Packet): Boolean {
		val ch = channel
		return if (ch != null && ch.isActive) {
			ch.writeAndFlush(packet)
			true
		} else {
			println("채널이 연결되지 않았거나 비활성 상태입니다.")
			false
		}
	}

	fun stop() {
		channel.close().sync()
		group.shutdownGracefully()
	}
}