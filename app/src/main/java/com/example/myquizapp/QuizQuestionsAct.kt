package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsAct : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0;
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null
    private var submitted:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)
        mQuestionsList = Constants.getQuestions()
        setQuestion()

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)

        btn_submit.setOnClickListener(this)
    }
    private fun setQuestion(){

        mQuestionsList = Constants.getQuestions()

        var ques : Question? = mQuestionsList!![mCurrentPosition-1]

        defaultOptionsView()
        submitted = false
        if(mCurrentPosition == mQuestionsList!!.size)
            btn_submit.text = "FINISH"
        else
            btn_submit.text = "SUBMIT"
        progress_bar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition/${progress_bar.max}"
//        if (ques != null) {
//            tv_ques.text = ques.question
//        }
//        or
        tv_ques.text = ques!!.question
        iv_image.setImageResource(ques!!.image)
        tv_option_one.text = ques!!.optionOne
        tv_option_two.text = ques!!.optionTwo
        tv_option_three.text = ques!!.optionThree
        tv_option_four.text = ques!!.optionFour
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0,tv_option_one)
        options.add(1,tv_option_two)
        options.add(2,tv_option_three)
        options.add(3,tv_option_four)

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_background
            )
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_option_one -> {
                if(!submitted)
                    selectedOptionView(tv_option_one,1)
            }
            R.id.tv_option_two -> {
                if(!submitted)
                    selectedOptionView(tv_option_two,2)
            }
            R.id.tv_option_three -> {
                if(!submitted)
                    selectedOptionView(tv_option_three,3)
            }
            R.id.tv_option_four -> {
                if(!submitted)
                    selectedOptionView(tv_option_four,4)
            }
            R.id.btn_submit -> {
                submitted = true
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition ++

                    when{
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else{
                    val question = mQuestionsList?.get(mCurrentPosition-1)
                    if(question!!.correctAns != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition,R.drawable.wrong_option_background)
                    }else {
                        mCorrectAnswers+=1
                    }
                        answerView(question.correctAns, R.drawable.correct_option_background)

                    if(mCurrentPosition == mQuestionsList!!.size){
                        btn_submit.text = "FINISH"
                    }else{
                        btn_submit.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }
    private fun answerView(answer: Int, drawableView : Int){
        when(answer){
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }
    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNumber

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_background
        )
    }
}