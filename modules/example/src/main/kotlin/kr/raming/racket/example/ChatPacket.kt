package kr.raming.racket.example

import kr.raming.racket.core.packet.Packet

class ChatPacket(override var message: String = "") : Packet() {
}