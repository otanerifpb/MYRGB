package com.example.myrgb.cadastro

// Modelo dos objetos da class com código da cor em Hexadecimal
//class Cor(var nomeCor: String, var codeCor: String): java.io.Serializable {
//
//    override fun toString(): String{
//        return "Nome: $nomeCor\n Código: $codeCor"
//    }
//}

// Modelo dos objetos da class com código da cor em RGB
class Cor(var nomeCor: String, var r: Int, var g: Int, var b: Int): java.io.Serializable {

    override fun toString(): String{
        return "Nome: $nomeCor\n R: $r, G: $g, B: $b "
    }
}