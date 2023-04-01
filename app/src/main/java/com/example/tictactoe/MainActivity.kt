package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    companion object
    {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }
    private val firstTurn = CROSS
    private val AiTurn  =  NOUGHT
    private var currentTurn = CROSS
    private var crossesScore = 0
    private var noughtsScore = 0
    private var boardList = Array(3){ arrayOfNulls<Button>(3)}

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
    }

    private fun initBoard()
    {
        boardList[0][0]=(binding.a1)
        boardList[0][1]=(binding.a2)
        boardList[0][2]=(binding.a3)
        boardList[1][0]=(binding.b1)
        boardList[1][1]=(binding.b2)
        boardList[1][2]=(binding.b3)
        boardList[2][0]=(binding.c1)
        boardList[2][1]=(binding.c2)
        boardList[2][2]=(binding.c3)
    }

    fun boardTapped(view: View)
    {
        if(view !is Button )
            return
        addToBoard(view)

        if(checkForVictory(NOUGHT))
        {
            noughtsScore++
            result("Noughts Win!")
            return
        }
        else if(checkForVictory(CROSS))
        {
            crossesScore++
            result("Crosses Win!")
            return
        }

        if(fullBoard())
        {
            result("Draw")
            return
        }

        bestMove()
        if(checkForVictory(NOUGHT))
        {
            noughtsScore++
            result("Noughts Win!")
            return
        }
        else if(checkForVictory(CROSS))
        {
            crossesScore++
            result("Crosses Win!")
            return
        }

        if(fullBoard())
        {
            result("Draw")
            return
        }
    }

    private fun checkForVictory(s: String): Boolean
    {
       // Horizontal Victory  and  Vertical Victory
        for(i in 0 until 3) {
            if(match(boardList[i][0]!!, s) && match(boardList[i][1]!!,s) && match(boardList[i][2]!!,s))
                return true
            if(match(boardList[0][i]!!, s) && match(boardList[1][i]!!,s) && match(boardList[2][i]!!,s))
                return true
        }
        //Diagonal Victory
        if(match(boardList[0][0]!!,s) && match(boardList[1][1]!!,s) && match(boardList[2][2]!!,s))
            return true
        if(match(boardList[0][2]!!,s) && match(boardList[1][1]!!,s) && match(boardList[2][0]!!,s))
          return true

        return false
    }

    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun result(title: String)
    {
        val message = "\nNoughts $noughtsScore\n\nCrosses $crossesScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset")
            { _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard()
    {
        for(i in 0 until  3)
        {
            for(j in 0 until 3) {
                boardList[i][j]!!.text = ""
            }
        }
        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean
    {
        for(i in 0 until  3)
        {
            for(j in 0 until 3) {
                if(boardList[i][j]!!.text == "")
                    return false
            }
        }
        return true
    }

    private fun addToBoard(button: Button)
    {
        if(button.text != "")
            return

        if(currentTurn == AiTurn)
        {
            button.text = NOUGHT
            currentTurn = firstTurn
        }
        else if(currentTurn == firstTurn)
        {
            button.text = CROSS
            currentTurn = AiTurn
        }
        setTurnLabel()
    }

    private fun setTurnLabel()
    {
        var turnText = ""
        if(currentTurn == firstTurn)
            turnText = "Turn $CROSS"
        else if(currentTurn == AiTurn)
            turnText = "Turn $NOUGHT"

        binding.turnTV.text = turnText
    }

    private fun bestMove(){
        var bestScore = Integer.MIN_VALUE
        var Move = binding.a1
        for(i in 0 until 3){
            for(j in 0 until 3){
                if(boardList[i][j]!!.text == ""){
                    boardList[i][j]!!.text = AiTurn
                    val score = minimax(boardList,0 , false)
                    boardList[i][j]!!.text = ""
                    if(score > bestScore){
                        bestScore = score
                        Move = boardList[i][j]!!
                    }
                }
            }
        }
        addToBoard(Move)
    }
    private fun minimax(boardList: Array<Array<Button?>>, depth:Int, isMaximizing:Boolean):Int {
         if(checkForVictory(NOUGHT))
             return 1
         else if(checkForVictory(CROSS))
             return -1
         else if(fullBoard())
             return 0

        if(isMaximizing){
            var bestScore = Integer.MIN_VALUE
            for(i in 0 until 3){
                for(j in 0 until 3){
                    if(boardList[i][j]!!.text == ""){
                        boardList[i][j]!!.text = AiTurn
                        val score = minimax(boardList,depth+1, false)
                        boardList[i][j]!!.text = ""
                        if(score > bestScore){
                            bestScore = score
                        }
                    }
                }
            }
            return bestScore
        }
        else{
            var bestScore = Integer.MAX_VALUE
            for(i in 0 until 3){
                for(j in 0 until 3){
                    if(boardList[i][j]!!.text == ""){
                        boardList[i][j]!!.text = firstTurn
                        val score = minimax(boardList,depth+1, true)
                        boardList[i][j]!!.text = ""
                        if(score < bestScore){
                            bestScore = score
                        }
                    }
                }
            }
            return bestScore
        }
    }

}










