package com.example.ecomapp.Firebase

import com.example.ecomapp.Data.Cartproduct
import com.example.ecomapp.Util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception


class Firebasecommon(
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
) {

    private val cart_collection =
        firestore.collection(Constants.user_collection).document(firebaseAuth.uid!!)
            .collection("cart")


    fun addProducttocart(
        cartproduct: Cartproduct,
        onResult: (Cartproduct?, java.lang.Exception?) -> Unit
    ) {
        cart_collection.document().set(cartproduct)
            .addOnSuccessListener {
                onResult(cartproduct, null)

            }
            .addOnFailureListener {
                onResult(null, it)
            }


    }

    fun increateQueantity(documentid: String, onResult: (String?, java.lang.Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val docref = cart_collection.document(documentid)
            val document = transaction.get(docref)
            val productobject = document.toObject(Cartproduct::class.java)
            productobject?.let { cartproduct ->
                val newqty = cartproduct.quantity + 1;
                val newproduct = cartproduct.copy(quantity = newqty)
                transaction.set(docref, newproduct)

            }


        }
            .addOnSuccessListener {
                onResult(documentid, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun decreaseQuantity(documentid: String, onResult: (String?, java.lang.Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val docref = cart_collection.document(documentid)
            val document = transaction.get(docref)
            val productobject = document.toObject(Cartproduct::class.java)
            productobject?.let { cartproduct ->
                val newqty = cartproduct.quantity - 1
                val newproduct = cartproduct.copy(quantity = newqty)
                transaction.set(docref, newproduct)

            }


        }
            .addOnSuccessListener {
                onResult(documentid, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    enum class Quantitychanging(){
        INCREASE,DECREASE
    }
}