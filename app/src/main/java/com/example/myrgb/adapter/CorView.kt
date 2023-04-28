package com.example.myrgb.adapter

import com.example.myrgb.cadastro.Cor

// ********************Fum para atualização de um objeto
interface CorView {
    fun displayCor(cor: Cor)

    fun updateCor(cor: Cor)
}