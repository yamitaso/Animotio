package ru.hatchup.animotio

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionbar = supportActionBar
        actionbar!!.title = ""
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun toDraw(view:View){
        val i : Intent = Intent(this,DrawActivity::class.java)
        startActivity(i)
    }
    fun exit(view:View){
        finish()
    }
    fun help(view:View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Помощь")
        builder.setMessage("Добро пожаловать в приложение для анимации \"Animotio\"\nВ этом приложении вы можете создавать покадровые анимации и сохранять их на память устройства." +
                "Для начала работы нажмите \"Начать рисовать\". Для добавления нового кадра нажмите \"+\". Для удаления текущего кадра нажмите на иконку корзины. С помощью кнопки \"Цвет\"" +
                " вы можете изменить цвет кисти. С помощью ползунка под холстом - изменить толщину линии. Выбранный вами цвет показан в виде полосы над холстом. Для сохранения анимации " +
                "нажмите кнопку с иконкой дискеты. Далее выберите задержку и количество повторений, после чего нажмите \"Сохранить\". Анимация будет сохранена в папку \"Animotio\" в корне диска.")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }
        builder.show()
    }

}