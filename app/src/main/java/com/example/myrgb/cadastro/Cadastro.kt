package com.example.myrgb.cadastro

class Cadastro() {
    private var cores: MutableList<Cor>

    init{
        this.cores = mutableListOf()
    }

    // Fun(Adicionar) para adicionar objeto no MainActivity.kt
    fun addCorCadastro(cor: Cor) {
        this.cores.add(cor)
    }

    // Fun(Size) para usar no MainActivity.kt
    fun sizeCadastro(): Int {
        return this.cores.size
    }
}