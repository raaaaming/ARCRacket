package kr.raming.racket.example

import kr.raming.racket.core.client.RacketClient
import kr.raming.racket.core.registry.PacketRegistry
import kr.raming.racket.core.server.RacketServer

fun main() {
	PacketRegistry.register(1) { ChatPacket() }
	PacketRegistry.register(2) { Imagepacket() }

	val server = RacketServer()
		.port(8080)
		.register(ChatPacket::class) { ctx, packet ->
			println(packet.message)
			ctx.writeAndFlush(ChatPacket("Hello, Client!"))
		}
		.register(Imagepacket::class) { ctx, packet ->
			println("url: ${packet.message}")
		}
		.start()

	val client = RacketClient()
		.bind("localhost",8080)
		.register(ChatPacket::class) { ctx, packet ->
			println("> ${packet.message}")
		}
		.register(Imagepacket::class) { ctx, packet ->
			println("image: ${packet.message}")
		}
		.start()

	client.send(ChatPacket("Hello, Server!"))
	client.send(Imagepacket("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"))
}