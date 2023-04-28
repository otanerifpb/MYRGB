package com.example.myrgb.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
//import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myrgb.R
import com.example.myrgb.cadastro.Cadastro
import com.example.myrgb.cadastro.Cor
import java.util.*

// Class para fazer a adaptação dos objetos e ações na Tela do Main
class MyAdapter(val lista: MutableList<Cor>): RecyclerView.Adapter<MyAdapter.MyHolder>() {
//class MyAdapter(val lista: Cor): RecyclerView.Adapter<MyAdapter.MyHolder>() {

    // Var para os clicks na Tela
    var onItemClickRecyclerView: OnItemClickRecyclerView? = null
    var onItemClick: OnItemClickRecyclerView? = null
    var onItemLongClickRecyclerView: OnItemLongClickRecyclerView? = null

    // Resgate do Modelo de Objeto do item_list.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyHolder(view)
    }

    // Resgate de um objeto na lista da Tela
    override fun onBindViewHolder(holder: MyAdapter.MyHolder, position: Int) {
        val nome = this.lista.get(position)
        holder.tvNomeCor.text = nome.toString()
    }

    // Fun(getItemCount) é para a class MyAdapter fazer o controle dos objetos
    override fun getItemCount(): Int {
        //Log.d("APP_LOG", "MyAdapter_Fun(getItemCount)")
        return this.lista.size
    }

    // Fun(AddAdapter) para adicionar um objeto ao clicar no Botão/Ação Salvar
//    fun addAdapter(nome: String){
//        Log.d("APP_LOG", "MyAdapter_Salvar Objeto na Tela")
//        this.lista.add(nome)
//        this.notifyItemInserted(this.lista.size)
//    }
    fun addCorAdapter(cor: Cor){
        Log.d("APP_LOG", "MyAdapter_Salvar Objeto na Tela")
        this.lista.add(cor)
        this.notifyItemInserted(this.lista.size)
    }

    // Fun(deleteAdapter) para deletar um objeto ao clicar no Botão/Ação Deletar
    fun deleteAdapter(position: Int) {
        Log.d("APP_LOG", "MyAdapter_Deletar Objeto na Tela")
        this.lista.removeAt(position)
        notifyInteRemoved(position)
        notifyItemRangeChanged(position, this.lista.size)
    }

    // Fun(moveAdapter) para mover um objeto na Tema Main com o click arrataste
    fun moveAdapter(from: Int, to: Int) {
        Log.d("APP_LOG", "MyAdapter_Mover Objeto na Tela")
        Collections.swap(this.lista, from, to)
        notifyItemMoved(from, to)
    }

    // Fun(compartilharAdapter) para compartilhar um objeto ao deslizar para
//    fun compartilheAdapter(texto: Cor) {
//        Log.d("APP_LOG", "MyAdapter_Compartilhar Objeto da Tela")
//        val intent = Intent(Intent.ACTION_SEND).apply {
//            setType("text/plain")
//            putExtra(Intent.EXTRA_TEXT, texto.nomeCor)
//        }
//
//        if(intent.resolveActivity(packageManager)!= null) {
//            startActivity(intent)
//        }else{
//            Log.d("APP_LOG", "MyAdapter_Compartilhar cancelado!!")
//            Toast.makeText(this, "Não é possível Compartilhar", Toast.LENGTH_SHORT).show()
//        }
//    }

    // Class MyHolde serve para controlar os objetos da Tela para realizar uma ação
    inner class MyHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var tvNomeCor: TextView

        init {
            this.tvNomeCor = itemView.findViewById(R.id.tvNomeCor)

            itemView.setOnClickListener {
                this@MyAdapter.onItemClickRecyclerView?.onItemClick(this.adapterPosition)
                this@MyAdapter.onItemClick?.onItemClick(this.adapterPosition)
            }

            itemView.setOnLongClickListener {
                this@MyAdapter.onItemLongClickRecyclerView?.onItemLongClick(this.adapterPosition)
                true
            }
        }
    }

    // Fun(notifyInteRemoved) é necessária para o funcionamento da fun(deleteAdapter)
    private fun notifyInteRemoved(position: Int) {

    }
}