package kr.raming.racket.core.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import kr.raming.racket.core.handler.ServerChannelHandler
import kr.raming.racket.core.handler.ServerPacketHandler
import kr.raming.racket.core.packet.Packet
import kr.raming.racket.core.util.PacketDecoder
import kr.raming.racket.core.util.PacketEncoder
import kotlin.reflect.KClass

class RacketServer() {
	private lateinit var channel: Channel
	private val bossGroup = NioEventLoopGroup(1)
	private val workerGroup = NioEventLoopGroup()
	private var port: Int = 0
	private val handler = ServerPacketHandler()

	fun port(port: Int) : RacketServer {
		this.port = port
		return this
	}

	fun <T: Packet> register(packet: KClass<T>, handleAction: (ChannelHandlerContext, T) -> Unit) : RacketServer {
		handler.setHandler(packet, handleAction)
		return this
	}

	fun start() : RacketServer {
		val future = ServerBootstrap()
			.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel::class.java)
			.childHandler(object : ChannelInitializer<SocketChannel>() {
				override fun initChannel(ch: SocketChannel) {
					ch.pipeline().apply {
						addLast(PacketDecoder())
						addLast(PacketEncoder())
						addLast(ServerChannelHandler(handler))
					}
				}
			}).bind(port).sync()

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
		bossGroup.shutdownGracefully()
		workerGroup.shutdownGracefully()
	}
}