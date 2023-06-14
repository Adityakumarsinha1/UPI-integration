package com.example.paymentdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import com.example.easyupipayment.R
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.PaymentApp
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() , PaymentStatusListener{

    private lateinit var easyUpiPayment: EasyUpiPayment

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            initViews()
    }

    private fun initViews() {
//        val transactionId = "TID" + System.currentTimeMillis()
//        field_transaction_id.setText(transactionId)
//        field_transaction_ref_id.setText(transactionId)
//        "589827709"
        // Setup click listener for Pay button
        button_pay.setOnClickListener { pay() }
    }

    private fun pay() {
        val payeeVpa = "eazypay.589827709@icici"
        val payeeName = "VISHVKARMA ENTERPRISES"
        val transactionId = "TID" + System.currentTimeMillis()
        val transactionRefId = "TID" + System.currentTimeMillis()
        val payeeMerchantCode = ""
        val description = field_description.text.toString()
        val amount = field_amount.text.toString()
//        val paymentAppChoice = radioAppChoice

        val paymentApp =  PaymentApp.ALL


        try {
            // START PAYMENT INITIALIZATION
            easyUpiPayment = EasyUpiPayment(this) {
                this.paymentApp = paymentApp
                this.payeeVpa = payeeVpa
                this.payeeName = payeeName
                this.transactionId = transactionId
                this.transactionRefId = transactionRefId
                this.payeeMerchantCode = payeeMerchantCode
                this.description = description
                this.amount = amount
            }
            // END INITIALIZATION

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this)

            // Start payment / transaction
            easyUpiPayment.startPayment()
        }
        catch (e: Exception) {
            e.printStackTrace()
            toast("Error: ${e.message}")
        }
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString())
        textView_status.text = transactionDetails.toString()

        when (transactionDetails.transactionStatus) {
            TransactionStatus.SUCCESS -> onTransactionSuccess(transactionDetails)
            TransactionStatus.FAILURE -> onTransactionFailed(transactionDetails)
            TransactionStatus.SUBMITTED -> onTransactionSubmitted()
        }
    }

    override fun onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled by user")
        imageView.setImageResource(R.drawable.ic_failed)
    }

    private fun onTransactionSuccess(transactionDetails: TransactionDetails) {
        // Payment Success
        toast("Success")
        Log.d("TransactionDetails", transactionDetails.toString())
        imageView.setImageResource(R.drawable.ic_sucess)
    }

    private fun onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted")
        imageView.setImageResource(R.drawable.ic_sucess)
    }

    private fun onTransactionFailed(transactionDetails: TransactionDetails) {
        // Payment Failed
        toast("Failed")
        Log.d("TransactionDetails", transactionDetails.toString())
        imageView.setImageResource(R.drawable.ic_failed)
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    Uncomment this if you have inherited [android.app.Activity] and not [androidx.appcompat.app.AppCompatActivity]
//
//	override fun onDestroy() {
//		super.onDestroy()
//		easyUpiPayment.removePaymentStatusListener()
//	}
}