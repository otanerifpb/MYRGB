package com.example.myrgb

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myrgb.FormActivity.AtualizarCorForm
import com.example.myrgb.adapter.CorView
import com.example.myrgb.adapter.MyAdapter
import com.example.myrgb.adapter.OnItemClickRecyclerView
import com.example.myrgb.adapter.OnItemLongClickRecyclerView
import com.example.myrgb.cadastro.Cadastro
import com.example.myrgb.cadastro.Cor
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), CorView{

    // Var para sincronizar Back-End com o Front-End com a Tela Main
    private lateinit var rvNomeMain: RecyclerView
    private lateinit var fbAddMain: FloatingActionButton

    // Var para entrada de valor pela Janela Popup
    //private lateinit var etNome: EditText

    // Var para falar o objeto da Tela ao dar 1 click
    private lateinit var ttsTexto: TextToSpeech

    // Var para deslocamento de objeto na Tela
    private var posicao: Int = 0

    // Var para criar objetos/testar
    private var lista = mutableListOf<Cor>()
    private  var cor: Cor
    private  var cadastro: Cadastro

    // *****************Var para atualizar um objeto da Tela para o FormActivity
    private lateinit var atualizarCorForm: AtualizarCorForm

    // Inicialização de objetos/teste na Tela Main
    init {
        this.cadastro = Cadastro()
        //this.cor = Cor("Cor Teste1", "#77928D")
        this.cor = Cor("Cor Teste1", "10", "250", "35")
        this.lista.add(cor)
        //this.cor = Cor("Cor Teste2", "#77928D")
        this.cor = Cor("Cor Teste2", "0", "50", "5")
        this.lista.add(cor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sincronismo Back-End com Front-End
        this.rvNomeMain = findViewById(R.id.rvNomeMain)
        this.fbAddMain = findViewById(R.id.fbAddMain)

        // *************Sincronismo da Tela MainActivity com FormActivity
        //this.etNomeForm = findViewById(R.id.etNomeForm)
        //this.tvRedForm = findViewById(R.id.tvRedForm)
        //this.tvGreenForm = findViewById(R.id.tvGreenForm)
        //this.tvBlueForm = findViewById(R.id.tvBlueForm)

        // Var para o acesso de outra Tela (activity_form)
        var cadastroResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                // Validar a versão do SmartPhone
                val cor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("COR", Cor::class.java)
                } else {
                    it.data?.getSerializableExtra("COR")
                } as Cor
                // Fun(Adicionar) da class Cadastro
                //this.cadastro.addCorCadastro(cor)
                this.lista.add(cor)
                atualizarApp()

                //Toast.makeText(this, "Main Cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
                Log.i("APP_LOG", "Main_Cadastrada com sucesso!")
            }
        }

        // Botão Flutuante (Salvar) Tela Main para Tela Form
        this.fbAddMain.setOnClickListener {
            Log.i("APP_LOG", "Main_Botão Flutuante")
            val intent = Intent(this, FormActivity::class.java)
            cadastroResult.launch(intent)
        }

        // Restart do aplicativo
        atualizarApp()

        // Falar objeto da tela
        this.ttsTexto = TextToSpeech(this, null)

        // Config movimentação objeto na Tela
        ItemTouchHelper(OnSwipe()).attachToRecyclerView(this.rvNomeMain)
    }

    fun atualizarApp() {
        // Criar a lista na Tela Main
        this.rvNomeMain.adapter = MyAdapter(this.lista)

        // Chamar a fun(OnItemClick) quando der um click curto no objeto na Tela Main
        (this.rvNomeMain.adapter as MyAdapter).onItemClickRecyclerView = OnItemClick()

        // Chamar a fun(LongClickList) quando der um click Longo no objeto na Tela Main
        (this.rvNomeMain.adapter as MyAdapter).onItemLongClickRecyclerView = LongClickList()
    }

    // Ação do click curto no objeto da Tela, falar o objeto da Tela
    inner class OnItemClick: OnItemClickRecyclerView {
        override fun onItemClick(position: Int){
            val nome = this@MainActivity.lista.get(position)
            //Toast.makeText(this@MainActivity, nome, Toast.LENGTH_SHORT).show()
            Log.d("APP_LOG", "Main_Click Curto: "+nome)
            this@MainActivity.ttsTexto.speak(nome.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // Ação do click longo no objeto da Tela, chama a Janela Popup (Deletar)
    inner class LongClickList: OnItemLongClickRecyclerView {
        override fun onItemLongClick(position: Int): Boolean {
            Log.d("APP_LOG", "Main_Click Longo Atualizar")
            posicao = position

            //atualizarCor(this@MainActivity.lista.get(position))
            var cor = this@MainActivity.lista.get(posicao)

            // *********************Setar os valores para o FormActivity
            //atualizarCorForm = FormActivity.AtualizarCorForm(cor, this)
            //this@MainActivity.etNomeForm.setText(cor.nomeCor)
            // atualizarCorForm = AtualizarCorForm(cor, this@MainActivity)

            atualizarCor(cor)
            Log.d("APP_LOG", "Main_Lista Atual: " + this@MainActivity.lista)
            //Toast.makeText(this@MainActivity, st, Toast.LENGTH_SHORT).show()
            return true
        }
    }

    // Fun(AtualizarCor) chama Form com um click Longo no objeto da Tela
    fun atualizarCor (cor: Cor) {
        Log.d("APP_LOG", "Main_AtualizarCor")
        //var cor = this@MainActivity.lista.get(posicao)
        val nomeTextView = findViewById<RecyclerView>(R.id.rvNomeMain)
       // nomeTextView.setRecyclerListener(cor.nomeCor)
        val intent = Intent(this, FormActivity::class.java).apply {
            putExtra("COR", nomeTextView.toString())
            //putExtra("COR", nomeTextView)
            Log.d("APP_LOG", "Main_AtualizarCor Intent: "+cor.nomeCor)
        }
        atualizarResult.launch(intent)
    }

    // Var para o acesso de outra Tela (activity_form)
    var atualizarResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            // Validar a versão do SmartPhone
            val cor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("COR", Cor::class.java)
            } else {
                it.data?.getSerializableExtra("COR")
            } as Cor

            //this.lista.add(cor)
            this.lista.set(posicao,cor)
            atualizarApp()

            //Toast.makeText(this, "Main_Atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            Log.i("APP_LOG", "Main_Atualizado com sucesso!")
            Log.d("APP_LOG", "Main_Lista Atualizada: " + this@MainActivity.lista)
        }
    }

    // Fun(Deletar) chama Janela Popup para deletar um onjeto da Tela(BTN Cancelar/Deletar)
    fun deletarTexto(texto: Cor): Boolean {
        Log.d("APP_LOG", "Main_Click Longo Janela Popup: "+texto)
        val builder = AlertDialog.Builder(this).apply {
            setTitle("                      ATENÇÃO")
            setMessage("              Deseja deletar \n"+texto+" !!")
            setPositiveButton("Deletar", OnClickDelete())
            setNegativeButton("Cancelar", null)
        }
        builder.create().show()
        (this.rvNomeMain.adapter as MyAdapter).notifyDataSetChanged()
        return true
    }

    // Deletar objeto ao fazer click no Botão Deletar
    inner class OnClickDelete: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            (this@MainActivity.rvNomeMain.adapter as MyAdapter).deleteAdapter(posicao)
            Log.d("APP_LOG", "Main_Lista Deletar: " + this@MainActivity.lista)
            atualizarApp()
        }
    }

    // Click para mover objeto na Tela em todas as direções
    inner class OnSwipe: ItemTouchHelper.SimpleCallback(
        // Config do arrastar na Tela (baixo/cima)
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        // Config do deslizar na Tela(esquerda/direita)
        ItemTouchHelper.START or ItemTouchHelper.END) {

        // Mover objeto na tela para Cima/Baixo
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            Log.d("APP_LOG", "Main_Mover Objeto na Tela onMove")
            //Acessar fun move() do MyAdapter
            (this@MainActivity.rvNomeMain.adapter as MyAdapter).moveAdapter(
                viewHolder.adapterPosition, target.adapterPosition
            )
            Log.d("APP_LOG", "Main_Lista Atualizada: " + this@MainActivity.lista)
            return true
        }

        //Deslizar o objeto da tela para a Direita (Delatar)
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (direction == ItemTouchHelper.START) {
                compartilharTexto(this@MainActivity.lista.get(posicao))
                Log.d("APP_LOG", "Main_Deslizar Esquerda")
            } else if (direction == ItemTouchHelper.END) {
                deletarTexto(this@MainActivity.lista.get(posicao))
                Log.d("APP_LOG", "Main_Deslizar Direita")
            }
            Log.d("APP_LOG", "Main_Lista Atualizada Deslizar: " + this@MainActivity.lista)
        }
    }

    // Fun(Compartilhar) chama Janela Popup para deletar um onjeto da Tela(BTN Compartilhar/Cancelar)
    fun compartilharTexto(texto: Cor): Boolean {
        Log.d("APP_LOG", "Main_Deslizar Esquerda Compartilhar: "+texto)
        val builder = AlertDialog.Builder(this).apply {
            setTitle("                      ATENÇÃO")
            setMessage("             Deseja Compartilhar \n"+texto+"!!")
            setPositiveButton("Compartilha", OnClickCompartilhe())
            setNegativeButton("Cancelar", null)
        }
        builder.create().show()
        (this.rvNomeMain.adapter as MyAdapter).notifyDataSetChanged()
        return true
    }

    // Deletar objeto ao fazer click no Botão Deletar
    inner class OnClickCompartilhe: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            var cor = this@MainActivity.lista.get(posicao)
            var hexacor = hexadec(cor.r.hashCode(), cor.g.hashCode(), cor.b.hashCode())
            compartilheMain(hexacor.toString())
            Log.d("APP_LOG", "Main_Lista Compartilhar: " + this@MainActivity.lista)
        }
    }

    fun hexadec(red: Int, green: Int, blue: Int): String {
        var hexa = "#" + Integer.toHexString(Color.rgb(red, green, blue)).substring(2)
            .toUpperCase()
        return hexa
    }

    // Fun(compartilharAdapter) para compartilhar um objeto ao deslizar para
    fun compartilheMain(texto: String) {
        Log.d("APP_LOG", "MyAdapter_Compartilhar Objeto da Tela")
        val intent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, texto)
        }

        if(intent.resolveActivity(packageManager)!= null) {
            startActivity(intent)
            atualizarApp()
        }else{
            Log.d("APP_LOG", "MyAdapter_Compartilhar cancelado!!")
            Toast.makeText(this, "Não é possível Compartilhar", Toast.LENGTH_SHORT).show()
        }
    }

    // *************Confirmar necessidade para atualizar
    override fun displayCor(cor: Cor) {
        TODO("Not yet implemented")
    }
    override fun updateCor(cor: Cor) {
        TODO("Not yet implemented")
    }
}