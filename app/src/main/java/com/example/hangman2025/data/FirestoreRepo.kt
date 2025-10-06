package com.example.hangman2025.data

import com.example.hangman2025.leaderboard.Score
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepo {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    fun saveUserProfile(username: String, email: String, onDone: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return onDone()
        val doc = db.collection("users").document(uid)
        val data = hashMapOf(
            "username" to username,
            "email" to email,
            "createdAt" to FieldValue.serverTimestamp()
        )
        doc.set(data).addOnCompleteListener { onDone() }
    }

    fun fetchUsername(onResult: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)
        db.collection("users").document(uid).get()
            .addOnSuccessListener { onResult(it.getString("username")) }
            .addOnFailureListener { onResult(null) }
    }

    fun saveScore(username: String, score: Int, word: String, onDone: () -> Unit) {
        val uid = auth.currentUser?.uid
        val data = hashMapOf(
            "uid" to uid,
            "username" to username,
            "score" to score,
            "word" to word,
            "createdAt" to FieldValue.serverTimestamp()
        )
        db.collection("scores").add(data).addOnCompleteListener { onDone() }
    }

    fun topScores(limit: Long = 10, onResult: (List<Score>) -> Unit) {
        db.collection("scores")
            .orderBy("score", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .addOnSuccessListener { qs ->
                val list = qs.documents.mapNotNull {
                    val username = it.getString("username") ?: return@mapNotNull null
                    val score = it.getLong("score")?.toInt() ?: 0
                    val word = it.getString("word") ?: ""
                    Score(username, score, word)
                }
                onResult(list)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}
