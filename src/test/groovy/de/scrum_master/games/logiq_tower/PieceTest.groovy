package de.scrum_master.games.logiq_tower

import spock.lang.Specification

class PieceTest extends Specification {
    def "GetSymbol"() {
        expect:
        Piece.CENTRAL_PIECES.get(0).getSymbol() == '0'
        println "Test ÄÖÜ äöü ß"
    }
}
