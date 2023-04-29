package com.example.myrgb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.myrgb.cadastro.Cor

class FormActivity : AppCompatActivity() {
    // Var para sincronizar Back-End com o Front-End com a Tela Form
    private lateinit var etNomeForm: EditText
    private lateinit var tvRedForm: TextView
    private lateinit var tvGreenForm: TextView
    private lateinit var tvBlueForm: TextView
    private lateinit var tvResultadoForm: TextView
    private lateinit var sbRedForm: SeekBar
    private lateinit var sbGreenForm: SeekBar
    private lateinit var sbBlueForm: SeekBar
    private lateinit var sbNomeForm: SeekBar
    private lateinit var llResultadoForm: LinearLayout
    private lateinit var btSalvarForm: Button
    private lateinit var btVoltarForm: Button

    // Var para a lista de objetos da Tela Main
    private var cores: MutableList<Cor>

    // Inicialização de objetos/teste na Tela Main
    init{
        this.cores = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        // Sincronismo Back-End com Front-End
        this.etNomeForm = findViewById(R.id.etNomeForm)
        this.tvRedForm = findViewById(R.id.tvRedForm)
        this.tvGreenForm = findViewById(R.id.tvGreenForm)
        this.tvBlueForm = findViewById(R.id.tvBlueForm)
        this.tvResultadoForm = findViewById(R.id.tvResultadoForm)
        this.sbRedForm = findViewById(R.id.sbRedForm)
        this.sbGreenForm = findViewById(R.id.sbGreenForm)
        this.sbBlueForm = findViewById(R.id.sbBlueForm)
        this.llResultadoForm = findViewById(R.id.llResultadoForm)
        this.btSalvarForm = findViewById(R.id.btSalvarForm)
        this.btVoltarForm = findViewById(R.id.btVoltarForm)

        // Chamar a class MudarCor ao deslizar na Seek Bar
        this.sbRedForm.setOnSeekBarChangeListener(MudarCor())
        this.sbGreenForm.setOnSeekBarChangeListener(MudarCor())
        this.sbBlueForm.setOnSeekBarChangeListener(MudarCor())
        this.llResultadoForm.setBackgroundColor(criarCor())

        // Botão Salvar da Tela Form para Tela Main
        this.btSalvarForm.setOnClickListener({salvar()})
        // Botão Voltar da Tela Form para Tela Main
        this.btVoltarForm.setOnClickListener({voltar()})

        // Para preencher o formulário com os dados do objeto que foi clicado na Tela MainActivity
        if (this.intent.hasExtra("COR")) {
            // cor = this.intent.getStringExtra("COR") as Cor
            var cor = this.intent.getSerializableExtra("COR") as Cor

            etNomeForm.setText(cor.nomeCor)
            tvRedForm.text = cor.r.toString()
            tvGreenForm.text = cor.g.toString()
            tvBlueForm.text = cor.b.toString()

        }
    }

    // Fum(criarCor) para criar a cor pelo Seek Bar
    fun criarCor(): Int{
        val red = this@FormActivity.sbRedForm.progress
        val green = this@FormActivity.sbGreenForm.progress
        val blue = this@FormActivity.sbBlueForm.progress

        return Color.rgb(red, green, blue)
    }

    // Inner class para mudar a cor da Seek Bar ao deslizar
    inner class MudarCor: SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val red = this@FormActivity.sbRedForm.progress
            val green = this@FormActivity.sbGreenForm.progress
            val blue = this@FormActivity.sbBlueForm.progress

            this@FormActivity.tvRedForm.text = red.toString()
            this@FormActivity.tvGreenForm.text = green.toString()
            this@FormActivity.tvBlueForm.text = blue.toString()

            this@FormActivity.llResultadoForm.setBackgroundColor(this@FormActivity.criarCor())

            fun hexadec(): String {
                var hexa = "#" + Integer.toHexString(Color.rgb(red, green, blue)).substring(2)
                    .toUpperCase()
                return hexa
            }
            this@FormActivity.tvResultadoForm.text = hexadec()
        }

        // Para o funcionamento da inner class são necessários as fun() abaixo
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            //TODO("Not yet implemented")
        }
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //TODO("Not yet implemented")
        }
    }

    // Acção ao clicar no botão Salvar da Tela Cadastro
    fun salvar() {
        val nomeCor = this.etNomeForm.text.toString()
        //val codeCor = this.tvResultadoForm.text.toString()
        val r = this.tvRedForm.text.toString().toInt()
        val g = this.tvGreenForm.text.toString().toInt()
        val b = this.tvBlueForm.text.toString().toInt()
        val cor = Cor(nomeCor, r, g, b)
        //val cor = Cor(nomeCor, codeCor)
        cores.add(cor)
        val intent = Intent().apply {
            putExtra("COR", cor)
        }
        setResult(RESULT_OK, intent)
        this.etNomeForm.text.clear()
        //Log.i("APP_LOG", "Form Cadastro salvo com sucesso")
        //Toast.makeText(this, "Form Cadastro salvo com sucesso", Toast.LENGTH_SHORT).show()
        finish()
    }

    // Ação ao clicar no Botão Cancelar da Tela Form, que retorna para Tela Main
    fun voltar() {
        //Log.i("APP_LOG", "Form_Sair Tela Form")
        finish()
    }
}